package work.bat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import work.Controller;
import work.VcdConf;
import work.util.InjMgr;

import com.google.inject.Inject;

public class TaskRunner implements Runnable {

	private static Logger log = LoggerFactory.getLogger(TaskRunner.class);

	protected Controller cont;
	protected VcdConf conf;

	public static void starTaskRunnert() {
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
				1);
		TaskRunner runner = InjMgr.create(TaskRunner.class);

		executor.scheduleWithFixedDelay(runner, 60, 60, TimeUnit.SECONDS);

	}

	@Inject
	public TaskRunner(Controller cont, VcdConf conf) {
		super();
		this.cont = cont;
		this.conf = conf;
	}

	@Override
	public void run() {

		String dir = this.conf.TaskRunDir;
		File[] listFiles = new File(dir).listFiles();

		log.info("run TaskFileSize:{}", listFiles.length);

		for (File file : listFiles) {
			String line = null;
			try (BufferedReader bf = new BufferedReader(new FileReader(file))) {

				while ((line = bf.readLine()) != null) {

					log.info("read TaskName:{}", line);
					Class cls = Class.forName(line);

					// 非同期化できるように、タスクはCallableで使っている。

					Object task = InjMgr.create(cls);
					if (task instanceof Callable) {
						log.info("run  TaskName:{}", line);
						((Callable) task).call();
					}
				}

			} catch (IOException e) {
				log.info("GUICEエラー file={},line={}",
						new Object[] { file, line }, e);
			} catch (InstantiationException e) {
				log.info("GUICEエラー file={},line={}",
						new Object[] { file, line }, e);
			} catch (IllegalAccessException e) {
				log.info("GUICEエラー file={},line={}",
						new Object[] { file, line }, e);
			} catch (ClassNotFoundException e) {
				log.info("GUICEエラー file={},line={}",
						new Object[] { file, line }, e);
			} catch (Exception e) {
				log.info("GUICEエラー file={},line={}",
						new Object[] { file, line }, e);
			}

			file.delete();

		}

	}
}
