package work;


import base.mydata.VApp;

import com.vmware.vcloud.sdk.VCloudException;

public interface CalcPayment {
	public int calc(VApp vapp) throws VCloudException;
}
