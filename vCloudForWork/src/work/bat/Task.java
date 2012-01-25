package work.bat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import work.Controller;
import work.VcdConf;
import base.mydata.User;

public class Task {

	private static Logger log = LoggerFactory.getLogger(Task.class);

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

	/**
	 * TODO ライブラリのクラスに置き換える。
	 * ここにあるのが良いかは不明だが、とりあえずここに。
	 * @param conf
	 * @return
	 */
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