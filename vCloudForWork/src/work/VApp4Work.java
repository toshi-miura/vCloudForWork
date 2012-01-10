package work;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mydata.User;
import mydata.VApp;
import mydata.VM;

import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.Vapp;

/**
 * 素直に継承でつくるつもりだったが、INITのコストが重かったため、
 * 断念。
 * ラッパーに方向転換。
 *
 * 定義追加フィールド。
 * - 承認者　　←ユーザ更新
 * - 承認状況  ←アドミンの更新
 * - P番　　　 ←ユーザ更新
 * - 利用開始日
 * - 構成変更日
 * - 構成情報フィールド　←文字列で構成をざっくり管理
 *
 * 追加検討項目
 * - 前回課金額　←いらない・・？
 * - 前回課金日　←いらない・・？
 *
 *
 * 導出項目
 * - 現在予定課金額
 *
 *
 *
 * @author user
 *
 */
public class VApp4Work extends VApp {

	private static final String AUTHOR = "AUTHOR";
	private static final String AUTH_STATUS = "AUTH_STATUS";
	private static final String P_NO = "P_NO";
	private static final String START_DATE = "START_DATE";
	private static final String OLD_VAPP_INFO = "OLD_VAPP_INFO";
	private static final String VAPP_UPDATE_DATE_STR = "VAPP_UPDATE_DATE_STR";
	private static final String MAX_COST = "MAX_COST";

	protected VApp vapp;
	private final CalcPayment calc;

	public VApp4Work(VApp app, CalcPayment calc) throws VCloudException {
		// 当初は継承モデルで実装していたが、INITが遅いので委譲モデルに切り替える
		// super(app);
		this.vapp = app;
		this.calc = calc;

		init();
	}

	/**
	 * VAPPの初期化。
	 *
	 * 構成情報変更管理。
	 *
	 * @throws VCloudException
	 */
	private void init() throws VCloudException {

		// 構成情報管理
		{
			String oldVappInfo = getOldVappInfo();
			String baseSimpleInfo = vapp.toBaseSimpleInfo();

			if (!baseSimpleInfo.equals(oldVappInfo)) {
				setOldVappInfo(baseSimpleInfo);
				setVappUpdateDateStr(toStr(new Date()));

			}
		}
		// 作成日
		{
			String startDate = getStartDate();
			if (startDate == null) {
				setStartDate(toStr(new Date()));
			}
		}

		// metadataを最後に更新。
		metadataUpdate();

	}

	@Override
	public Vapp getVcdVapp() {

		return vapp.getVcdVapp();
	}

	@Override
	public String getVcdName() {

		return vapp.getVcdName();
	}

	@Override
	public void metadataUpdate() throws VCloudException {
		vapp.metadataUpdate();
	}

	@Override
	public String getName() throws VCloudException {
		return vapp.getName();
	}

	@Override
	public Map<String, VM> getVmMap() {
		return vapp.getVmMap();
	}

	@Override
	public User getOwner() {
		return vapp.getOwner();
	}

	@Override
	public List<User> getUsers() {
		return vapp.getUsers();
	}

	@Override
	public int getCpu() throws VCloudException {
		return vapp.getCpu();
	}

	@Override
	public int getMemorySizeMB() throws VCloudException {
		return vapp.getMemorySizeMB();
	}

	@Override
	public int getTotalHDDGB() throws VCloudException {
		return vapp.getTotalHDDGB();
	}

	@Override
	public String toBaseString() {
		try {
			return vapp.toBaseString() + "\t" + "Author:	" + getAuthor() + "\t"
					+ "pNo:	" + getpNo() + "\t" + "StartDate:	"
					+ getStartDate() + "\t" + "costPerMonth:	" + costPerMonth()
					+ "\t" + "updateDate:	" + getVappUpdateDateStr();

		} catch (VCloudException e) {

			e.printStackTrace();
			return e.getMessage();
		}
	}

	@Override
	public String toString() {
		return vapp.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return vapp.equals(obj);
	}

	@Override
	public int hashCode() {
		return vapp.hashCode();
	}

	public String getAuthor() throws VCloudException {

		return vapp.getMetadataStr(AUTHOR);
	}

	public void setAuthor(String author) throws VCloudException {
		vapp.setMetadataStr(VApp4Work.AUTHOR, author);
	}

	/**
	 * その月の最大コストを取得します。
	 * @return
	 * @throws VCloudException
	 */
	public int getMaxCost() throws VCloudException {
		return vapp.getMetadataInt(MAX_COST);

	}

	/**
	 * その月の最大コストを設定します。
	 * @return
	 * @throws VCloudException
	 */
	public void setMaxCost() throws VCloudException {

		int maxCost = getMaxCost();
		int costPerMonth = costPerMonth();
		if (maxCost < costPerMonth) {
			setMaxCost(costPerMonth);
		}

	}

	/**
	 * コストのリセットを実施します。
	 * @return
	 * @throws VCloudException
	 */
	public void resetMaxCost() throws VCloudException {

		setMaxCost(0);
	}

	/**
	 * その月の最大コストを設定します。
	 * @return
	 * @throws VCloudException
	 */
	private void setMaxCost(int cost) throws VCloudException {
		vapp.setMetadataInt(VApp4Work.MAX_COST, cost);
	}

	public String getpNo() throws VCloudException {
		return vapp.getMetadataStr(P_NO);

	}

	public void setpNo(String pNo) throws VCloudException {
		vapp.setMetadataStr(VApp4Work.P_NO, pNo);
	}

	public String getStartDate() throws VCloudException {
		return vapp.getMetadataStr(START_DATE);

	}

	private void setStartDate(String startDate) throws VCloudException {
		vapp.setMetadataStr(START_DATE, startDate);
	}

	public boolean isAuthStatus() throws VCloudException {
		return Boolean.valueOf(getAuthStatus());
	}

	public void setAuthStatus(boolean b) throws VCloudException {
		setAuthStatus(Boolean.toString(b));
	}

	private String getAuthStatus() throws VCloudException {
		return vapp.getMetadataStr(AUTH_STATUS);

	}

	private void setAuthStatus(String authStatus) throws VCloudException {
		vapp.setMetadataStr(AUTH_STATUS, authStatus);
	}

	// ----------------

	public String getOldVappInfo() throws VCloudException {
		return vapp.getMetadataStr(OLD_VAPP_INFO);

	}

	public void setOldVappInfo(String authStatus) throws VCloudException {
		vapp.setMetadataStr(OLD_VAPP_INFO, authStatus);
	}

	public String getVappUpdateDateStr() throws VCloudException {
		return vapp.getMetadataStr(VAPP_UPDATE_DATE_STR);

	}

	public void setVappUpdateDateStr(String authStatus) throws VCloudException {
		vapp.setMetadataStr(VAPP_UPDATE_DATE_STR, authStatus);
	}

	private String toStr(Date date) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return simpleDateFormat.format(date);
	}

	public int costPerMonth() throws VCloudException {
		return calc.calc(this);
	}

}
