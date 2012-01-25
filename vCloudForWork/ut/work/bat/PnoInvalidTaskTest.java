package work.bat;

import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import work.Controller;
import work.VApp4Work;
import work.VcdConfImpl;
import all.InjMgr;

public class PnoInvalidTaskTest {

	private static Controller contoroller;
	private static PnoInvalidTask pnoInvalidTask;

	@BeforeClass
	public static void beforClass() throws Exception {
		try {
			contoroller = InjMgr.create(Controller.class);

			pnoInvalidTask = new PnoInvalidTask(contoroller, new VcdConfImpl());

			Set<VApp4Work> vappInValidPno = contoroller
					.getVappInValidPno(BATTestConf.VCDNAME);
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
