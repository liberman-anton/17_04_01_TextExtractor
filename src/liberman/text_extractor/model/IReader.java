package liberman.text_extractor.model;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface IReader {
	boolean hasPaths();
	void addPaths(String path, LinkedList<String> stack, 
			LinkedList<String> exeptions);
	void addPaths(String path, ConcurrentLinkedDeque<String> stack, ConcurrentLinkedQueue<String> exceptions);
}
