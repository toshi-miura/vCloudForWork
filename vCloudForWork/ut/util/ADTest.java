package util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ADTest {

	@Test
	public void test0() {

		// OK

		String ldapADsPath = ADTestConf.TEST1.ldapADsPath;
		String domainName = ADTestConf.TEST1.domainName;
		String user = ADTestConf.TEST2.user;
		String pass = ADTestConf.TEST2.pass;

		AD ad = new AD(ldapADsPath, domainName, ADTestConf.BASE_DN);

		String mail = ad.getMail(user, pass);
		String display = ad.getDisplayName(user, pass);
		String tel = ad.getTelephoneNumber(user, pass);

		System.out.println(mail + ":" + display + ":" + tel);

	}

	@Test
	public void testUserInfo() {

		// OK

		String ldapADsPath = ADTestConf.TEST1.ldapADsPath;
		String domainName = ADTestConf.TEST1.domainName;
		String user = ADTestConf.TEST2.user;
		String pass = ADTestConf.TEST2.pass;

		AD ad = new AD(ldapADsPath, domainName, ADTestConf.BASE_DN);

		UserInfo userInfo = ad.getUserInfo(user, pass);

		System.out.println(userInfo);

	}

	@Test
	public void test1() {

		// OK

		String ldapADsPath = ADTestConf.TEST1.ldapADsPath;
		String domainName = ADTestConf.TEST1.domainName;
		String user = ADTestConf.TEST1.user;
		String pass = ADTestConf.TEST1.pass;

		AD ad = new AD(ldapADsPath, domainName, ADTestConf.BASE_DN);

		assertTrue(ad.auth(user, pass));
		UserInfo userInfo = ad.getUserInfo(user, pass);
		System.out.println(userInfo);
	}

	@Test
	public void test2() {

		// OK

		String ldapADsPath = ADTestConf.TEST2.ldapADsPath;
		String domainName = ADTestConf.TEST2.domainName;
		String user = ADTestConf.TEST2.user;
		String pass = ADTestConf.TEST2.pass;

		AD ad = new AD(ldapADsPath, domainName, ADTestConf.BASE_DN);

		assertTrue(ad.auth(user, pass));
		UserInfo userInfo = ad.getUserInfo(user, pass);
		System.out.println(userInfo);

	}

	@Test
	public void test5() {

		// OK

		String ldapADsPath = ADTestConf.TEST5.ldapADsPath;
		String domainName = ADTestConf.TEST5.domainName;
		String user = ADTestConf.TEST5.user;
		String pass = ADTestConf.TEST5.pass;

		AD ad = new AD(ldapADsPath, domainName, ADTestConf.BASE_DN);

		assertTrue(ad.auth(user, pass));
		UserInfo userInfo = ad.getUserInfo(user, pass);
		System.out.println(userInfo);
	}

	@Test
	public void test6() {

		// OK

		String ldapADsPath = ADTestConf.TEST6.ldapADsPath;
		String domainName = ADTestConf.TEST6.domainName;
		String user = ADTestConf.TEST6.user;
		String pass = ADTestConf.TEST6.pass;

		AD ad = new AD(ldapADsPath, domainName, ADTestConf.BASE_DN);

		assertTrue(ad.auth(user, pass));
		UserInfo userInfo = ad.getUserInfo(user, pass);
		System.out.println(userInfo);
	}

	@Test
	public void test3() {

		// NG PASSWORD FAIL
		String ldapADsPath = ADTestConf.TEST3.ldapADsPath;
		String domainName = ADTestConf.TEST3.domainName;
		String user = ADTestConf.TEST3.user;
		String pass = ADTestConf.TEST3.pass;

		AD ad = new AD(ldapADsPath, domainName, ADTestConf.BASE_DN);

		assertFalse(ad.auth(user, pass));

	}

	@Test
	public void test4() {

		// NG domain fail
		String ldapADsPath = ADTestConf.TEST4.ldapADsPath;
		String domainName = ADTestConf.TEST4.domainName;
		String user = ADTestConf.TEST4.user;
		String pass = ADTestConf.TEST4.pass;

		AD ad = new AD(ldapADsPath, domainName, ADTestConf.BASE_DN);

		assertFalse(ad.auth(user, pass));

	}

}
