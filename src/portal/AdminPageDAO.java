package portal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
public class AdminPageDAO{
	/**
	 * 全ての日の体温データを取得するメソッド
	 * @return 取得した全ての日の体温データ（PersonalDataBeanクラス型）を格納したArrayListを返す
	 */
	public static ArrayList<PersonalDataBean> allTemperature() {
		ArrayList<PersonalDataBean> allTempList = new ArrayList<PersonalDataBean>();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
		    ps = db.prepareStatement("select * from temperaturedata");
		    rs = ps.executeQuery();
			while (rs.next()) {
				PersonalDataBean personalData = new PersonalDataBean();
				personalData.setTid(rs.getString("tid"));
				personalData.setUserid(rs.getString("username"));
				personalData.setTemperature(rs.getString("temperature"));
				personalData.setTdate(rs.getString("tdate"));
				personalData.setTtime(rs.getString("ttime"));
				allTempList.add(personalData);
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
		return allTempList;
	}

	/**
	 * 特定の日の体温データを取得するメソッド
	 * @param tdate
	 * 取得したい体温データの日付
	 * @return
	 * 取得した体温データ（PersonalDataBeanクラス型）を格納したArrayListを返す
	 */
	public static ArrayList<PersonalDataBean> pickTemperature(String tdate) {
		ArrayList<PersonalDataBean> allTempList = new ArrayList<PersonalDataBean>();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
		    ps = db.prepareStatement("select * from temperaturedata, user where user.userid=temperaturedata.userid and tdate=? order by temperature asc");
		    ps.setString(1, tdate);
		    rs = ps.executeQuery();
			while (rs.next()) {
				PersonalDataBean personalData = new PersonalDataBean();
				personalData.setTid(rs.getString("tid"));
				personalData.setUserName(rs.getString("username"));
				personalData.setUserid(rs.getString("userid"));
				personalData.setTemperature(rs.getString("temperature"));
				personalData.setTdate(rs.getString("tdate"));
				personalData.setTtime(rs.getString("ttime"));
				allTempList.add(personalData);
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
		return allTempList;
	}
}
