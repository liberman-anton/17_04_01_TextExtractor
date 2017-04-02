package liberman.text_extractor.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import liberman.text_extractor.exeption.TextExtractionException;
import liberman.text_extractor.model.IReader;
import liberman.text_extractor.model.TextReader;
import liberman.text_extractor.model.ReaderFactory;

public class TextExtractor {
	
	public boolean findSensitive(String path, String sensitive) 
			throws TextExtractionException{
		LinkedList<String> stack = new LinkedList<>();
		stack.add(path);
		Set<Integer> hashCodes = new HashSet<>();
		hashCodes.add(path.hashCode());
		Boolean needCleaning = false;
		IReader reader;
		LinkedList<String> exeptions = new LinkedList<>();
		
		while(!stack.isEmpty()){
			path = stack.removeLast();
			hashCodes.add(path.hashCode());
			if(!new File(path).isDirectory()){
				try {
					reader = ReaderFactory.getInstance(FilenameUtils.getExtension(path),exeptions);
					if(!reader.hasPaths()){
						if(((TextReader)reader).find(path,sensitive)){
							cleanTemp(needCleaning);
							return true;
						}
					}
					else{
						reader.addPaths(path, stack, exeptions, needCleaning);
					}
				} catch (Exception e) {
					exeptions.add(e.getMessage());
				}	
			} else {
				addPaths(path, stack, exeptions, hashCodes);
			}
		}
		cleanTemp(needCleaning);
		if(!exeptions.isEmpty())
			throw new TextExtractionException(exeptions.toString());
		return false;
	}

	private void cleanTemp(Boolean needCleaning) {
		if(!needCleaning) return;
		needCleaning = false;
		try {
			FileUtils.cleanDirectory(new File("temp"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void addPaths(String path, LinkedList<String> stack, 
				LinkedList<String> exeptions, Set<Integer> hashCodes) {
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
}
