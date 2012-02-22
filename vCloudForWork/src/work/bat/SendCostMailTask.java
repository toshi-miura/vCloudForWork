package work.bat;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import work.Controller;
import work.VApp4Work;
import work.VcdConf;
import work.util.InjMgr;
import work.util.Sender;
import base.mydata.User;

import com.google.inject.Inject;
import com.vmware.vcloud.sdk.VCloudException;

/**
 * 課金予定額を送るメール。
 * PNOが不正でもとりあえず送る。
 *
 * @author user
 *
 */
public class SendCostMailTask extends Task implements Callable<Void> {

	private static Logger log = LoggerFactory.getLogger(SendCostMailTask.class);

	@Inject
	public SendCostMailTask(Controller cont, VcdConf conf) {
		super();
		this.cont = cont;
		this.conf = conf;
	}

	@Override
	public Void call() throws Exception {

		// コストチェックを事前に呼んでおく。
		InjMgr.create(MaxCostCheckTask.class).call();

		Set<VApp4Work> allVapp = cont.getVappSet(conf.vcdName);
		for (VApp4Work vApp4Work : allVapp) {

			List<User> users = vApp4Work.getAllUsers();

			log.info("-----------------------");
			log.info(vApp4Work.getName());
			for (User user : users) {
				log.info(user.toString());

			}
			sendMail(vApp4Work, users);
		}
		cont.refresh(allVapp);

		return null;
	}

	private void sendMail(VApp4Work vapp, List<User> users)
			throws VCloudException, MessagingException {

		String temple = load(conf.SendCostMailTaskTemplatePath);

		MessageFormat mf = new MessageFormat(temple);
		String format = mf.format(new Object[] { vapp.getName(), vapp.getpNo(),
				vapp.getMaxCost(), vapp.getMaxCostCpu(), vapp.getMaxCostMem(),
				vapp.getMaxCostHDD(), vapp.getMaxCostDate()

		});
		log.info("送付先");
		for (String mail : getMailAddress(users)) {
			log.info(mail);
		}
		log.info("メール本文");

		int firstline = format.indexOf("\n");
		String title = format.substring(0, firstline);
		String body = format.substring(firstline, format.length());

		log.info("title={}", title);
		log.info(body);

		Sender sender = InjMgr.create(Sender.class);
		sender.sendMail(getMailAddress(users), title, body,
				"toshihiko.miura+program@gmail.com");

	}
}
