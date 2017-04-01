package liberman.text_extractor.model.readers;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import liberman.text_extractor.model.Reader;

public class PdfReader extends Reader {

	@Override
	protected Stream<String> getStream(String path) throws IOException {
		File file = new File(path);
		PDFParser parser = new PDFParser(new RandomAccessFile(file,"r")); // update for PDFBox V 2.0
		parser.parse();
		COSDocument cosDoc = parser.getDocument();
		PDFTextStripper pdfStripper = new PDFTextStripper();
		PDDocument pdDoc = new PDDocument(cosDoc);
		pdfStripper.setStartPage(1);
	    pdfStripper.setEndPage(pdDoc.getNumberOfPages());
		String str = pdfStripper.getText(pdDoc);
		pdDoc.close();cosDoc.close();
		LinkedList<String> l = new LinkedList<>();
		Collections.addAll(l, str.split("\n"));
		return l.stream();
	}

}
