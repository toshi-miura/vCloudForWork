package work;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ProjCodeInfo {

	private static DateFormat DF = new SimpleDateFormat("yyyy/MM/dd");

	public static boolean valid(Map<String, String> map) {

		return map.get("プロジェクト名") != null

		&& map.get("プロジェクトNo") != null && map.get("プロジェクト期間開始日") != null
				&& map.get("プロジェクト期間終了日") != null;
	}

	public ProjCodeInfo(Map<String, String> map) {
		super();
		this.projName = map.get("プロジェクト名");
		this.projNo = map.get("プロジェクトNo");
		try {
			this.startDate = DF.parse(map.get("プロジェクト期間開始日"));
			this.endDate = DF.parse(map.get("プロジェクト期間終了日"));
		} catch (ParseException e) {

			throw new IllegalArgumentException("ファイルの書式が不正と思われます", e);
		}
	}

	private final String projName;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result
				+ ((projName == null) ? 0 : projName.hashCode());
		result = prime * result + ((projNo == null) ? 0 : projNo.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}

	public boolean isValid(Date now) {

		if (startDate.getTime() < now.getTime()
		// 日付が、０時になっていると思われるため、
		// 次の日まで１日ずらす
				&& (now.getTime() < (endDate.getTime() + 1000l * 60 * 24))) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjCodeInfo other = (ProjCodeInfo) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (projName == null) {
			if (other.projName != null)
				return false;
		} else if (!projName.equals(other.projName))
			return false;
		if (projNo == null) {
			if (other.projNo != null)
				return false;
		} else if (!projNo.equals(other.projNo))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

	private final String projNo;
	private Date startDate;
	private Date endDate;

	@Override
	public String toString() {
		return "ProjCodeInfo [projName=" + projName + ", projNo=" + projNo
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

	public String getProjName() {
		return projName;
	}

	public String getProjNo() {
		return projNo;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

}
