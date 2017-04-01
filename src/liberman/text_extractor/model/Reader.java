package liberman.text_extractor.model;

import java.io.IOException;
import java.util.stream.Stream;

public abstract class Reader {

	public boolean find(String path, String sensitive) throws IOException {
		try(Stream<String> stream = getStream(path)){
			return stream.anyMatch(str -> str.contains(sensitive));
		}
	}

	abstract protected Stream<String> getStream(String path) throws IOException;
}
