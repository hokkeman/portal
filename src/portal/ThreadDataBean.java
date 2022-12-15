package portal;

import java.io.Serializable;

public class ThreadDataBean implements Serializable {
	private String threadId = null;
	private String threadTitle = null;
	private String threadDate = null;
	private String threadTime = null;
	private String groupId = null;
	private String userName= null;

	/**
	 * @return threadId
	 */
	public String getThreadId() {
		return threadId;
	}
	/**
	 * @param threadId セットする threadId
	 */
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
	/**
	 * @return threadTitle
	 */
	public String getThreadTitle() {
		return threadTitle;
	}
	/**
	 * @param threadTitle セットする threadTitle
	 */
	public void setThreadTitle(String threadTitle) {
		this.threadTitle = threadTitle;
	}
	/**
	 * @return threadDate
	 */
	public String getThreadDate() {
		return threadDate;
	}
	/**
	 * @param threadDate セットする threadDate
	 */
	public void setThreadDate(String threadDate) {
		this.threadDate = threadDate;
	}
	/**
	 * @return threadTime
	 */
	public String getThreadTime() {
		return threadTime;
	}
	/**
	 * @param threadTime セットする threadTime
	 */
	public void setThreadTime(String threadTime) {
		this.threadTime = threadTime;
	}
	/**
	 * @return groupId
	 */
	public String getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId セットする groupId
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
