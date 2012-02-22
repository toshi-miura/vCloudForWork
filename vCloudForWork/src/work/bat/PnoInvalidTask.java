package work.bat;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import work.Controller;
import work.VApp4Work;
import work.VcdConf;
import work.util.Sender;
import all.InjMgr;
import base.mydata.User;

import com.vmware.vcloud.sdk.VCloudException;

/**
 * PNOの不正を検知する。
 * 定期的に走らせる。
 * 基本夜間にする。
 *
 * @author user
 *
 */
public class PnoInvalidTask extends Task implements Callable<Void> {

	private static Logger log = LoggerFactory.getLogger(PnoInvalidTask.class);

	public PnoInvalidTask(Controller cont, VcdConf conf) {
		super();
		this.cont = cont;
		this.conf = conf;
	}

	@Override
	public Void call() throws Exception {

		Set<VApp4Work> vappInValidPno = cont.getVappInValidPno(conf.vcdName,
				new Date());
		for (VApp4Work vApp4Work : vappInValidPno) {
			List<User> users = vApp4Work.getAllUsers();

			log.info("-----------------------");
			log.info(vApp4Work.getName());
			for (User user : users) {
				log.info(user.toString());

			}
			sendMail(vApp4Work, users);
		}
		cont.refresh(vappInValidPno);

		return null;
	}

	private void sendMail(VApp4Work vapp, List<User> users)
			throws VCloudException, MessagingException {

		String temple = load(conf.PnoInvalidTaskTemplatePath);

		MessageFormat mf = new MessageFormat(temple);
		String format = mf
				.format(new String[] { vapp.getName(), vapp.getpNo() });
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
