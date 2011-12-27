package work;

import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import work.CalcPayment.Entry;

import com.vmware.vcloud.sdk.VCloudException;

public class ControllerTest {

	private static Controller contoroller;

	@BeforeClass
	public static void beforClass() throws Exception {
		try {
			contoroller = new Controller();

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

	// @Test
	public void testWorkVapp() throws VCloudException {

		Set<VApp4Work> vappSet = contoroller.getVappSetByUser("KAIGIV5",
				"miura");

		CalcPayment calcPayment = new CalcPayment(new WorkCalcConf());
		Set<Entry<VApp4Work>> calc = calcPayment.calc(vappSet);

		for (Entry<VApp4Work> e : calc) {
			System.out.println(e.getVapp().toBaseString() + "\t"
					+ e.getPayment());

		}
		System.out.println();

	}

	public void testWriteAttr() throws VCloudException {

		Set<VApp4Work> vappSet = contoroller.getVappSetByUser("KAIGIV5",

		"miura");

		System.out
				.println("値なし-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {

			System.out.println(vApp4Work.toBaseString());
		}
		System.out.println("-------------------------------------------------");
		for (VApp4Work vApp4Work : vappSet) {
			vApp4Work.setStartDate("2010/10/10");
			vApp4Work.setAuthor("★★俺が承認者だ！！");
			vApp4Work.setpNo("★★P#01101");

			vApp4Work.metadataUpdate();
			System.out.println("...");
		}
		System.out
				.println("値あり-------------------------------------------------");

		try {
			Thread.sleep(10 * 1l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// contoroller.refresh();
		vappSet = contoroller.getVappSetByUser("KAIGIV5",

		"miura");
		System.out.println("........");
		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out.println("-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {
			vApp4Work.setStartDate("");
			vApp4Work.setAuthor("");
			vApp4Work.setpNo("");
			vApp4Work.metadataUpdate();
			System.out.println("...");

		}
		try {
			Thread.sleep(10 * 1l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out
				.println("空になってほしい-------------------------------------------------");
		// contoroller.refresh();
		vappSet = contoroller.getVappSetByUser("KAIGIV5",

		"miura");
		System.out.println("........");
		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out.println("-------------------------------------------------");
		System.out.println();

	}

	// @Test
	public void testWriteAttrコントローラレベルのリフレッシュ() throws VCloudException {

		Set<VApp4Work> vappSet = contoroller.getVappSetByUser("KAIGIV5",

		"miura");

		System.out
				.println("値なし-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {

			System.out.println(vApp4Work.toBaseString());
		}
		System.out
				.println("update-------------------------------------------------");
		for (VApp4Work vApp4Work : vappSet) {
			vApp4Work.setStartDate("2010/10/10");
			vApp4Work.setAuthor("★★俺が承認者だ！！");
			vApp4Work.setpNo("★★P#01101");

			vApp4Work.metadataUpdate();
			System.out.println("...");
		}
		System.out
				.println("refresr-------------------------------------------------");
		contoroller.refresh();
		System.out.println("getUsr...");
		vappSet = contoroller.getVappSetByUser("KAIGIV5", "miura");
		System.out.println("...");

		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out.println("-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {
			vApp4Work.setStartDate("");
			vApp4Work.setAuthor("");
			vApp4Work.setpNo("");
			vApp4Work.metadataUpdate();

			System.out.println("...");
		}

		System.out
				.println("refresr-------------------------------------------------");
		contoroller.refresh();
		System.out.println("getUsr...");
		vappSet = contoroller.getVappSetByUser("KAIGIV5", "miura");
		System.out.println("...");

		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out.println("-------------------------------------------------");
		System.out.println();

	}

	// @Test
	public void testWriteAttrVAPPレベルのリフレッシュ() throws VCloudException {

		Set<VApp4Work> vappSet = contoroller.getVappSetByUser("KAIGIV5",

		"miura");

		System.out
				.println("値なし-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {

			System.out.println(vApp4Work.toBaseString());
		}
		System.out
				.println("update-------------------------------------------------");
		for (VApp4Work vApp4Work : vappSet) {
			vApp4Work.setStartDate("2010/10/10");
			vApp4Work.setAuthor("★★俺が承認者だ！！");
			vApp4Work.setpNo("★★P#01101");

			vApp4Work.metadataUpdate();
			System.out.println("...");
		}

		// METADATAのキャッシュから読み取りをしている。
		// キャッシュがないと値が古い値になる。
		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out
				.println("refresr-------------------------------------------------");
		// VAPPの更新。重い。
		vappSet = contoroller.refresh(vappSet);

		System.out.println("...");

		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out.println("-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {
			vApp4Work.setStartDate("");
			vApp4Work.setAuthor("");
			vApp4Work.setpNo("");
			vApp4Work.metadataUpdate();

			System.out.println("...");
		}

		System.out
				.println("refresr-------------------------------------------------");
		vappSet = contoroller.refresh(vappSet);
		System.out.println("...");

		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out.println("-------------------------------------------------");
		System.out.println();

	}

	// @Test
	public void testWriteAttrVAPPレベルのリフレッシュを非同期でする() throws VCloudException {

		Set<VApp4Work> vappSet = contoroller.getVappSetByUser("KAIGIV5",

		"miura");

		System.out
				.println("値なし-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {

			System.out.println(vApp4Work.toBaseString());
		}
		System.out
				.println("update-------------------------------------------------");
		for (VApp4Work vApp4Work : vappSet) {
			vApp4Work.setStartDate("2010/10/10");
			vApp4Work.setAuthor("★★俺が承認者だ！！");
			vApp4Work.setpNo("★★P#01101");

			vApp4Work.metadataUpdate();
			System.out.println("...");
		}

		// METADATAのキャッシュから読み取りをしている。
		// キャッシュがないと値が古い値になる。
		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out
				.println("refresr-------------------------------------------------");
		// VAPPの更新。重い。
		contoroller.refreshAsync(vappSet);

		System.out.println("...");

		// 古いオブジェクトでの起動
		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out.println("-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {
			vApp4Work.setStartDate("");
			vApp4Work.setAuthor("");
			vApp4Work.setpNo("");
			vApp4Work.metadataUpdate();

			System.out.println("...");
		}

		System.out
				.println("refresr-------------------------------------------------");
		vappSet = contoroller.refresh(vappSet);
		System.out.println("...");

		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out.println("-------------------------------------------------");
		System.out.println();

	}

	@Test
	public void testWriteAttrVAPPレベルのリフレッシュを非同期でする2() throws VCloudException {

		Set<VApp4Work> vappSet = contoroller.getVappSetByUser("KAIGIV5",

		"miura");

		System.out
				.println("値なし-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {

			System.out.println(vApp4Work.toBaseString());
		}
		System.out
				.println("update-------------------------------------------------");
		for (VApp4Work vApp4Work : vappSet) {
			vApp4Work.setStartDate("2010/10/10");
			vApp4Work.setAuthor("★★俺が承認者だ！！");
			vApp4Work.setpNo("★★P#01101");

			vApp4Work.metadataUpdate();
			System.out.println("...");
		}

		// METADATAのキャッシュから読み取りをしている。
		// キャッシュがないと値が古い値になる。
		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out
				.println("refresr-------------------------------------------------");
		// VAPPの更新。重い。
		contoroller.refreshAsync(vappSet);

		System.out.println("...");

		// 古いオブジェクトでの起動
		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out.println("-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {
			vApp4Work.setStartDate("");
			vApp4Work.setAuthor("");
			vApp4Work.setpNo("");
			vApp4Work.metadataUpdate();

			System.out.println("...");
		}

		System.out
				.println("refresr-------------------------------------------------");
		contoroller.refreshAsync(vappSet);
		System.out.println("...");

		for (VApp4Work vApp4Work : vappSet) {
			System.out.println(vApp4Work.toBaseString());
		}
		System.out.println("-------------------------------------------------");
		System.out.println();

	}

	/**
	 * パフォーマンスレベルを見るために。
	 * 通信のタイミングをあまり理解していないため。
	 * @throws VCloudException
	 */
	// @Test
	public void testWorkVappLoop() throws VCloudException {
		for (int i = 0; i < 10; i++) {
			Set<VApp4Work> vappSet = contoroller.getVappSetByUser("KAIGIV5",
					"miura");

			CalcPayment calcPayment = new CalcPayment(new WorkCalcConf());
			Set<Entry<VApp4Work>> calc = calcPayment.calc(vappSet);

			for (Entry<VApp4Work> e : calc) {
				System.out.println(e.getVapp().toBaseString() + "\t"
						+ e.getPayment());

			}
		}
		System.out.println();

	}

}
