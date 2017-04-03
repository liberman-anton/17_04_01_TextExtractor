package liberman.text_extractor.model.readers;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.io.FilenameUtils;

import liberman.text_extractor.model.IReader;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class ZipReader implements IReader {

	@Override
	public boolean hasPaths() {
		return true;
	}

	@Override
	public void addPaths(String path, LinkedList<String> stack, 
			LinkedList<String> exceptions) {
		try {
			ZipFile zipFile = new ZipFile(path);
			String newPath = "temp/" + FilenameUtils.getBaseName(path);
			zipFile.extractAll(newPath);
			stack.add(newPath);
		} catch (ZipException e) {
			exceptions.add(e.getMessage());
		}
	}	
	
	@Override
	public void addPaths(String path, ConcurrentLinkedDeque<String> stack, 
			ConcurrentLinkedQueue<String> exceptions) {		
		try {
			ZipFile zipFile = new ZipFile(path);
			String newPath = "temp/" + FilenameUtils.getBaseName(path);
			zipFile.extractAll(newPath);
			stack.add(newPath);
		} catch (ZipException e) {
			exceptions.add(e.getMessage());
		}
	}	
}
