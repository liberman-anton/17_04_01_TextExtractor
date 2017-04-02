package liberman.text_extractor.model.readers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import liberman.text_extractor.model.TextReader;

public class TxtReader extends TextReader {

	@Override
	protected Stream<String> getStream(String path) throws IOException {
		return Files.lines(Paths.get(path));
	}

}
