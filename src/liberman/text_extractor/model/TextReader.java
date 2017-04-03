package liberman.text_extractor.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

public abstract class TextReader implements IReader{

	public boolean find(String path, String sensitive) throws IOException {
		try(Stream<String> stream = getStream(path)){
			return stream.anyMatch(str -> str.contains(sensitive));
		}
	}

	abstract protected Stream<String> getStream(String path) throws IOException;

	@Override
	public boolean hasPaths() {
		return false;
	}

	@Override
	public void addPaths(String path, LinkedList<String> stack, 
			LinkedList<String> exeptions) {		
	}
	
	@Override
	public void addPaths(String path, ConcurrentLinkedDeque<String> stack, 
			ConcurrentLinkedQueue<String> exceptions) {		
	}
}
