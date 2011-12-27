package work;

import static org.junit.Assert.fail;

import java.util.HashSet;
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

			System.out.println(app.toBaseString() + "\t" + result);
		}

		System.out.println();
	}

	@Test
	public void testCalcCollectionOfVApp() throws VCloudException {

		CalcPayment calcPayment = new CalcPayment(new WorkCalcConf());
		Set<VApp> vappSet = mapper.getVappSet("KAIGIV5");

		Set<Entry<VApp>> calc = calcPayment.calc(vappSet);

		for (Entry<VApp> entry : calc) {
			System.out.println(entry.getVapp().toBaseString() + "\t"
					+ entry.getPayment());
		}

		System.out.println();

	}

	@Test
	public void testCalcCollectionOfVAppWork() throws VCloudException {

		CalcPayment calcPayment = new CalcPayment(new WorkCalcConf());
		Set<VApp> vappSet = mapper.getVappSet("KAIGIV5");

		Set<VApp4Work> vapp4workSet = new HashSet<VApp4Work>();
		for (VApp vApp : vappSet) {
			vapp4workSet.add(new VApp4Work(vApp));
		}

		Set<Entry<VApp4Work>> calc = calcPayment.calc(vapp4workSet);

		for (Entry<VApp4Work> entry : calc) {

			System.out.println(entry.getVapp().toBaseString() + "\t"
					+ entry.getPayment());
		}
		System.out.println();

	}

	@Test
	public void testCalcCollectionOfVAppWorkForLoop() throws VCloudException {

		CalcPayment calcPayment = new CalcPayment(new WorkCalcConf());
		Set<VApp> vappSet = mapper.getVappSet("KAIGIV5");

		Set<VApp4Work> vapp4workSet = new HashSet<VApp4Work>();
		for (VApp vApp : vappSet) {
			vapp4workSet.add(new VApp4Work(vApp));
		}

		for (int i = 0; i < 100; i++) {
			Set<Entry<VApp4Work>> calc = calcPayment.calc(vapp4workSet);
			for (Entry<VApp4Work> entry : calc) {

				System.out.println(entry.getVapp().toBaseString() + "\t"
						+ entry.getPayment());
			}
			System.out.println();
		}

	}
}
