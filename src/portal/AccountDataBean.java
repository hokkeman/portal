package portal;

import java.io.Serializable;

public class AccountDataBean implements Serializable {
	private String userid = null;
	private String username = null;
	private String userpass = null;
	private String salt = null;

	/**
	 * @return userid
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * @param userid セットする userid
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username セットする username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return userpass
	 */
	public String getUserpass() {
		return userpass;
	}
	/**
	 * @param userpass セットする userpass
	 */
	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
	/**
	 * @return salt
	 */
	public String getSalt() {
		return salt;
	}
	/**
	 * @param salt セットする salt
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

}
