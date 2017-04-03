package liberman.text_extractor.controller;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.io.*;

import liberman.text_extractor.exeption.TextExtractionException;
import liberman.text_extractor.model.*;

public class TextExtractorPool {

	public boolean findSensitive(String path, String sensitive) 
			throws TextExtractionException{
		if(path == null) throw new TextExtractionException("path is null");
		ConcurrentLinkedDeque<String> stack = new ConcurrentLinkedDeque<>();
		stack.add(path);
		Set<Integer> hashCodes = new HashSet<>();
		hashCodes.add(path.hashCode());
		ConcurrentLinkedQueue<String> exeptions = new ConcurrentLinkedQueue<>();
		AtomicBoolean res = new AtomicBoolean(false);
		ExecutorService executor = Executors.newFixedThreadPool(4);
		
		while(!stack.isEmpty() && !res.get()){
			path = stack.removeLast();
			hashCodes.add(path.hashCode());
			if(!new File(path).isDirectory()){
				executor.submit(runable(path, stack, exeptions, sensitive, res));
			} else {
				addPaths(path, stack, exeptions, hashCodes);
			}
		}
		
		stop(executor);
		cleanTemp();
		if(res.get())
			return true;
		else if(!exeptions.isEmpty())
			throw new TextExtractionException(exeptions.toString());
		return false;
	}

	private void cleanTemp() {
		try {
			File dirTemp = new File("temp");
			if(FileUtils.sizeOfDirectory(dirTemp) != 0L){
				FileUtils.cleanDirectory(dirTemp);
				System.out.println("Log: cleaning temp");
			}
		} catch (IOException e) {
			System.out.println("Log: " + e.getMessage());
		}
	}

	private void addPaths(String path, ConcurrentLinkedDeque<String> stack, 
				ConcurrentLinkedQueue<String> exeptions, Set<Integer> hashCodes) {
		try {
			Collection<String> c = Files.walk(Paths.get(path))
				.map(Path::toString)
				.filter(str -> !hashCodes.contains(str.hashCode()))
				.collect(Collectors.toList());
			stack.addAll(c);
		} catch (Throwable e) {
			exeptions.add(e.getMessage());
		}
	}
	
	private Runnable runable(String path, ConcurrentLinkedDeque<String> stack, 
			ConcurrentLinkedQueue<String> exceptions, String sensitive, AtomicBoolean res) {
	    return () -> {
	    	try {
	    		System.out.println("Log: " + Thread.currentThread().getName() + " - " + path);
	    		IReader reader = ReaderFactory.getInstance(FilenameUtils.getExtension(path),exceptions);
				if(!reader.hasPaths()){
					if(((TextReader)reader).find(path,sensitive)){
						res.set(true);
					}
				}
				else{
					reader.addPaths(path, stack, exceptions);
				}
			} catch (Exception e) {
				exceptions.add(e.getMessage());
			}
	    };
	}
	
	private void stop(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(120, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("termination interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }
}
