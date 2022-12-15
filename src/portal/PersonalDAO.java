package portal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class PersonalDAO{
	/**
	 * 今日の体温が記録されているかどうか確認するメソッド
	 * @param userId
	 * 確認対象のユーザーのユーザーID
	 * @param tdate
	 * 確認対象となる日付
	 * @return
	 * 記録されているかどうかを示すboolean型変数（true＝記録済み）
	 */
	public static boolean todayPersonalChack(String userId , String tdate) {
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("SELECT * FROM temperaturedata where userid=? and tdate=?");
			ps.setString(1, userId);
			ps.setString(2, tdate);
			rs = ps.executeQuery();
			if (!rs.next()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
		try {
			if(rs != null) {rs.close();}
			if(ps != null) {ps.close();}
			if(db != null) {db.close();}
		} catch(Exception e) {}
		}
		return true;
	}

	/**
	 * ユーザーの過去3週間の体温データ取得するメソッド
	 * @param userId
	 * 対象ユーザーのユーザーID
	 * @return
	 * 取得した体温データ（PersonalDataBeanクラス型）を格納したArrayList
	 */
	public static ArrayList<PersonalDataBean> personalData(String userId) {
		ArrayList<PersonalDataBean> personalDataList = new ArrayList<PersonalDataBean>();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			// 過去3週間の体温データ(日付昇順)を取得
			ps = db.prepareStatement("select * from temperaturedata where tdate > ( now() - interval 22 day ) and userid=? order by tdate asc");
			ps.setString(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				PersonalDataBean personalData = new PersonalDataBean();
				personalData.setTid(rs.getString("tid"));
				personalData.setUserid(rs.getString("userid"));
				personalData.setTemperature(rs.getString("temperature"));
				personalData.setTdate(rs.getString("tdate"));
				personalData.setTtime(rs.getString("ttime"));
				personalDataList.add(personalData);
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
		return personalDataList;
	}

	/**
	 * ログインユーザーの平常時の（37.5℃以下）の体温の平均値を取得するメソッド
	 * @param userId
	 * ログインユーザーのユーザーID
	 * @return
	 * 体温の平均値
	 */
	public static String temperatureAverage (String userId) {
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    String average = null;
	    try {
		    Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			// 平常時（37.5℃以下）の体温の平均値を取得(日付昇順)
			ps = db.prepareStatement("select cast(avg(temperature) as decimal(10,2)) from temperaturedata where userid=? and temperature < 37.5");
			ps.setString(1, userId);
			rs = ps.executeQuery();
			while (rs.next()) {
				average = rs.getString("cast(avg(temperature) as decimal(10,2))");
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
		return average;
	}

	/**
	 * 検温データ登録用メソッド
	 * @param personalData
	 * 登録する検温データ
	 * @return
	 * 検温データの登録が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean insertTemperature (PersonalDataBean personalData){
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("insert into temperaturedata(userid, temperature, tdate, ttime) values (?,?,?,?)");

			ps.setString(1, personalData.getUserid());
			ps.setString(2, personalData.getTemperature());
			ps.setString(3, personalData.getTdate());
			ps.setString(4, personalData.getTtime());
			int i = ps.executeUpdate();
			if(i == 0) {
				return false;
			}
	    } catch (Exception e) {
			return false;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return true;
	}
}

