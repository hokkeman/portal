package portal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class LoginDAO {
	/**
	 * ユーザーのログインをチェックするメソッド
	 * @param loginBean
	 * ログインフォームに入力された内容
	 * @return
	 * ログイン成功したユーザーの情報
	 */
	public static AccountDataBean loginCheck(AccountDataBean loginBean) {
		AccountDataBean returnLogin = new AccountDataBean();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("SELECT * FROM user where userid=? and userpass=?");
			ps.setString(1, loginBean.getUserid());
            ps.setString(2, loginBean.getUserpass());
			rs = ps.executeQuery();
			if (rs.next()) {
				returnLogin.setUserid(rs.getString("userid"));
				returnLogin.setUserpass(rs.getString("userpass"));
				returnLogin.setUsername(rs.getString("username"));
			} else {
				return null;
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
		return returnLogin;
	}

	/**
	 * 管理者のログインをチェックするメソッド
	 * @param loginBean
	 * 管理者のログインフォームに入力された内容
	 * @return
	 * ログイン成功した管理者の情報
	 */
	public static AccountDataBean adminloginCheck(AccountDataBean loginBean) {
		AccountDataBean returnLogin = new AccountDataBean();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("SELECT * FROM admin where adminid=? and adminpass=?");
			ps.setString(1, loginBean.getUserid());
            ps.setString(2, loginBean.getUserpass());
			rs = ps.executeQuery();
			if (rs.next()) {
				returnLogin.setUserid(rs.getString("adminid"));
				returnLogin.setUserpass(rs.getString("adminpass"));
			} else {
				return null;
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
		return returnLogin;
	}

	/**
	 * ログインを試みるユーザーのソルトを取得するメソッド
	 * @param userId
	 * ログインフォームに入力されたユーザーID
	 * @return
	 * データベースから取得したソルト
	 */
	public static String saltGet(String userId) {
		String salt = null;
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("select * from user where userid=?");
            ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				salt = rs.getString("salt");
			} else {
				return null;
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
		return salt;
	}

	/**
	 * ログインを試みる管理人のソルトを取得するメソッド
	 * @param userId
	 * 管理人のログインフォームに入力されたユーザーID
	 * @return
	 * 取得したソルト
	 */
	public static String saltAdGet(String userId) {
		String salt = null;
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("select * from admin where adminid=?");
            ps.setString(1, userId);
			rs = ps.executeQuery();
			if (rs.next()) {
				salt = rs.getString("salt");
			} else {
				return null;
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
		return salt;
	}
}
