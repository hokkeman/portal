package portal;

import java.io.Serializable;

public class ScheduleBean implements Serializable {
	private String sid = null;
	private String stitle = null;
	private String sdate = null;
	private String stime = null;
	private String memo = null;
	private String groupid = null;
	private String userid = null;
	private String username = null;

	/**
	 * @return sid
	 */
	public String getSid() {
		return sid;
	}
	/**
	 * @param sid セットする sid
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}
	/**
	 * @return stitle
	 */
	public String getStitle() {
		return stitle;
	}
	/**
	 * @param stitle セットする stitle
	 */
	public void setStitle(String stitle) {
		this.stitle = stitle;
	}
	/**
	 * @return sdate
	 */
	public String getSdate() {
		return sdate;
	}
	/**
	 * @param sdate セットする sdate
	 */
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	/**
	 * @return stime
	 */
	public String getStime() {
		return stime;
	}
	/**
	 * @param stime セットする stime
	 */
	public void setStime(String stime) {
		this.stime = stime;
	}
	/**
	 * @return memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo セットする memo
	 */
	public void setMemo(String memo) {
		this.memo = memo;
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

}
