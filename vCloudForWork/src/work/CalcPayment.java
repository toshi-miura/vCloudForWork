package work;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import mydata.VApp;

import com.vmware.vcloud.sdk.VCloudException;

public class CalcPayment {

	private final CalcConf conf;

	public CalcPayment(CalcConf conf) {
		this.conf = conf;

	}

	/**
	 * 一つずつ計算
	 * @param vapp
	 * @return
	 * @throws VCloudException
	 */
	public <V extends VApp> int calc(V vapp) throws VCloudException {

		int cpuCore = vapp.getCpu();
		// TODO これ1000でいいのか？1024で切り上げ？
		int memG = vapp.getMemorySizeMB() / 1000;
		// TODO これ1000でいいのか？1024で切り上げ？
		int hddPre10G = vapp.getTotalHDDGB() / 10;

		int result = conf.cpuCostPer1Core * cpuCore + memG
				* conf.memoryCostPer1G + hddPre10G * conf.hddCostPer10G;

		return result;

	}

	/**
	 * 表とか要にまとめて処理するために
	 * @param vapp
	 * @return
	 * @throws VCloudException
	 */
	public <V extends VApp> Set<Entry<V>> calc(Collection<V> vapp)
			throws VCloudException {

		HashSet<Entry<V>> set = new HashSet<Entry<V>>();

		for (VApp app : vapp) {
			set.add(new Entry(app, calc(app)));

		}

		return set;

	}

	public static class Entry<V extends VApp> {

		private final V vapp;
		private final int payment;

		public Entry(V vapp, int payment) {
			super();
			this.vapp = vapp;
			this.payment = payment;
		}

		public V getVapp() {
			return vapp;
		}

		public int getPayment() {
			return payment;
		}

	}

}
