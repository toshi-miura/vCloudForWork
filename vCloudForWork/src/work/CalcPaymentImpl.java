package work;


import base.mydata.VApp;

import com.vmware.vcloud.sdk.VCloudException;

public class CalcPaymentImpl implements CalcPayment {

	private final CalcConf conf;

	public CalcPaymentImpl(CalcConf conf) {
		this.conf = conf;

	}

	/**
	 * 一つずつ計算
	 * @param vapp
	 * @return
	 * @throws VCloudException
	 */
	@Override
	public int calc(VApp vapp) throws VCloudException {

		int cpuCore = vapp.getCpu();
		// TODO これ1000でいいのか？1024で切り上げ？
		int memG = vapp.getMemorySizeMB() / 1000;
		// TODO これ1000でいいのか？1024で切り上げ？
		int hddPre10G = vapp.getTotalHDDGB() / 10;

		int result = conf.cpuCostPer1Core * cpuCore + memG
				* conf.memoryCostPer1G + hddPre10G * conf.hddCostPer10G;

		return result;

	}

}
