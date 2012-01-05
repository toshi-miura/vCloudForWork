package work.bat;

import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import work.CalcPaymentImpl;
import work.Controller;
import work.ProjCodeCheckerImpl;
import work.VApp4Work;
import work.VcdConfImpl;
import work.WorkCalcConf;

public class PnoInvalidTaskTest {

	private static Controller contoroller;
	private static PnoInvalidTask pnoInvalidTask;

	@BeforeClass
	public static void beforClass() throws Exception {
		try {
			contoroller = new Controller(
					new CalcPaymentImpl(new WorkCalcConf()),
					new ProjCodeCheckerImpl(
							"C:\\Users\\user\\git\\vCloudForWork\\vCloudForWork\\conf\\P__20110401-20110630-0.txt"));

			pnoInvalidTask = new PnoInvalidTask(contoroller, new VcdConfImpl());

			Set<VApp4Work> vappInValidPno = contoroller
					.getVappInValidPno("KAIGIV5");
			System.out.println("INITTEST:" + vappInValidPno.size());

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}
	}

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
	public void testCall() throws Exception {

		pnoInvalidTask.call();
	}

}
