package work;

import java.util.List;
import java.util.Map;

import mydata.User;
import mydata.VApp;
import mydata.VM;

import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.Vapp;
import com.vmware.vcloud.sdk.VcloudClient;

/**
 * 素直に継承でつくるつもりだったが、INITのコストが重かったため、
 * 断念。
 * ラッパーに方向転換
 *
 * @author user
 *
 */
public class VApp4Work extends VApp {

	private static final String AUTHOR = "AUTHOR";
	private static final String P_NO = "P_NO";
	private static final String START_DATE = "START_DATE";

	protected VApp vapp;

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
					+ getStartDate();
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

	public VApp4Work(VApp app) throws VCloudException {
		// 当初は継承モデルで実装していたが、INITが遅いので委譲モデルに切り替える
		// super(app);
		this.vapp = app;
	}

	@SuppressWarnings("unused")
	private VApp4Work(Vapp vapp, VcloudClient vcloudClient)
			throws VCloudException {
		// super(vapp, vcloudClient);
	}

	public String getAuthor() throws VCloudException {

		return vapp.getMetadataStr(AUTHOR);
	}

	public void setAuthor(String author) throws VCloudException {
		vapp.setMetadataStr(VApp4Work.AUTHOR, author);
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

	public void setStartDate(String startDate) throws VCloudException {
		vapp.setMetadataStr(this.START_DATE, startDate);
	}

}
