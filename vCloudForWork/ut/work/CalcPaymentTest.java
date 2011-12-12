package work;

import static org.junit.Assert.fail;

import java.util.Set;

import my.VMDetailsMapper;
import mydata.VApp;

import org.junit.BeforeClass;
import org.junit.Test;

import utconf.Conf;
import work.CalcPayment.Entry;

import com.vmware.vcloud.sdk.VCloudException;

public class CalcPaymentTest {

	private static VMDetailsMapper mapper;

	@BeforeClass
	public static void beforClass() throws Exception {
		try {
			mapper = new VMDetailsMapper(Conf.HOST, Conf.USER, Conf.PASS);
			mapper.run();
		} catch (Exception e) {

			e.printStackTrace();
			fail();

		}
	}

	@Test
	public void testCalcVApp() throws VCloudException {

		CalcPayment calcPayment = new CalcPayment(new WorkCalcConf());
		for (VApp app : mapper.getVappSet("KAIGIV5")) {
			int result = calcPayment.calc(app);

			System.out.println(app.getName() + "\t" + result);
		}

	}

	@Test
	public void testCalcCollectionOfVApp() throws VCloudException {

		CalcPayment calcPayment = new CalcPayment(new WorkCalcConf());
		Set<VApp> vappSet = mapper.getVappSet("KAIGIV5");

		Set<Entry> calc = calcPayment.calc(vappSet);

		for (Entry entry : calc) {
			System.out.println(entry.getVapp().getName() + "\t"
					+ entry.getPayment());
		}

	}

}
