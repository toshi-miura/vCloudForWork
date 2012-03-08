package work;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.mydata.User;
import base.mydata.VApp;
import base.mydata.VM;

import com.google.common.base.Strings;
import com.vmware.vcloud.api.rest.schema.ovf.StartupSectionItem;
import com.vmware.vcloud.api.rest.schema.ovf.StartupSectionType;
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
 * 追加項目は、vCloudのvAppのメタデータとして定義
 *
 *
 *
 * @author user
 *
 */
public class VApp4Work extends VApp {

	private static Logger log = LoggerFactory.getLogger(VApp4Work.class);

	public enum AUTH_STATUS {
		BEFORE_INPUT_PNO("Pno未入力"), DONOT_HAVA_AUTHER("承認者未入力"), BEFORE_AUTH(
				"未承認"), AUTH("承認済み"), NEED_UPDATE("要再承認");

		private String viewName;

		AUTH_STATUS(String name) {
			this.viewName = name;

		}

		public String getViewName() {
			return viewName;
		}

		/**
		 * ステータスを二つ参照し、総合したステータスを返す。
		 * @param otherStatus
		 * @return
		 */
		public AUTH_STATUS isStatus(AUTH_STATUS otherStatus) {

			if (this != DONOT_HAVA_AUTHER && otherStatus != DONOT_HAVA_AUTHER) {
				if (this.ordinal() < otherStatus.ordinal()) {
					return this;
				} else {
					return otherStatus;
				}
			} else {

				if (this != DONOT_HAVA_AUTHER) {
					return otherStatus;
				} else {
					return this;
				}

			}

		}

	};

	private static final String AUTHOR1 = "AUTHOR1";
	private static final String AUTHOR2 = "AUTHOR2";
	private static final String AUTH_STATUS1 = "AUTH_STATUS";
	private static final String AUTH_STATUS2 = "AUTH_STATUS";
	private static final String P_NO = "P_NO";
	private static final String START_DATE = "START_DATE";
	private static final String OLD_VAPP_INFO = "OLD_VAPP_INFO";
	/**
	 * 更新日時
	 */
	private static final String VAPP_UPDATE_DATE_STR = "VAPP_UPDATE_DATE_STR";
	/**
	 * MAXコスト
	 */
	private static final String MAX_COST = "MAX_COST";

	/**
	 * MAXコスト時の構成情報
	 */
	private static final String MAX_COST_INFO_CPU = "MAX_COST_INFO_CPU";
	/**
	 *  MAXコスト時の構成情報
	 */
	private static final String MAX_COST_INFO_MEM = "MAX_COST_INFO_MEM";
	/**
	 *  MAXコスト時の構成情報
	 */
	private static final String MAX_COST_HDD = "MAX_COST_HDD";
	/**
	 *  MAXコスト時の構成情報
	 */
	private static final String MAX_COST_DATE = "MAX_COST_DATE";

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
		{
			String pno = getpNo();

			if (Strings.isNullOrEmpty(pno)) {

				log.info("{}の状態の初期化をします。（PNOが空）", getName());

				setAuthStatus1(AUTH_STATUS.BEFORE_INPUT_PNO);
				setAuthStatus2(AUTH_STATUS.BEFORE_INPUT_PNO);

			} else {

				String auther1 = getAuthor1();
				if (Strings.isNullOrEmpty(auther1)) {
					log.info("{}の状態の承認者１未承認にします。（PNOが空）", getName());
					setAuthStatus1(AUTH_STATUS.DONOT_HAVA_AUTHER);
				}
				String auther2 = getAuthor2();
				if (Strings.isNullOrEmpty(auther2)) {

					log.info("{}の状態の承認者２未承認にします。（PNOが空）", getName());
					setAuthStatus2(AUTH_STATUS.DONOT_HAVA_AUTHER);
				}
			}
		}

		// metadataを最後に更新。
		metadataUpdate();

