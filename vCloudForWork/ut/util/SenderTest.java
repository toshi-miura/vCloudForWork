package util;

import javax.mail.MessagingException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import work.util.Sender;
import all.InjMgr;

public class SenderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSendMail() throws MessagingException {

		try {
			Sender sender = InjMgr.create(Sender.class);
			sender.sendMail("toshihiko.miura+program@gmail.com", "Title",
					"BODY", "toshihiko.miura+program@gmail.com");
		} catch (MessagingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			throw e;
		}

	}

}
