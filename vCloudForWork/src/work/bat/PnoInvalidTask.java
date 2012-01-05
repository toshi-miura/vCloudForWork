package work.bat;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import mydata.User;
import work.Controller;
import work.VApp4Work;
import work.VcdConf;

import com.vmware.vcloud.sdk.VCloudException;

public class PnoInvalidTask extends Task implements Callable<Void> {

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

			System.out.println("-----------------------");
			System.out.println(vApp4Work.getName());
			for (User user : users) {
				System.out.println(user);

			}
			sendMail(vApp4Work, users);
		}

		return null;
	}

	private void sendMail(VApp4Work vapp, List<User> users)
			throws VCloudException {

		String temple = load(conf.PnoInvalidTaskTemplatePath);

		MessageFormat mf = new MessageFormat(temple);
		String format = mf
				.format(new String[] { vapp.getName(), vapp.getpNo() });
		System.out.println("送付先");
		for (String mail : getMailAddress(users)) {
			System.out.println(mail);
		}
		System.out.println("メール本文");
		System.out.println(format);

	}

}
