package work;

import java.util.Date;
import java.util.List;

import org.seasar.doma.jdbc.tx.LocalTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import work.dao.DeletedVapp;
import work.dao.DeletedVappDao;
import work.dao.DeletedVappDaoImpl;
import work.dao.VcdUser;
import work.dao.VcdUserDao;
import work.dao.VcdUserDaoImpl;
import base.dao.AppConfig;
import base.my.DeletedVappHandler;
import base.mydata.User;
import base.mydata.VApp;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.vmware.vcloud.sdk.VCloudException;

public class DeletedVappHandlerImpl implements DeletedVappHandler {

	private static Logger log = LoggerFactory
			.getLogger(DeletedVappHandlerImpl.class);

	private CalcPayment calc;

	@Inject
	public DeletedVappHandlerImpl(CalcPayment calc) {
		this.calc = calc;
	}

	@Override
	public void handle(List<VApp> vappList) throws VCloudException {

		log.info("DeletedVappHandler:init {}", vappList.size());

		List<VApp4Work> list = Lists.transform(vappList,
				new Function<VApp, VApp4Work>() {
					@Override
					public VApp4Work apply(VApp arg0) {
						try {
							return new VApp4Work(arg0, calc);
						} catch (VCloudException e) {
							throw new RuntimeException(e);
						}
					}
				});

		LocalTransaction tx = AppConfig.getLocalTransaction();
		try {
			// トランザクションの開始
			tx.begin();

			DeletedVappDao vApppDao = new DeletedVappDaoImpl();
			VcdUserDao userdao = new VcdUserDaoImpl();

			for (VApp4Work vApp : list) {

				List<User> allUsers = vApp.getAllUsers();

				for (User user : allUsers) {

					VcdUser vcdUser = new VcdUser();

					vcdUser.userId = user.getNameInSource();
					vcdUser.vappID = vApp.getID();

					log.info("{}>{}", user.getId(), vApp.getID());

					userdao.insert(vcdUser);

				}

				DeletedVapp dellVapp = new DeletedVapp();

				log.info("DELVAPP {}>{}", vApp.getName(), vApp.getID());

				dellVapp.vappID = vApp.getID();
				dellVapp.vappName = vApp.getName();
				dellVapp.vmNum = vApp.getVmNum();
				dellVapp.cpu = vApp.getCpu();
				dellVapp.memorySizeMB = vApp.getMemorySizeMB();
				dellVapp.totalHDDGB = vApp.getTotalHDDGB();
				dellVapp.pNo = vApp.getpNo();
				dellVapp.auther1 = vApp.getAuthor1();
				dellVapp.auther2 = vApp.getAuthor2();
				dellVapp.deleteDate = new Date();
				dellVapp.lastMaxCost = 1000;

				vApppDao.insert(dellVapp);

			}
			// aptで生成されたDaoの実装クラスを生成

			tx.commit();
		} finally {
			// トランザクションのロールバック
			tx.rollback();
		}
	}

	public CalcPayment getCalc() {
		return calc;
	}

	public void setCalc(CalcPayment calc) {
		this.calc = calc;
	}
}
