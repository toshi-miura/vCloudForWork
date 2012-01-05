package work;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProjCodeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMain() throws IOException {
		new ProjCodeCheckerImpl(
				"C:\\Users\\user\\git\\vCloudForWork\\vCloudForWork\\conf\\P__20110401-20110630-0.txt");
	}

	@Test
	public void testMain2() throws IOException {
		ProjCodeCheckerImpl projCodeChecker = new ProjCodeCheckerImpl(
				"C:\\Users\\user\\git\\vCloudForWork\\vCloudForWork\\conf\\P__20110401-20110630-0.txt");

		assertNotNull(projCodeChecker.getProjCodeInfo("LIPJ1104D140"));
		assertNotNull(projCodeChecker.getProjCodeInfo("LIPJ1104D151"));
		assertNotNull(projCodeChecker.getProjCodeInfo("LIPJ1104D152"));

		assertNotNull(projCodeChecker.getProjCodeInfo("LIPJ1104D154"));
		assertNotNull(projCodeChecker.getProjCodeInfo("LIPJ1104D155"));

	}

	@Test
	public void testMain3() throws IOException {
		ProjCodeChecker projCodeChecker = new ProjCodeCheckerImpl(
				"C:\\Users\\user\\git\\vCloudForWork\\vCloudForWork\\conf\\P__20110401-20110630-0.txt");

		Date date = new Date("2011/05/01");
		assertTrue(projCodeChecker.valid("LIPJ1104D140", date));
		assertTrue(projCodeChecker.valid("LIPJ1104D151", date));
		assertTrue(projCodeChecker.valid("LIPJ1104D152", date));

		assertTrue(projCodeChecker.valid("LIPJ1104D154", date));
		assertTrue(projCodeChecker.valid("LIPJ1104D155", date));

	}

	@Test
	public void testMain4() throws IOException {
		ProjCodeChecker projCodeChecker = new ProjCodeCheckerImpl(
				"C:\\Users\\user\\git\\vCloudForWork\\vCloudForWork\\conf\\P__20110401-20110630-0.txt");

		Date date = new Date("2012/05/01");
		assertFalse(projCodeChecker.valid("LIPJ1104D140", date));
		assertFalse(projCodeChecker.valid("LIPJ1104D151", date));
		assertFalse(projCodeChecker.valid("LIPJ1104D152", date));

		assertFalse(projCodeChecker.valid("LIPJ1104D154", date));
		assertFalse(projCodeChecker.valid("LIPJ1104D155", date));

	}

	@Test
	public void testMain5() throws IOException {
		ProjCodeChecker projCodeChecker = new ProjCodeCheckerImpl(
				"C:\\Users\\user\\git\\vCloudForWork\\vCloudForWork\\conf\\P__20110401-20110630-0.txt");

		Date date = new Date("2011/05/01");
		assertFalse(projCodeChecker.valid("aaa", date));
		assertFalse(projCodeChecker.valid("bbb", date));
		assertFalse(projCodeChecker.valid("LIPJ1104D153", date));

		assertFalse(projCodeChecker.valid("ccc", date));
		assertFalse(projCodeChecker.valid("ddd", date));

	}

}
