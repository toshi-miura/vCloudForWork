package work.bat;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import my.VMDetailsMapper;
import mydata.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import work.Controller;
import work.VApp4Work;
import work.VcdConf;

import com.vmware.vcloud.sdk.VCloudException;

public class AuthInvalidTask extends Task implements Callable<Void> {

	private static Logger log = LoggerFactory.getLogger(VMDetailsMapper.class);

	public AuthInvalidTask(Controller cont, VcdConf conf) {
		super();
		this.cont = cont;
		this.conf = conf;
	}

	@Override
	public Void call() throws Exception {

		Set<VApp4Work> vappInValidPno = cont.getVappInValidAuth(conf.vcdName);
		for (VApp4Work vApp4Work : vappInValidPno) {
			List<User> users = vApp4Work.getAllUsers();

			sendMail(vApp4Work, users);
		}

		return null;
	}

	private void sendMail(VApp4Work vapp, List<User> users)
			throws VCloudException {

		String temple = load(this.conf.AuthInvalidTaskTemplatePath);

		MessageFormat mf = new MessageFormat(temple);
		String format = mf.format(new Object[] { vapp.getName(), vapp.getpNo(),
				vapp.costPerMonth() });

		log.info("");
		log.info("送付先");
		for (String mail : getMailAddress(users)) {
			log.info(mail);
		}
		log.info("");
		log.info("メール本文");

		log.info(format);

	}

}
