package work.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Sender {
	private static Logger log = LoggerFactory.getLogger(Sender.class);

	private static final String USER = "USER";
	private static final String PASS = "PASS";

	private String charset;
	private Session session;
	private Properties authProp;

	static {
		System.setProperty("mail.debug", "true");
	}

	// this("mb3.dentsu.co.jp", "iso-2022-jp");

	public Sender() {

		try (InputStream inStream = Sender.class.getClassLoader()
				.getResourceAsStream("mail.properties")) {

			final Properties prop = new Properties();

			prop.load(inStream);
			this.session = Session.getDefaultInstance(prop, null);
		} catch (IOException e) {
			log.error("", e);

		}

		try (InputStream inStream = Sender.class.getClassLoader()
				.getResourceAsStream("mailAuth.properties")) {

			authProp = new Properties();
			authProp.load(inStream);

			log.info("{}:{}", authProp.getProperty(USER),
					authProp.getProperty(PASS));

		} catch (IOException e) {
			log.error("", e);
		}

	}

	/**
	 *
	 * @param userId 現在使用せず
	 * @param passwd 現在使用せず
	 * @param mail
	 * @param sub
	 * @param body
	 * @throws MessagingException
	 */
	public void sendMail(String mail, String sub, String body, String from)
			throws MessagingException {

		sendMail(Lists.newArrayList(mail), sub, body, from);

	}

	/**
	 *
	 * @param userId 現在使用せず
	 * @param passwd 現在使用せず
	 * @param mail
	 * @param sub
	 * @param body
	 * @throws MessagingException
	 */
	public void sendMail(Collection<String> mailListTo, String sub,
			String body, String from) throws MessagingException {
		MimeMessage msg = new MimeMessage(session);

		for (String m : mailListTo) {
			try {
				if (!m.equals("")) {
					msg.setRecipient(Message.RecipientType.TO,
							new InternetAddress(m));
				}
			} catch (MessagingException e) {
				log.error("illegal address:{}", m);
				throw e;
			}
		}

		msg.setFrom(new InternetAddress(from));
		msg.setSentDate(new Date());

		msg.setSubject(sub, charset);
		msg.setText(body, charset);

		Transport transport = null;
		try {

			transport = session.getTransport("smtp");
			transport.connect(authProp.getProperty(USER),
					authProp.getProperty(PASS));
			transport.sendMessage(msg, msg.getAllRecipients());
		} finally {
			if (transport != null) {
				transport.close();
			}
		}

	}
}
