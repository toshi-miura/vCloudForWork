package work;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import my.VMDetailsMapper;
import mydata.VApp;
import utconf.Conf;

import com.vmware.vcloud.sdk.VCloudException;

public class Controller {

	private VMDetailsMapper mapper;
	private final Executor executor = Executors.newFixedThreadPool(4);

	public Controller() {

		init();
		refresh();
		refreshTask();
	}

	public void init() {
		try {
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

			mapper.run();
		} catch (VCloudException e) {

			e.printStackTrace();
			// エラーが出たら、とりあえず、再接続
			init();
		}

	}

	/**
	 *
	 */
	private void refreshTask() {
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
				1);
		executor.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				init();

			}
		}, 60, 60, TimeUnit.SECONDS);

	}

	public Set<VApp4Work> getVappSet(String vcdNamd) throws VCloudException {
		return toWork(mapper.getVappSet(vcdNamd));
	}

	public Set<VApp4Work> getVappSetByUser(String vcdNamd, String userid)
			throws VCloudException {
		return toWork(mapper.getVappSetByUser(vcdNamd, userid));
	}

	private Set<VApp4Work> toWork(Set<VApp> vappSet) throws VCloudException {
		Set<VApp4Work> vapp4workSet = new HashSet<VApp4Work>();
		for (VApp vApp : vappSet) {
			vapp4workSet.add(new VApp4Work(vApp));
		}
		return vapp4workSet;
	}

	public VApp4Work refresh(VApp4Work vapp) throws VCloudException {
		VApp refresh = mapper.refresh(vapp);
		return new VApp4Work(refresh);
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
					System.out.println("ASYNC END");
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

}