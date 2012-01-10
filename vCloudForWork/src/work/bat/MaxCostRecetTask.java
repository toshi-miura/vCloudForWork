package work.bat;

import java.util.Set;
import java.util.concurrent.Callable;

import work.Controller;
import work.VApp4Work;
import work.VcdConf;

/**
 * 月末にはしらせる。
 * TODO
 * @author user
 *
 */
public class MaxCostRecetTask extends Task implements Callable<Void> {

	public MaxCostRecetTask(Controller cont, VcdConf conf) {
		super();
		this.cont = cont;
		this.conf = conf;
	}

	@Override
	public Void call() throws Exception {

		Set<VApp4Work> allVapp = cont.getVappSet(conf.vcdName);
		for (VApp4Work vApp4Work : allVapp) {

			// GIT

			// 振替終了後に実行する。
			vApp4Work.resetMaxCost();
		}

		return null;
	}

}
