package work.bat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mydata.User;
import work.Controller;
import work.VcdConf;

public class Task {

	protected Controller cont;
	protected VcdConf conf;

	public Task() {
		super();
	}

	public Controller getCont() {
		return cont;
	}

	public void setCont(Controller cont) {
		this.cont = cont;
	}

	public VcdConf getConf() {
		return conf;
	}

	public void setConf(VcdConf conf) {
		this.conf = conf;
	}

	public Set<String> getMailAddress(List<User> users) {
		HashSet<String> set = new HashSet<>();
		for (User user : users) {
			set.add(user.getEmailAddress());

		}
		return set;

	}

	public String load(String conf) {

		try (FileInputStream inputStream = new FileInputStream(conf);
				InputStreamReader isr = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(isr);) {
			StringBuffer sb = new StringBuffer();
			String s;
			while ((s = br.readLine()) != null) {
				sb.append(s);
				sb.append("\n");

			}
			return sb.toString();

		} catch (Exception e) {
			//
			e.printStackTrace();
			return e.getMessage();
		}

	}

}