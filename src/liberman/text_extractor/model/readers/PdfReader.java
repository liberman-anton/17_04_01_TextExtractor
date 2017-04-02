package liberman.text_extractor.model.readers;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import liberman.text_extractor.model.TextReader;

public class PdfReader extends TextReader {

	@Override
	protected Stream<String> getStream(String path) throws IOException {
		LinkedList<String> l = new LinkedList<>();
		Collections.addAll(l, extractsPdfLines(path));
		return l.stream();
	}
	
	private String[] extractsPdfLines(String PdfFile) throws IOException {
			StringBuffer buff = new StringBuffer();
			String ExtractedText = null;
			com.itextpdf.text.pdf.PdfReader reader = new com.itextpdf.text.pdf.PdfReader(PdfFile);
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			TextExtractionStrategy strategy;

			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
				ExtractedText = strategy.getResultantText().toString();
				buff.append(ExtractedText + "\n");
			}

			String[] LinesArray;
			LinesArray = buff.toString().split("\n");
			reader.close();
			return LinesArray;
	}
}
