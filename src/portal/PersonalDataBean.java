package portal;

import java.io.Serializable;

public class PersonalDataBean implements Serializable {
	private String tid = null;
	private String userid = null;
	private String temperature = null;
	private String tdate = null;
	private String ttime = null;
	private String userName = null;

	/**
	 * @return tid
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid セットする tid
	 */
	public void setTid(String tid) {
		this.tid = tid;
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
	 * @return temperature
	 */
	public String getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature セットする temperature
	 */
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return tdate
	 */
	public String getTdate() {
		return tdate;
	}

	/**
	 * @param tdate セットする tdate
	 */
	public void setTdate(String tdate) {
		this.tdate = tdate;
	}

	/**
	 * @return ttime
	 */
	public String getTtime() {
		return ttime;
	}

	/**
	 * @param ttime セットする ttime
	 */
	public void setTtime(String ttime) {
		this.ttime = ttime;
	}

	/**
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName セットする userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
