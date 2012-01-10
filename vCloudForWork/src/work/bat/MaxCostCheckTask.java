package work.bat;

import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import work.Controller;
import work.VApp4Work;
import work.VcdConf;

/**
 * １日一回、夜間に走る。
 * @author user
 *
 */
public class MaxCostCheckTask extends Task implements Callable<Void> {

	private static Logger log = LoggerFactory.getLogger(MaxCostCheckTask.class);

	public MaxCostCheckTask(Controller cont, VcdConf conf) {
		super();
		this.cont = cont;
		this.conf = conf;
	}

	@Override
	public Void call() throws Exception {

		Set<VApp4Work> allVapp = cont.getVappSet(conf.vcdName);
		for (VApp4Work vApp4Work : allVapp) {

			// 現在の構成の値段を取得して、以前の額より上だったら登録
			vApp4Work.setMaxCost();
		}

		return null;
	}

}
