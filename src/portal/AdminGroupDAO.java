package portal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class AdminGroupDAO {
	/**
	 * 全グループの情報とそれぞれのグループリーダーの名前を取得
	 * @return 取得したデータ(GroupDataBeanクラス型)を格納したArrayListを返す
	 */
	public static ArrayList<GroupDataBean> allGroup() {
		ArrayList<GroupDataBean> groupDataList = new ArrayList<GroupDataBean>();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("select groupdata.groupid, groupdata.invitationcode, groupdata.groupname, groupdata.userid, user.username FROM user, groupdata where user.userid=groupdata.userid");
			rs = ps.executeQuery();
			while (rs.next()) {
				GroupDataBean groupData = new GroupDataBean();
				groupData.setGroupid(rs.getString("groupid"));
				groupData.setInvitationcode(rs.getString("invitationcode"));
				groupData.setGroupName(rs.getString("groupname"));
				groupData.setUserid(rs.getString("userid"));
				groupData.setUsername(rs.getString("username"));
				groupDataList.add(groupData);
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
		return groupDataList;
	}

	/**
	 * グループの管理者を変更するメソッド
	 * @param userId
	 * ユーザーID
	 * @param groupId
	 * グループID
	 * @return 変更に成功したか示すboolean型変数を返す（true＝成功）
	 */
	public static boolean leader(String userId, String groupId){
		ResultSet rs = null;
		PreparedStatement ps = null;
	    Connection db = null;
	    boolean leaderOK = false;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			// affiliationテーブルのpermitを'1'に書き換える
			ps = db.prepareStatement("UPDATE groupdata SET userid=? where groupid=?");
			ps.setString(1, userId);
			ps.setString(2, groupId);
			ps.executeUpdate();

		    db.commit(); // 変更を適用
		    leaderOK = true;
	    } catch (Exception e) {
	    	try { // 例外発生時変更を取り消し
				db.rollback();
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			return leaderOK;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
		} catch(Exception e) {}
		}
		return leaderOK;
	}
}
