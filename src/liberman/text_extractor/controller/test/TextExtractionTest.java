package liberman.text_extractor.controller.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import liberman.text_extractor.controller.TextExtractor;
import liberman.text_extractor.exeption.TextExtractionException;

public class TextExtractionTest {
	private static final String PATH_1 = 
			"C:/Users/Anton/workspace/17_03_31_TextExtractor/test";
			//"test";//"test";//"test/";
	private static final String PATH_2 = "test1/test.txt";
	private static final String PATH_DOC = "test.doc";
	private static final String PATH_XLS = "test.xls";
	private static final String PATH_TEXT = "text";
	
	private static final String SENSITIVE_1 = "yes";
	private static final String SENSITIVE_2 = "no";
	private static final String SENSITIVE_3 = "good";
	private static final String SENSITIVE_4 = "doc";
	
	
	TextExtractor textExtractor = new TextExtractor();
	Exception exp;
	
	@Before
	public void setUp() throws Exception {
		exp = null;
	}

	@Test
	public void testFound() {
		try {
			assertTrue(textExtractor.findSensitive(PATH_1, SENSITIVE_1));
		} catch (TextExtractionException e) {
			exp = e;
		}
		assertNull(exp);
	}
	
	@Test
	public void testTextFound() {
		try {
			assertTrue(textExtractor.findSensitive(PATH_TEXT, "texts"));
		} catch (TextExtractionException e) {
			exp = e;
		}
		assertNull(exp);
	}

	@Test
	public void testNotFound(){
		try {
			assertFalse(textExtractor.findSensitive(PATH_1, SENSITIVE_2));
		} catch (TextExtractionException e) {
			exp = e;
		}
		assertNull(exp);
	}
	
	@Test
	public void testWrongPath(){
		try {
			assertFalse(textExtractor.findSensitive(PATH_2, SENSITIVE_1));
		} catch (Exception e) {
			exp = e;
		}
		assertNotNull(exp);
	}
	
	@Test
	public void testSubFolder() {
		try {
			assertTrue(textExtractor.findSensitive(PATH_1, SENSITIVE_3));
		} catch (TextExtractionException e) {
			exp = e;
		}
		assertNull(exp);
	}
	
	@Test
	public void testDocFound(){
		try {
			assertTrue(textExtractor.findSensitive(PATH_DOC, SENSITIVE_4));
		} catch (Exception e) {
			exp = e;
		}
		assertNull(exp);
	}
	
	@Test
	public void testDocNotFound(){
		try {
			assertFalse(textExtractor.findSensitive(PATH_DOC, SENSITIVE_4 + SENSITIVE_1));
		} catch (Exception e) {
			exp = e;
		}
		assertNotNull(exp);
		assertEquals("[Not found class for extension: doc]",exp.getMessage());
	}
	
	@Test
	public void testXls(){
		try {
			assertTrue(textExtractor.findSensitive(PATH_XLS, SENSITIVE_1 + SENSITIVE_2));
		} catch (Exception e) {
			exp = e;
		}
		assertNotNull(exp);
	}
	
	@Test
	public void testPdfFound() {
		try {
			assertTrue(textExtractor.findSensitive("test.pdf", "Good"));
		} catch (TextExtractionException e) {
			exp = e;
		}
		assertNull(exp);
	}
	
	@Test
	public void testPdfNotFound(){
		try {
			assertFalse(textExtractor.findSensitive("test.pdf", "golangolan"));
		} catch (TextExtractionException e) {
			exp = e;
		}
		assertNull(exp);
	}
	
	@Test
	public void testZipFound() {
		try {
			assertTrue(textExtractor.findSensitive("test.zip", "Microsoft"));
		} catch (TextExtractionException e) {
			exp = e;
		}
		assertNull(exp);
	}
	
	@Test
	public void testZipNotFound(){
		try {
			assertFalse(textExtractor.findSensitive("test.zip", "golangolan"));
		} catch (TextExtractionException e) {
			exp = e;
		}
		assertNull(exp);
	}
}
