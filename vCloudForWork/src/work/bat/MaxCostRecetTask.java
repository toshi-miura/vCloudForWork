package work.bat;

import java.util.Set;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import work.Controller;
import work.VApp4Work;
import work.VcdConf;

import com.google.inject.Inject;

/**
 * 月末にはしらせる。
 * TODO 結果のログ出力をどうするか・・？
 * @author user
 *
 */
public class MaxCostRecetTask extends Task implements Callable<Void> {

	private static Logger log = LoggerFactory.getLogger(MaxCostRecetTask.class);

	@Inject
	public MaxCostRecetTask(Controller cont, VcdConf conf) {
		super();
		this.cont = cont;
		this.conf = conf;
	}

	@Override
	public Void call() throws Exception {

		Set<VApp4Work> allVapp = cont.getVappSet(conf.vcdName);
		StringBuilder sb = new StringBuilder();
		for (VApp4Work vApp4Work : allVapp) {

			// GIT
			sb.append(vApp4Work.getMaxCostInfoStr("\t")).append("\n");

			// 振替終了後に実行する。
			vApp4Work.resetMaxCost();
			vApp4Work.metadataUpdate();
		}
		cont.refresh(allVapp);
		log.info("課金情報を出力します。\n{}\n", sb);

		return null;
	}

}
