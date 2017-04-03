package liberman.text_extractor.model;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import liberman.text_extractor.exeption.TextExtractionException;
import liberman.text_extractor.model.readers.PdfReader;
import liberman.text_extractor.model.readers.TxtReader;
import liberman.text_extractor.model.readers.ZipReader;

public class ReaderFactory {
	
	static Map<String,Class<? extends IReader>> map = new HashMap<>();
	
	static{
		map.put("txt", TxtReader.class);
		map.put("pdf", PdfReader.class);
		map.put("zip", ZipReader.class);
	}

	public static IReader getInstance(String extenstion, LinkedList<String> exceptions) 
			throws TextExtractionException, InstantiationException, IllegalAccessException{
		Class<? extends IReader> clazz = map.get(extenstion);
		if(clazz == null){
			clazz = TxtReader.class;
			exceptions.add("Not found class for extension: " + extenstion);
		}	
		return clazz.newInstance();
	}

	public static IReader getInstance(String extenstion, ConcurrentLinkedQueue<String> exceptions) 
			throws TextExtractionException, InstantiationException, IllegalAccessException{
		Class<? extends IReader> clazz = map.get(extenstion);
		if(clazz == null){
			clazz = TxtReader.class;
			exceptions.add("Not found class for extension: " + extenstion);
		}	
		return clazz.newInstance();
	}

}
