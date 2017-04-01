package liberman.text_extractor.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import liberman.text_extractor.exeption.TextExtractionException;
import liberman.text_extractor.model.Reader;
import liberman.text_extractor.model.ReaderFactory;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class TextExtractor {
	
	public boolean findSensitive(String path, String sensitive) 
			throws TextExtractionException{
		LinkedList<String> stack = new LinkedList<>();
		stack.add(path);
		Set<Integer> hashCodes = new HashSet<>();
		hashCodes.add(path.hashCode());
		Set<Integer> zips = new HashSet<>();
		Reader reader;
		String extention;
		LinkedList<String> exeptions = new LinkedList<>();
		while(!stack.isEmpty()){
			path = stack.removeLast();
			hashCodes.add(path.hashCode());
			extention = FilenameUtils.getExtension(path);
			if(!new File(path).isDirectory() && !"zip".equals(extention)){
				try {
					reader = ReaderFactory.getInstance(extention,exeptions);
					if(reader.find(path,sensitive)){
						cleanTemp(zips);
						return true;
					}
				} catch (Exception e) {
					exeptions.add(e.getMessage());
				}	
			} else {
				addPaths(path, stack, exeptions, hashCodes, extention, zips);
			}
		}
		cleanTemp(zips);
		if(!exeptions.isEmpty())
			throw new TextExtractionException(exeptions.toString());
		return false;
	}

	private void cleanTemp(Set<Integer> zips) {
		if(zips.isEmpty()) return;
		try {
			FileUtils.cleanDirectory(new File("temp"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void addPaths(String path, LinkedList<String> stack, 
				LinkedList<String> exeptions, Set<Integer> hashCodes, String extention, Set<Integer> zips) {
		if("zip".equals(extention)){
			try {
				ZipFile zipFile = new ZipFile(path);
				String newPath = "temp/" + FilenameUtils.getBaseName(path);
				zipFile.extractAll(newPath);
				Integer hash = newPath.hashCode();
				stack.add(newPath);
				zips.add(hash);
			} catch (ZipException e) {
				exeptions.add(e.getMessage());
			}
		} else{
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
}
