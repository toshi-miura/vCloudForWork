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

public class AuthInvalidTaskTest {

	private static Controller contoroller;
	private static AuthInvalidTask pnoInvalidTask;

	@BeforeClass
	public static void beforClass() throws Exception {
		try {
			contoroller = new Controller(
					new CalcPaymentImpl(new WorkCalcConf()),
					new ProjCodeCheckerImpl(
							BATTestConf.PNOFILE));

			pnoInvalidTask = new AuthInvalidTask(contoroller, new VcdConfImpl());

			Set<VApp4Work> vappInValidPno = contoroller
					.getVappInValidPno(BATTestConf.VCDNAME);
			System.out.println("INITTEST:" + vappInValidPno.size());

		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}
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
