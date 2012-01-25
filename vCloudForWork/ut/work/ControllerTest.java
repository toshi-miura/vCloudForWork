package work;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import all.InjMgr;

import com.vmware.vcloud.sdk.VCloudException;

public class ControllerTest {

	private static Controller contoroller;

	@BeforeClass
	public static void beforClass() throws Exception {
		try {

			contoroller = InjMgr.create(Controller.class);

			// new Controller(new CalcPaymentImpl(new WorkCalcConf()),
			// new ProjCodeCheckerImpl(new VcdConfImpl()));

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

	@Test
	public void test単純動作確認() throws VCloudException {

		Set<VApp4Work> vappSet = contoroller.getVappSet("KAIGIV5");

		System.out
				.println("単純動作確認-------------------------------------------------");

		int hdd = 0;
		int mem = 0;
		for (VApp4Work vApp4Work : vappSet) {

			System.out.println(vApp4Work.toBaseString());
			mem += vApp4Work.getMemorySizeMB();
			hdd += vApp4Work.getTotalHDDGB();
		}
		System.out.println("SUM");
		System.out.println("HDD(GB)		" + hdd);
		System.out.println("mem(MB)		" + mem);

	}

	@Test
	public void test認証不正() throws VCloudException {

		Set<VApp4Work> vappSet = contoroller.getVappInValidAuth("KAIGIV5");

		System.out
				.println("認証不正-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {

			System.out.println(vApp4Work.toBaseString());
		}

	}

	@Test
	public void testPno不正() throws VCloudException {

		Set<VApp4Work> vappSet = contoroller.getVappInValidPno("KAIGIV5",
				new Date("2011/05/01"));

		System.out
				.println("Pno不正-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {

			System.out.println(vApp4Work.toBaseString());
		}

	}

	@Test
	public void testOKな設定() throws VCloudException {

		Set<VApp4Work> vappSet = contoroller.getVappSet("KAIGIV5");

		System.out
				.println("OKな設定-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {

			vApp4Work.setAuthStatus(true);
			vApp4Work.setpNo("LIPJ10030678");
			vApp4Work.metadataUpdate();
		}

		vappSet = contoroller.refresh(vappSet);

	}

	@Test
	public void testOKなはず() throws VCloudException {

		System.out.println("■OKなはず");
		Set<VApp4Work> vappSet = contoroller.getVappInValidAuth("KAIGIV5");
		Set<VApp4Work> vappSet2 = contoroller.getVappInValidPno("KAIGIV5",
				new Date("2011/05/01"));

		assertEquals(vappSet.size(), 0);
		assertEquals(vappSet2.size(), 0);

	}

	@Test
	public void testPno日付間違い() throws VCloudException {

		System.out.println("■Pno日付間違い");

		Set<VApp4Work> vappSet2 = contoroller.getVappInValidPno("KAIGIV5",
				new Date("2012/05/01"));

		for (VApp4Work vApp4Work : vappSet2) {

			System.out.println(vApp4Work.toBaseString());
		}

		assertEquals(vappSet2.size(), 5);
	}

	@Test
	public void testNGな設定() throws VCloudException {

		Set<VApp4Work> vappSet = contoroller.getVappSet("KAIGIV5");

		System.out
				.println("NGな設定-------------------------------------------------");

		for (VApp4Work vApp4Work : vappSet) {

			vApp4Work.setAuthStatus(false);
			vApp4Work.setpNo("DUMMY");
			vApp4Work.metadataUpdate();
		}

		vappSet = contoroller.refresh(vappSet);

	}

}
