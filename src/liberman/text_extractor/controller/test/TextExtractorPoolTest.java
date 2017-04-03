package liberman.text_extractor.controller.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import liberman.text_extractor.controller.TextExtractorPool;
import liberman.text_extractor.exeption.TextExtractionException;

public class TextExtractorPoolTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	

	private static final String PATH_1 = "C:/Users/Anton/workspace/";
	Exception exception;
	TextExtractorPool tep;

	@Before
	public void setUp() throws Exception {
		exception = null;
		tep = new TextExtractorPool();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1True() {
		try {
			assertTrue(tep.findSensitive(PATH_1, "MoovitApp"));
		} catch (TextExtractionException e) {
			exception = e;
		}
		assertNull(exception);
	}
	
	@Test
	public void test2Exception() {
		boolean res = false;
		try {
			assertTrue(tep.findSensitive(PATH_1, "MoovitAppMoovitAp\n"));
		} catch (TextExtractionException e) {
			exception = e;
		}
		assertNotNull(exception);
		
	}

}
