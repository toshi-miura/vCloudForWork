package work;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jp.sf.orangesignal.csv.Csv;
import jp.sf.orangesignal.csv.CsvConfig;
import jp.sf.orangesignal.csv.handlers.ColumnNameMapListHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class ProjCodeCheckerImpl implements ProjCodeChecker {

	private static Logger log = LoggerFactory
			.getLogger(ProjCodeCheckerImpl.class);

	private final String file;

	@Inject
	public ProjCodeCheckerImpl(VcdConf vcdConf) {
		super();
		this.file = vcdConf.ProjCodeCheckerMasterPath;

		try {
			read();
		} catch (IOException e) {
			throw new IllegalStateException("想定外のエラー", e);
		}

	}

	private final Map<String, ProjCodeInfo> map = new HashMap<String, ProjCodeInfo>();

	/* (非 Javadoc)
	 * @see work.IProjCodeChecker#valid(java.lang.String, java.util.Date)
	 */
	@Override
	public boolean valid(String projNo, Date date) {

		ProjCodeInfo projCodeInfo = map.get(projNo);
		if (projCodeInfo != null) {
			return projCodeInfo.isValid(date);
		} else {
			return false;
		}

	}

	public ProjCodeInfo getProjCodeInfo(String no) {
		return map.get(no);

	}

	protected void read() throws IOException {
		CsvConfig cfg = new CsvConfig('\t');

		cfg.setIgnoreEmptyLines(true);

		Pattern p = Pattern.compile("$^");
		cfg.setIgnoreLinePatterns(p);

		// 入力
		List<Map<String, String>> list = Csv.load(new InputStreamReader(
				new FileInputStream(file), "shift-jis"), cfg,
				new ColumnNameMapListHandler());

		for (Map<String, String> projCodeInfo : list) {
			if (ProjCodeInfo.valid(projCodeInfo)) {

				ProjCodeInfo info = new ProjCodeInfo(projCodeInfo);

				ProjCodeInfo info2 = map.get(info);
				if (info2 == null) {
					map.put(info.getProjNo(), info);
				} else {
					if (!info.equals(info2)) {
						log.info(info.toString());
						log.info(info2.toString());
						throw new IllegalStateException("想定外のデータ形式");
					}
				}

			}

		}

	}
}