		setStartUpSection();
	}

	/**
	 * スタートアップセクションをゲスト終了にする。
	 * 電源強制終了対策。
	 * @throws VCloudException
	 */
	public void setStartUpSection() throws VCloudException {

		try {
			Vapp vcdVapp = vapp.getVcdVapp();
			StartupSectionType startUpSection = vcdVapp.getStartUpSection();

			List<StartupSectionItem> item = startUpSection.getItem();

			boolean update = false;
			for (StartupSectionItem startupSectionItem : item) {

				// 設定文字列は下記。
				// powerOff
				// guestShutdown

				// TODO WINDOWSはとか入れたほうがいいかも。Vmware TOOLSが入っていない場合の考慮
				//
				if (!startupSectionItem.getStopAction().equals("guestShutdown")) {
					log.info("startupMode  {}=>guestShutdown ",
							startupSectionItem.getStopAction());
					startupSectionItem.setStopAction("guestShutdown");
					update = true;
				}

			}
			if (update) {
				vcdVapp.updateSection(startUpSection);
			}
		} catch (VCloudException e) {
			log.warn("スタートアップセクションの書き換えに失敗しました。{}", getName());

		}

	}

	@Override
	public Vapp getVcdVapp() {

		return vapp.getVcdVapp();
	}

	@Override
	public String getVcdName() {

		return vapp.getVcdName();
	}

	/**
	 * 更新対象がない場合は更新しない
	 * @see base.mydata.VApp#metadataUpdate()
	 */
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
	/**
	 * 権限を持っている人。オーナーは含まない。
	 */
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
			return vapp.toBaseString() + "\t" + "Author:	" + getAuthor1()
					+ "\t" + "pNo:	" + getpNo() + "\t" + "StartDate:	"
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

	public int getVmNum() {
		return getVmMap().size();
	}

	@Override
	public boolean equals(Object obj) {
		return vapp.equals(obj);
	}

	@Override
	public int hashCode() {
		return vapp.hashCode();
	}

	public String getAuthor1() throws VCloudException {

		return vapp.getMetadataStr(AUTHOR1);
	}

	public void setAuthor1(String author) throws VCloudException {

		if (!author.equals(getAuthor1())) {
			setAuthStatus1(AUTH_STATUS.BEFORE_AUTH);
			vapp.setMetadataStr(VApp4Work.AUTHOR1, author);
		}
	}

	public String getAuthor2() throws VCloudException {

		return vapp.getMetadataStr(AUTHOR2);
	}

	public void setAuthor2(String author) throws VCloudException {
		if (!author.equals(getAuthor2())) {
			setAuthStatus2(AUTH_STATUS.BEFORE_AUTH);
			vapp.setMetadataStr(VApp4Work.AUTHOR2, author);
		}
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
	 * 合わせてその時の構成情報も設定します。
	 * @return
	 * @throws VCloudException
	 */
	public void setMaxCost() throws VCloudException {

		int maxCost = getMaxCost();
		int costPerMonth = costPerMonth();
		if (maxCost < costPerMonth) {

			log.info("課金額を更新します。{} - \\ {} >> \\ {}", new Object[] { getName(),
					maxCost, costPerMonth

			});

			setMaxCost(costPerMonth);

			setMaxCostCpu(getCpu());
			setMaxCostMem(getMemorySizeMB());
			setMaxCostHDD(getTotalHDDGB());
			setMaxCostDate(toStr(new Date()));
		}

	}

	public String getMaxCostInfoStr(String separator) throws VCloudException {

		StringBuilder builder = new StringBuilder();

		builder.append(getName()).append(separator);
		builder.append(getMaxCost()).append(separator);
		builder.append(getMaxCostCpu()).append(separator);
		builder.append(getMaxCostMem()).append(separator);
		builder.append(getMaxCostHDD()).append(separator);
		builder.append(getMaxCostDate()).append(separator);

		return builder.toString();

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

	public int getMaxCostCpu() throws VCloudException {
		return vapp.getMetadataInt(MAX_COST_INFO_CPU);

	}

	private void setMaxCostCpu(int pNo) throws VCloudException {
		vapp.setMetadataInt(VApp4Work.MAX_COST_INFO_CPU, pNo);
	}

	public int getMaxCostMem() throws VCloudException {
		return vapp.getMetadataInt(MAX_COST_INFO_MEM);

	}

	private void setMaxCostMem(int pNo) throws VCloudException {
		vapp.setMetadataInt(VApp4Work.MAX_COST_INFO_MEM, pNo);
	}

	public int getMaxCostHDD() throws VCloudException {
		return vapp.getMetadataInt(MAX_COST_HDD);

	}

	private void setMaxCostHDD(int pNo) throws VCloudException {
		vapp.setMetadataInt(VApp4Work.MAX_COST_HDD, pNo);
	}

	public String getMaxCostDate() throws VCloudException {
		return vapp.getMetadataStr(MAX_COST_DATE);

	}

	private void setMaxCostDate(String pNo) throws VCloudException {
		vapp.setMetadataStr(VApp4Work.MAX_COST_DATE, pNo);
	}

	public String getpNo() throws VCloudException {
		return vapp.getMetadataStr(P_NO);

	}

	public void setpNo(String pNo) throws VCloudException {

		if (!(pNo.equals(getpNo()))) {

			if (Strings.isNullOrEmpty(pNo)) {

				if (Strings.isNullOrEmpty(getAuthor1())) {
					setAuthStatus1(AUTH_STATUS.DONOT_HAVA_AUTHER);
				} else {
					setAuthStatus1(AUTH_STATUS.BEFORE_AUTH);
				}

				if (Strings.isNullOrEmpty(getAuthor2())) {
					setAuthStatus2(AUTH_STATUS.DONOT_HAVA_AUTHER);
				} else {
					setAuthStatus2(AUTH_STATUS.BEFORE_AUTH);
				}

				vapp.setMetadataStr(VApp4Work.P_NO, pNo);

			}

		}
	}

	public String getStartDate() throws VCloudException {
		return vapp.getMetadataStr(START_DATE);

	}

	/**
	 *
	 * @return
	 * @throws VCloudException
	 */
	public AUTH_STATUS getAuthStatus() throws VCloudException {
		AUTH_STATUS status1 = getAuthStatus1();
		AUTH_STATUS status2 = getAuthStatus2();

		return status1.isStatus(status2);

	}

	private void setStartDate(String startDate) throws VCloudException {
		vapp.setMetadataStr(START_DATE, startDate);
	}

	public void setAuthStatus1(AUTH_STATUS authStatus) throws VCloudException {
		vapp.setMetadataStr(AUTH_STATUS1, authStatus.name());
	}

	public AUTH_STATUS getAuthStatus1() throws VCloudException {

		String str = vapp.getMetadataStr(AUTH_STATUS1);
		return AUTH_STATUS.valueOf(str);

	}

	public void setAuthStatus2(AUTH_STATUS authStatus) throws VCloudException {

		vapp.setMetadataStr(AUTH_STATUS2, authStatus.name());
	}

	public AUTH_STATUS getAuthStatus2() throws VCloudException {
		String str = vapp.getMetadataStr(AUTH_STATUS2);
		return AUTH_STATUS.valueOf(str);

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

	@Override
	public String getMetadataStr(String k) throws VCloudException {
		return vapp.getMetadataStr(k);
	}

	@Override
	public int getMetadataInt(String k) throws VCloudException {
		return vapp.getMetadataInt(k);
	}

	@Override
	public void setMetadataStr(String k, String val) throws VCloudException {
		vapp.setMetadataStr(k, val);
	}

	@Override
	public String getID() throws VCloudException {
		return vapp.getID();
	}

	@Override
	public void setMetadataInt(String k, int val) throws VCloudException {
		vapp.setMetadataInt(k, val);
	}

	@Override
	public List<User> getAllUsers() {
		return vapp.getAllUsers();
	}

	@Override
	public List<String> getAllMailAddress() {
		return vapp.getAllMailAddress();
	}

	@Override
	public String toBaseSimpleInfo() {
		return vapp.toBaseSimpleInfo();
	}

}
