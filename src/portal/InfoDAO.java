package portal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class InfoDAO {
	/**
	 * 特定のグループの全連絡事項取得用DAO
	 * @param groupId
	 * 連絡事項を取得するグループのグループID
	 * @return
	 * 取得した連絡事項（InfoBeanクラス型）を格納したArrayListを返す
	 */
	public static ArrayList<InfoBean> allInfo(String groupId) {
		ArrayList<InfoBean> allInfo = new ArrayList<InfoBean>();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
		SimpleDateFormat dateStFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeStFormat = new SimpleDateFormat("HH:mm:ss");

		//テーブルの表示形式用の形式に変換
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH時mm分");

		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("SELECT * FROM info where groupid=? order by editdate desc,edittime desc");
			ps.setString(1, groupId);
			rs = ps.executeQuery();
			while (rs.next()) {
				InfoBean infoData = new InfoBean();

				// データベースから取得した日時のデータをdate型のオブジェクトに変換
				Date date = dateStFormat.parse(rs.getString("infodate"));
				Date time = timeStFormat.parse(rs.getString("infotime"));

				// データベースから取得したデータのcalenderを作成
				Calendar dateCalendar = Calendar.getInstance();
				Calendar timeCalendar = Calendar.getInstance();
				dateCalendar.setTime(date);
				timeCalendar.setTime(time);

				// データベースから取得した連絡事項の情報をinfoDateにセット
				infoData.setInfoId(rs.getString("infoid"));
				infoData.setInfoTitle(rs.getString("infotitle"));
				infoData.setInfoText(rs.getString("infotext"));
				infoData.setUserId(rs.getString("userid"));
				infoData.setInfoDate(dateFormat.format(dateCalendar.getTime()));
				infoData.setInfoTime(timeFormat.format(timeCalendar.getTime()));
				infoData.setGroupId(rs.getString("groupid"));
				infoData.setUserName(rs.getString("username"));

				// 連絡事項が変更されている場合は変更時刻を取得しinfoDataにセット
				if(rs.getString("edittime") != null){
					Date editDate = dateStFormat.parse(rs.getString("editdate"));
					Date editTime = timeStFormat.parse(rs.getString("edittime"));
					Calendar editDateCalendar = Calendar.getInstance();
					Calendar editTimeCalendar = Calendar.getInstance();
					editDateCalendar.setTime(editDate);
					editTimeCalendar.setTime(editTime);
					infoData.setEditDate(dateFormat.format(editDateCalendar.getTime()));
					infoData.setEditTime(timeFormat.format(editTimeCalendar.getTime()));
				}
				allInfo.add(infoData);
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
		return allInfo;
	}

	/**
	 * 連絡事項投稿用DAO
	 * @param infoPostBean
	 * 投稿する連絡事項の内容
	 * @return
	 * 投稿が成功した件数
	 */
	public static int infoPost(InfoBean infoPostBean) {
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    int postCount = 0;

	    try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("insert into info(infotitle , infotext , userid , infodate , infotime ,editdate, edittime, groupid , username) values (?,?,?,?,?,?,?,?,?)");
			ps.setString(1, infoPostBean.getInfoTitle());
			ps.setString(2, infoPostBean.getInfoText());
			ps.setString(3, infoPostBean.getUserId());
			ps.setString(4, infoPostBean.getInfoDate());
			ps.setString(5, infoPostBean.getInfoTime());
			ps.setString(6, infoPostBean.getInfoDate());
			ps.setString(7, infoPostBean.getInfoTime());
			ps.setString(8, infoPostBean.getGroupId());
			ps.setString(9, infoPostBean.getUserName());
			postCount = ps.executeUpdate();
	    } catch (Exception e) {
			return postCount;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return postCount;
	}

	/**
	 * 連絡事項変更用DAO
	 * @param infoChangeBean
	 * 変更内容
	 * @return
	 * 変更が完了した件数
	 */
	public static int infoChange(InfoBean infoChangeBean) {
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    int changeCount = 0;

	    try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("UPDATE info SET infotitle=?, infotext=?, editdate=?, edittime=? where infoid=?");
			ps.setString(1, infoChangeBean.getInfoTitle());
			ps.setString(2, infoChangeBean.getInfoText());
			ps.setString(3, infoChangeBean.getEditDate());
			ps.setString(4, infoChangeBean.getEditTime());
			ps.setString(5, infoChangeBean.getInfoId());
			changeCount = ps.executeUpdate();
	    } catch (Exception e) {
			return changeCount;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return changeCount;
	}

	/**
	 * 連絡事項削除用DAO
	 * @param infoDeleteBean
	 * 削除する連絡事項の情報
	 * @return
	 * 削除が完了した件数
	 */
	public static int infoDelete(InfoBean infoDeleteBean) {
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    int deleteCount = 0;
	    try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("DELETE FROM info WHERE infoid=?");
			ps.setString(1, infoDeleteBean.getInfoId());
			deleteCount = ps.executeUpdate();
	    } catch (Exception e) {
			return deleteCount;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return deleteCount;
	}

}