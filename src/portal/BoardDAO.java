package portal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	/**
	 * グループ内の全てのスレッドデータを取得するメソッド
	 * @param groupData
	 * 取得したいグループの情報
	 * @return
	 * 取得したスレッドデータ（ThreadDataBeanクラス型）を格納したArrayListを返す
	 */
	public static ArrayList<ThreadDataBean> allThreadGet(GroupDataBean groupData){
		// 戻り値用のArrayListを用意
		ArrayList<ThreadDataBean> allThreadList = new ArrayList<ThreadDataBean>();

		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("SELECT * FROM thread where groupid=? group by threadid desc");
			ps.setString(1, groupData.getGroupid());
			rs = ps.executeQuery();
			while (rs.next()) {
				ThreadDataBean threadData = new ThreadDataBean();
				threadData.setThreadId(rs.getString("threadid"));
				threadData.setThreadTitle(rs.getString("threadtitle"));
				threadData.setThreadDate(rs.getString("threaddate"));
				threadData.setThreadTime(rs.getString("threadtime"));
				threadData.setGroupId(rs.getString("groupid"));
				threadData.setUserName(rs.getString("username"));
				allThreadList.add(threadData);
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return allThreadList;
	}

	/**
	 * 掲示板の書き込み情報を全て取得するメソッド
	 * @param threadId
	 * 全ての書き込み情報を取得したい掲示板のID
	 * @return
	 * 取得した書き込みの情報（BoardDataBeanクラス型）を格納したArrayListを返す
	 */
	public static ArrayList<BoardDataBean> allBoardGet(String threadId){
		// 戻り値用のArrayListを用意
		ArrayList<BoardDataBean> boardDataList = new ArrayList<BoardDataBean>();

		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("SELECT * FROM board where threadid=?");
			ps.setString(1, threadId);
			rs = ps.executeQuery();
			while (rs.next()) {
				BoardDataBean boardData = new BoardDataBean();
				boardData.setBoardId(rs.getString("boardid"));
				boardData.setThreadId(rs.getString("threadid"));
				boardData.setContents(rs.getString("contents"));
				boardData.setBoardDate(rs.getString("boarddate"));
				boardData.setBoardTime(rs.getString("boardtime"));
				boardData.setUserName(rs.getString("username"));
				boardData.setGroupId(rs.getString("groupid"));
				boardDataList.add(boardData);
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return boardDataList;
	}

	/**
	 * 新規スレッドを作成するメソッド
	 * @param threadData
	 * 作成する新規スレッドの情報
	 * @param contents
	 * 作成する新規スレッドの1件目の書き込み情報
	 * @return
	 * 新規スレッドの作成が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean createThread(ThreadDataBean threadData , String contents) {
		ResultSet rs = null;
	    PreparedStatement psCreateThread = null;
	    PreparedStatement psTakeThreadId = null;
	    PreparedStatement psCreateBoard = null;
	    Connection db = null;
	    boolean createOK = false;

	    try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);
			// Threadテーブルに新規スレッド情報を追加
			psCreateThread = db.prepareStatement("insert into thread(threadtitle, threaddate, threadtime, groupid, username) values (?,?,?,?,?)");
			psCreateThread.setString(1, threadData.getThreadTitle());
			psCreateThread.setString(2, threadData.getThreadDate());
			psCreateThread.setString(3, threadData.getThreadTime());
			psCreateThread.setString(4, threadData.getGroupId());
			psCreateThread.setString(5, threadData.getUserName());
			psCreateThread.executeUpdate();

			// 作成した新規スレッドのThreadIdを取得
			String threadId = null;
			psTakeThreadId = db.prepareStatement("select * from thread where threadtitle=? and threaddate=? and threadtime=? and username=?");
			psTakeThreadId.setString(1, threadData.getThreadTitle());
			psTakeThreadId.setString(2, threadData.getThreadDate());
			psTakeThreadId.setString(3, threadData.getThreadTime());
			psTakeThreadId.setString(4, threadData.getUserName());
			rs = psTakeThreadId.executeQuery();
			while (rs.next()) {
				threadId = rs.getString("threadid");
			}

			// boardテーブルに1件目の書き込み情報を追加する
			psCreateBoard = db.prepareStatement("insert into board(threadid, contents, boarddate, boardtime, username, groupid) values (?,?,?,?,?,?)");
			psCreateBoard.setString(1, threadId);
			psCreateBoard.setString(2, contents);
			psCreateBoard.setString(3, threadData.getThreadDate());
			psCreateBoard.setString(4, threadData.getThreadTime());
			psCreateBoard.setString(5, threadData.getUserName());
			psCreateBoard.setString(6, threadData.getGroupId());
			psCreateBoard.executeUpdate();

			db.commit(); // 変更を適用
			createOK = true;
	    } catch (Exception e) {
	    	try { // 例外発生時変更を取り消し
				db.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return createOK;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(psCreateThread != null) {psCreateThread.close();}
				if(psTakeThreadId != null) {psTakeThreadId.close();}
				if(psCreateBoard != null) {psCreateBoard.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return createOK;
	}

	/**
	 * 新規書き込みを投稿するメソッド
	 * @param boardData
	 * 新規書き込みを投稿するスレッドの情報
	 * @return
	 * 新規書き込みの投稿が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean boardPost(BoardDataBean boardData) {
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    boolean postOK = false;

	    try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			// Boardテーブルに新規書き込みを追加
			ps = db.prepareStatement("insert into board(threadid, contents, boarddate, boardtime, username, groupid) values (?,?,?,?,?,?)");
			ps.setString(1, boardData.getThreadId());
			ps.setString(2, boardData.getContents());
			ps.setString(3, boardData.getBoardDate());
			ps.setString(4, boardData.getBoardTime());
			ps.setString(5, boardData.getUserName());
			ps.setString(6, boardData.getGroupId());
			ps.executeUpdate();
			db.commit(); // 変更を適用
			postOK = true;
	    } catch (Exception e) {
	    	try { // 例外発生時変更を取り消し
				db.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return postOK;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return postOK;
	}

	/**
	 * スレッドを削除するメソッド
	 * @param threadId
	 * 削除したいスレッドのスレッドID
	 * @return
	 * スレッドの削除が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean deleteThread(String threadId) {
		ResultSet rs = null;
	    PreparedStatement psThread = null;
	    PreparedStatement psBoard = null;
	    Connection db = null;
	    boolean deleteOK = false;

	    try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			// データベースからスレッド情報と書き込み内容を削除
			psThread = db.prepareStatement("delete from thread where threadid=?");
			psThread.setString(1, threadId);
			psBoard = db.prepareStatement("delete from board where threadid=?");
			psBoard.setString(1, threadId);

			psThread.executeUpdate();
			psBoard.executeUpdate();
			db.commit(); // 変更を適用
			deleteOK = true;
	    } catch (Exception e) {
	    	try { // 例外発生時変更を取り消し
				db.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return deleteOK;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(psThread != null) {psThread.close();}
				if(psBoard != null) {psBoard.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return deleteOK;
	}
}
