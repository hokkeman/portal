package portal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ScheduleDAO {
	/**
	 * データベースへスケジュールを追加するメソッド
	 * @param schedule
	 * 追加するスケジュールの情報
	 * @return
	 * 追加が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean insertSchedule(ScheduleBean schedule){
		Connection db = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean insertOK = false;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			ps = db.prepareStatement("INSERT INTO schedule(stitle, sdate, stime, memo, groupid, userid) VALUES(?,?,?,?,?,?)");
			ps.setString(1, schedule.getStitle());
			ps.setString(2, schedule.getSdate());
			ps.setString(3, schedule.getStime());
			ps.setString(4, schedule.getMemo());
			ps.setString(5, schedule.getGroupid());
			ps.setString(6, schedule.getUserid());
			ps.executeUpdate();

			db.commit(); // 変更を適用
			insertOK = true;
		} catch(Exception e) {
			try {
				db.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return insertOK;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {
			}
		}
		return insertOK;
	}

	/**
	 * データベースからスケジュールの内容を変更するメソッド
	 * @param schedule
	 * 変更内容
	 * @return
	 * スケジュールの変更が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean changeSchedule(ScheduleBean schedule) {
		Connection db = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean changeOK = false;
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			ps = db.prepareStatement("UPDATE schedule SET stitle=? , sdate=? , stime=? , memo=? WHERE sid=?");
			ps.setString(1, schedule.getStitle());
			ps.setString(2, schedule.getSdate());
			ps.setString(3, schedule.getStime());
			ps.setString(4, schedule.getMemo());
			ps.setString(5, schedule.getSid());
			ps.executeUpdate();

			db.commit(); // 変更を適用
			changeOK = true;
		} catch(Exception e) {
			try {
				db.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return changeOK;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {
			}
		}
		return changeOK;
	}

	/**
	 * データベースからスケジュールを削除するメソッド
	 * @param sid
	 * 削除対象のスケジュールのID
	 * @return
	 * 削除が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean deleteSchedule(String sid) {
		Connection db = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean deleteOK = false;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			ps = db.prepareStatement("DELETE FROM schedule WHERE sid=?");
			ps.setString(1, sid);
			ps.executeUpdate();
			db.commit(); // 変更を適用
			deleteOK = true;
		} catch(Exception e) {
			try {
				db.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return deleteOK;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return deleteOK;
	}

	/**
	 * データベースから特定の月のスケジュールを取得するメソッド
	 * @param groupId
	 * 取得したいスケジュールのグループID
	 * @param date
	 * 取得したいスケジュールの日付
	 * @return
	 * 取得したスケジュール（ScheduleBeanクラス型）を格納したArrayListを返す
	 */
	public static ArrayList<ScheduleBean> thisMonthSchedule(String groupId, String date) {
		ArrayList<ScheduleBean> thisMonthSchedule = new ArrayList<ScheduleBean>();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			// 今月のスケジュールを取得
			ps = db.prepareStatement("select * from schedule,user where groupid=? and LAST_DAY(?) >= sdate AND DATE_FORMAT(?, '%Y-%m-01') <= sdate and schedule.userid=user.userid group by sdate,stime asc");
			ps.setString(1, groupId);
			ps.setString(2, date);
			ps.setString(3, date);
			rs = ps.executeQuery();
			while (rs.next()) {
				ScheduleBean schedule = new ScheduleBean();
				schedule.setSid(rs.getString("sid"));
				schedule.setStitle(rs.getString("stitle"));
				schedule.setSdate(rs.getString("sdate"));
				schedule.setStime(rs.getString("stime"));
				schedule.setMemo(rs.getString("memo"));
				schedule.setGroupid(rs.getString("groupid"));
				schedule.setUserid(rs.getString("userid"));
				schedule.setUsername(rs.getString("username"));
				thisMonthSchedule.add(schedule);
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
		return thisMonthSchedule;
	}
}
