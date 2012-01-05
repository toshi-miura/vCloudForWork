package util;

public class UserInfo {

	private String id;
	private String pass;
	private String displayName;
	private String telephoneNumber;
	private String mail;

	public UserInfo() {
	}

	public UserInfo(String id, String pass, String displayName,
			String telephoneNumber, String mail) {
		super();
		this.id = id;
		this.pass = pass;
		this.displayName = displayName;
		this.telephoneNumber = telephoneNumber;
		this.mail = mail;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", pass=" + pass + ", displayName="
				+ displayName + ", telephoneNumber=" + telephoneNumber
				+ ", mail=" + mail + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

}
