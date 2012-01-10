package work.bat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import work.Controller;
import work.VcdConf;

public class TaskRunner implements Runnable {

	private static Logger log = LoggerFactory.getLogger(TaskRunner.class);

	protected Controller cont;
	protected VcdConf conf;

	public TaskRunner(Controller cont, VcdConf conf) {
		super();
		this.cont = cont;
		this.conf = conf;
	}

	@Override
	public void run() {

		String dir = this.conf.TaskRunDir;
		File[] listFiles = new File(dir).listFiles();

		for (File file : listFiles) {
			try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
				String line = bf.readLine();
				Object instance = Class.forName(line).newInstance();

			} catch (IOException e) {
			} catch (InstantiationException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

		}

	}

}
