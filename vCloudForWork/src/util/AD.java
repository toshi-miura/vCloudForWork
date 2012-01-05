package util;

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
import javax.naming.ldap.LdapContext;

public class AD {

	public AD(String ldapADsPath, String domainName) {
		super();
		this.ldapADsPath = ldapADsPath;
		this.domainName = domainName;
	}

	private final String ldapADsPath;
	private final String domainName;

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

	public boolean auth2(String user, String pass) {

		Hashtable<String, String> env = new Hashtable<String, String>();

		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapADsPath);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, user + "@" + domainName);
		env.put(Context.SECURITY_CREDENTIALS, pass);

		env.put(Context.REFERRAL, "follow");

		boolean result;
		try {
			// bind認証する
			DirContext ctx = new InitialDirContext(env);

			Object lookup = ctx.lookup("CN=hoge,CN=Users,DC=vm,DC=local");
			if (lookup instanceof LdapContext) {
				LdapContext l = (LdapContext) lookup;

				System.out.println(l.getAttributes("attr=mail"));

				/*
				Hashtable<?, ?> environment = l.getEnvironment();
				for (Entry<?, ?> e : environment.entrySet()) {

					System.out.println(e.getKey() + " " + e.getValue());
				}
				*/

			}

			System.out.println(lookup);

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

		UserInfo u = new UserInfo(user, pass, attr.get("displayName").get(0),
				attr.get("telephoneNumber").get(0), attr.get("mail").get(0));

		return u;

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

			String baseDn = "CN=Users,DC=vm,DC=local";
			String filter = "sn=" + user;

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

					// System.out.println(attribute.getID() + "   "
					// + attrValueList);
				}

				if (counter > 1) {
					throw new IllegalArgumentException("結果が複数件ありました");
				}
			}

			dirContext.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return entry;

	}
}