package portal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserManageDAO {
	/**
	 * 全てのユーザーのデータを取得するメソッド
	 * @return
	 * 取得したユーザーのデータ（AccountDataBeanクラス型）を格納したArrayList
	 */
	public static ArrayList<AccountDataBean> allUserData() {
		ArrayList<AccountDataBean> allUserList = new ArrayList<AccountDataBean>();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
		    ps = db.prepareStatement("select * from user");
		    rs = ps.executeQuery();
			while (rs.next()) {
				AccountDataBean userData = new AccountDataBean();
				userData.setUserid(rs.getString("userid"));
				userData.setUsername(rs.getString("username"));
				allUserList.add(userData);
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
		return allUserList;
	}

	/**
	 * 全てのグループデータを取得するメソッド
	 * @return
	 * 取得したグループのデータ（GroupDataBeanクラス型）を格納したArrayList
	 */
	public static ArrayList<GroupDataBean> allGroupData() {
		ArrayList<GroupDataBean> allGroupList = new ArrayList<GroupDataBean>();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
		    ps = db.prepareStatement("select * from groupdata");
		    rs = ps.executeQuery();
			while (rs.next()) {
				GroupDataBean groupData = new GroupDataBean();
				groupData.setGroupid(rs.getString("groupid"));
				groupData.setGroupName(rs.getString("groupname"));
				groupData.setInvitationcode("invitationcode");
				groupData.setUserid("userid");
				allGroupList.add(groupData);
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
		return allGroupList;
	}

	/**
	 * 新規ユーザーを追加するメソッド
	 * @param newUserData
	 * 追加するユーザーの情報
	 * @return
	 * ユーザーの追加が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean newUser(AccountDataBean newUserData) {
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    boolean newUserOK = false;
	    try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			ps = db.prepareStatement("insert into user values (?,?,?,?)");
			ps.setString(1, newUserData.getUserid());
			ps.setString(2, newUserData.getUserpass());
			ps.setString(3, newUserData.getUsername());
			ps.setString(4, newUserData.getSalt());
			ps.executeUpdate();

			db.commit(); // 変更を適用
			newUserOK = true;
	    } catch (Exception e) {
	    	try {
				db.rollback();
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			return newUserOK;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return newUserOK;
	}

	/**
	 * 新規ユーザーの初期所属グループを設定するメソッド
	 * @param newUserGroup
	 * 設定する初期所属グループの情報
	 * @return
	 * 設定が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean newUserGroup(GroupDataBean newUserGroup) {
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    PreparedStatement psGetGroupName = null;
	    Connection db = null;
	    boolean newUserGroupOK = false;
	    String groupName = null;
	    try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			// グループIDを元にグループ名をデータベースから取得
			psGetGroupName = db.prepareStatement("select * from groupdata where groupid=?");
			psGetGroupName.setString(1, newUserGroup.getGroupid());
			rs = psGetGroupName.executeQuery();
			while (rs.next()) {
				groupName = rs.getString("groupname");
			}

			// 新規ユーザーの初期所属グループをデータベースに設定
			ps = db.prepareStatement("insert into affiliation values (?,?,?,?)");
			ps.setString(1, newUserGroup.getUserid());
			ps.setString(2, newUserGroup.getGroupid());
			ps.setString(3, groupName);
			ps.setString(4, "1");
			ps.executeUpdate();

			db.commit(); // 変更を適用
			newUserGroupOK = true;
	    } catch (Exception e) {
	    	try {
				db.rollback();
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			return newUserGroupOK;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(psGetGroupName != null) {psGetGroupName.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return newUserGroupOK;
	}

	/**
	 * ユーザーIDを元にuserテーブルからユーザー情報を取得するメソッド
	 * @param userId
	 * 対象のユーザーのユーザーID
	 * @return
	 * 取得したユーザーの情報
	 */
	public static AccountDataBean pickUserData(String userId) {
		AccountDataBean userData = new AccountDataBean();
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
			while (rs.next()) {
				userData.setUserid(rs.getString("userid"));
				userData.setUsername(rs.getString("username"));
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
		return userData;
	}

	/**
	 * ユーザーを削除するメソッド
	 * @param userId
	 * 削除対象のユーザーのユーザーID
	 * @return
	 * ユーザーの削除が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean userDelete(String userId) {
		ResultSet rs = null;
	    PreparedStatement psUserTable = null;
	    PreparedStatement psAffiliationTable = null;
	    Connection db = null;
	    boolean userDeleteOK = false;
	    try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);
			// userテーブルからユーザーを削除
			psUserTable = db.prepareStatement("DELETE FROM user WHERE userid=?");
			psUserTable.setString(1, userId);
			psUserTable.executeUpdate();

			// affiliationテーブルからユーザーを削除
			psAffiliationTable = db.prepareStatement("DELETE FROM affiliation WHERE userid=?");
			psAffiliationTable.setString(1, userId);
			psAffiliationTable.executeUpdate();

			db.commit(); // 変更を適用
			userDeleteOK = true;
	    } catch (Exception e) {
	    	try {
				db.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return userDeleteOK;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(psUserTable != null) {psUserTable.close();}
				if(psAffiliationTable != null) {psAffiliationTable.close();}
				if(db != null) {db.close();}
			} catch(Exception e) {}
		}
		return userDeleteOK;
	}
}
