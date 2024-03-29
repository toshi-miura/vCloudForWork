package work.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

/**
 * AD検索用のユーティル。
 * 社員番号からの「名前」「メールアドレス」「電話番号」
 * 等の検索に使用
 *
 * @author user
 *
 */
public class AD {

	private static Logger log = LoggerFactory.getLogger(AD.class);

	private final String ldapADsPath;
	private final String domainName;
	private final String baseDn;
	private final String query;

	public AD(String ldapADsPath, String domainName, String baseDN, String query) {
		super();
		this.ldapADsPath = ldapADsPath;
		this.domainName = domainName;
		this.baseDn = baseDN;
		this.query = query;

	}

	public boolean auth(String user, String pass) {

		Hashtable<String, String> env = new Hashtable<String, String>();

		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapADsPath);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, user + "@" + domainName);
		env.put(Context.SECURITY_CREDENTIALS, pass);

		boolean result;
		try {
			// bind認証する
			DirContext ctx = new InitialDirContext(env);
			ctx.close();
			result = true;

		} catch (AuthenticationException ae) {
			result = false;

		} catch (Exception e) {

			throw new IllegalStateException("想定外のエラー", e);
		}
		return result;
	}

	/**
	 *
	 *
	 * @param user
	 * @param pass
	 * @return
	 */
	public String getMail(String user, String pass) {
		Map<String, List<String>> attr = getAttr(user, pass);

		return attr.get("mail").get(0);

	}

	/**
	 *
	 *
	 * @param user
	 * @param pass
	 * @return
	 */
	public String getDisplayName(String user, String pass) {
		Map<String, List<String>> attr = getAttr(user, pass);

		return attr.get("displayName").get(0);

	}

	public String getTelephoneNumber(String user, String pass) {
		Map<String, List<String>> attr = getAttr(user, pass);

		return attr.get("telephoneNumber").get(0);

	}

	public UserInfo getUserInfo(String user, String pass) {

		Map<String, List<String>> attr = getAttr(user, pass);

		if (log.isDebugEnabled()) {
			log.debug(Joiner.on("\n").join(attr.values()));
		}

		String name = "";
		String s = head(attr.get("displayName"));
		if (s != null && !s.equals("")) {
			name = s;
		}
		String s2 = head(attr.get("sn"));
		if (s2 != null && !s2.equals("")) {
			name = s2;
		}

		UserInfo u = new UserInfo(user, pass, name, attr.get("telephoneNumber")
				.get(0), attr.get("mail").get(0));

		return u;

	}

	private String head(List<String> attr) {
		if (attr != null && attr.size() > 0) {
			return attr.get(0);
		} else {
			return null;
		}

	}

	/**
	 *
	 *
	 *
	 * @param user
	 * @param pass
	 * @return
	 */
	private Map<String, List<String>> getAttr(String user, String pass) {

		Map<String, List<String>> entry = new HashMap<String, List<String>>();
		try {

			Hashtable<String, String> env = new Hashtable<String, String>();

			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, ldapADsPath);
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, user + "@" + domainName);
			env.put(Context.SECURITY_CREDENTIALS, pass);

			env.put(Context.REFERRAL, "follow");

			InitialDirContext dirContext = new InitialDirContext(env);

			// String baseDn = "CN=Users,DC=vm,DC=local";

			// 環境によって異なる
			String filter = query + "=" + user;

			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			NamingEnumeration<SearchResult> searchResultEnum = dirContext
					.search(baseDn, filter, searchControls);

			int counter = 0;

			while (searchResultEnum.hasMore()) {

				counter++;
				SearchResult searchResult = searchResultEnum.next();
				NamingEnumeration namingEnumeration = searchResult
						.getAttributes().getAll();

				while (namingEnumeration.hasMore()) {
					Attribute attribute = (javax.naming.directory.Attribute) namingEnumeration
							.nextElement();

					NamingEnumeration attrValEnum = attribute.getAll();

					java.util.List<String> attrValueList = new ArrayList<String>();
					while (attrValEnum.hasMoreElements()) {
						attrValueList.add((String) attrValEnum.nextElement());
					}

					entry.put(attribute.getID(), attrValueList);

					// log.info(attribute.getID() + "   "
					// + attrValueList);
				}

				if (counter > 1) {
					throw new IllegalArgumentException("結果が複数件ありました");
				}
			}

			dirContext.close();

		} catch (Exception e) {
			throw new IllegalArgumentException("認証エラーと思われます。", e);
		}

		return entry;

	}
}
