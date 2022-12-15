package portal;

import java.io.Serializable;

public class BoardDataBean implements Serializable {
	private String boardId = null;
	private String threadId = null;
	private String contents = null;
	private String boardDate = null;
	private String boardTime = null;
	private String userName = null;
	private String groupId = null;

	/**
	 * @return boardId
	 */
	public String getBoardId() {
		return boardId;
	}
	/**
	 * @param boardId セットする boardId
	 */
	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
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
	 * @return contents
	 */
	public String getContents() {
		return contents;
	}
	/**
	 * @param contents セットする contents
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}
	/**
	 * @return boardDate
	 */
	public String getBoardDate() {
		return boardDate;
	}
	/**
	 * @param boardDate セットする doardDate
	 */
	public void setBoardDate(String boardDate) {
		this.boardDate = boardDate;
	}
	/**
	 * @return boardTime
	 */
	public String getBoardTime() {
		return boardTime;
	}
	/**
	 * @param boardTime セットする boardTime
	 */
	public void setBoardTime(String boardTime) {
		this.boardTime = boardTime;
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
}
