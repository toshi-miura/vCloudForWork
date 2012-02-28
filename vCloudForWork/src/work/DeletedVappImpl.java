package work;

import java.util.List;

import org.seasar.doma.jdbc.tx.LocalTransaction;

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

import com.vmware.vcloud.sdk.VCloudException;

public class DeletedVappImpl implements DeletedVappHandler {

	@Override
	public void handle(List<VApp> vappList) throws VCloudException {
		LocalTransaction tx = AppConfig.getLocalTransaction();
		try {
			// トランザクションの開始
			tx.begin();

			DeletedVappDao vApppDao = new DeletedVappDaoImpl();
			VcdUserDao userdao = new VcdUserDaoImpl();

			for (VApp vApp : vappList) {

				List<User> allUsers = vApp.getAllUsers();

				for (User user : allUsers) {
					VcdUser vcdUser = new VcdUser();

					vcdUser.userId = user.getId();
					vcdUser.vappID = vApp.getID();

					userdao.insert(vcdUser);

				}

				DeletedVapp dellVapp = new DeletedVapp();

				dellVapp.vappID = vApp.getID();
				dellVapp.vappName = vApp.getName();
				dellVapp.cpu = vApp.getCpu();
				dellVapp.memorySizeMB = vApp.getMemorySizeMB();
				dellVapp.totalHDDGB = vApp.getTotalHDDGB();
				dellVapp.auther1 = "";
				dellVapp.auther2 = "";
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

}
