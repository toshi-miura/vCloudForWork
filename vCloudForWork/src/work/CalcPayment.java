package work;

import mydata.VApp;

import com.vmware.vcloud.sdk.VCloudException;

public interface CalcPayment {
	public int calc(VApp vapp) throws VCloudException;
}
