package liberman.text_extractor.model;

import java.util.*;

import liberman.text_extractor.exeption.TextExtractionException;
import liberman.text_extractor.model.readers.PdfReader;
import liberman.text_extractor.model.readers.TxtReader;

public class ReaderFactory {
	
	static Map<String,Class<? extends Reader>> map = new HashMap<>();
	
	static{
		map.put("txt", TxtReader.class);
		map.put("pdf", PdfReader.class);
	}

	public static Reader getInstance(String extenstion, LinkedList<String> exceptions) 
			throws TextExtractionException, InstantiationException, IllegalAccessException{
		Class<? extends Reader> clazz = map.get(extenstion);
		if(clazz == null){
			clazz = TxtReader.class;
			exceptions.add("Not found class for extension: " + extenstion);
		}	
		return clazz.newInstance();
	}

}
