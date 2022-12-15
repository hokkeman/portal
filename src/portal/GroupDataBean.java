package portal;

import java.io.Serializable;

public class GroupDataBean implements Serializable {
	private String groupid = null;
	private String invitationcode = null;
	private String groupName = null;
	private String permit = null;
	private String userid = null;
	private String username = null;

	/**
	 * @return invitationcode
	 */
	public String getInvitationcode() {
		return invitationcode;
	}

	/**
	 * @param invitationcode セットする invitationcode
	 */
	public void setInvitationcode(String invitationcode) {
		this.invitationcode = invitationcode;
	}

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
	 * @return groupid
	 */
	public String getGroupid() {
		return groupid;
	}

	/**
	 * @param groupid セットする groupid
	 */
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	/**
	 * @return groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName セットする groupName
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return permit
	 */
	public String getPermit() {
		return permit;
	}

	/**
	 * @param permit セットする permit
	 */
	public void setPermit(String permit) {
		this.permit = permit;
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
}
