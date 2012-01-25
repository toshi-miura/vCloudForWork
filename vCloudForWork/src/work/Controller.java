package work;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utconf.Conf;
import base.my.VMDetailsMapper;
import base.mydata.VApp;

import com.google.inject.Inject;
import com.vmware.vcloud.sdk.VCloudException;

public class Controller {

	private static Logger log = LoggerFactory.getLogger(Controller.class);

	private VMDetailsMapper mapper;
	private final Executor executor = Executors.newFixedThreadPool(4);

	private CalcPayment calc = null;

	private ProjCodeChecker checker = null;

	@Inject
	public Controller(CalcPayment calc, ProjCodeChecker checker) {

		log.info("Controller");

		this.calc = calc;
		this.checker = checker;
		init();
		refresh();
		refreshTask();

	}

	public void init() {
		try {
			log.info("init");
			mapper = new VMDetailsMapper(Conf.HOST, Conf.USER, Conf.PASS);

		} catch (KeyManagementException e) {

			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {

			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (KeyStoreException e) {

			e.printStackTrace();
		} catch (VCloudException e) {

			e.printStackTrace();
		}
	}

	public void refresh() {
		try {

			log.info("refresh");
			mapper.run();
			// リフレッシュのタイミングで、VAPPのイニットを走らせる。

			Set<String> vcdNameSet = mapper.getVCDNameSet();
			for (String vcdName : vcdNameSet) {
				getVappSet(vcdName);

			}

		} catch (VCloudException e) {

			e.printStackTrace();
			// エラーが出たら、とりあえず、再接続
			init();
		}

	}

	/**
	 * 一度のみ呼ぶ。
	 * スケジューラを使って定期感覚でデータを更新する。
	 */
	private void refreshTask() {
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
				1);
		executor.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				log.info("refreshTask");
				refresh();

			}
		}, 60, 60 * 5, TimeUnit.SECONDS);

	}

	public Set<VApp4Work> getVappSet(String vcdNamd) throws VCloudException {
		return toWork(mapper.getVappSet(vcdNamd));
	}

	public int getTotalMemoryGB(String vcdNamd) throws VCloudException {
		Set<VApp4Work> work = toWork(mapper.getVappSet(vcdNamd));
		int r = 0;
		for (VApp4Work vApp4Work : work) {
			r += vApp4Work.getMemorySizeMB();
		}

		return r / 1024;
	}

	public int getTotalHDDGB(String vcdNamd) throws VCloudException {
		Set<VApp4Work> work = toWork(mapper.getVappSet(vcdNamd));
		int r = 0;
		for (VApp4Work vApp4Work : work) {
			r += vApp4Work.getTotalHDDGB();
		}

		return r / 1024;
	}

	public Set<VApp4Work> getVappSetByUser(String vcdNamd, String userid)
			throws VCloudException {
		return toWork(mapper.getVappSetByUser(vcdNamd, userid));
	}

	/**
	 * 未承認のアプリのみ返します。
	 * @param vcdNamd
	 * @return
	 * @throws VCloudException
	 */
	public Set<VApp4Work> getVappInValidAuth(String vcdNamd)
			throws VCloudException {

		Set<VApp4Work> work = toWork(mapper.getVappSet(vcdNamd));
		Set<VApp4Work> result = new HashSet<VApp4Work>();

		for (VApp4Work vApp4Work : work) {
			if (!vApp4Work.isAuthStatus()) {
				result.add(vApp4Work);
			}
		}
		return result;
	}

	/**
	 * Pno不正のマシンのみをとりだす。
	 * @param vcdNamd
	 * @return
	 * @throws VCloudException
	 */
	public Set<VApp4Work> getVappInValidPno(String vcdNamd)
			throws VCloudException {

		return getVappInValidPno(vcdNamd, new Date());
	}

	/**
	 * Pno不正のマシンのみをとりだす。
	 * @param vcdNamd
	 * @return
	 * @throws VCloudException
	 */
	public Set<VApp4Work> getVappInValidPno(String vcdNamd, Date date)
			throws VCloudException {

		Set<VApp4Work> work = toWork(mapper.getVappSet(vcdNamd));
		Set<VApp4Work> result = new HashSet<VApp4Work>();

		for (VApp4Work vApp4Work : work) {
			if (!checker.valid(vApp4Work.getpNo(), date)) {
				result.add(vApp4Work);
			}
		}
		return result;
	}

	private Set<VApp4Work> toWork(Set<VApp> vappSet) throws VCloudException {
		Set<VApp4Work> vapp4workSet = new HashSet<VApp4Work>();
		for (VApp vApp : vappSet) {
			vapp4workSet.add(new VApp4Work(vApp, this.calc));
		}
		return vapp4workSet;
	}

	public VApp4Work refresh(VApp4Work vapp) throws VCloudException {
		VApp refresh = mapper.refresh(vapp);
		return new VApp4Work(refresh, this.calc);
	}

	public Set<VApp4Work> refresh(Set<VApp4Work> vappSet)
			throws VCloudException {

		return toWork(mapper.refresh(vappSet));
	}

	/**
	 * 非同期でキャッシュのクリアを実行
	 * @param vapp
	 * @throws VCloudException
	 */
	public void refreshAsync(final VApp4Work vapp) throws VCloudException {

		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					mapper.refresh(vapp);
					log.info("ASYNC END");
				} catch (VCloudException e) {
					// TODO
				}

			}
		});

	}

	/**
	 * 非同期でキャッシュのクリアを実行
	 * @param vapp
	 * @throws VCloudException
	 */
	public void refreshAsync(Set<VApp4Work> vappSet) throws VCloudException {

		for (VApp4Work vApp4Work : vappSet) {
			refreshAsync(vApp4Work);
		}
	}

	public CalcPayment getCalc() {
		return calc;
	}

	public void setCalc(CalcPayment calc) {
		this.calc = calc;
	}

	public ProjCodeChecker getChecker() {
		return checker;
	}

	public void setChecker(ProjCodeChecker checker) {
		this.checker = checker;
	}

}
