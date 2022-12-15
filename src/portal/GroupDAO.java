package portal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class GroupDAO{
	/**
	 * ユーザーの所属グループデータとそのグループのリーダーの名前を取得
	 * @param userData
	 * 取得するユーザーのIDと名前が記録されているAccontDataBeanクラス型変数
	 * @return
	 * 取得したグループのデータ（GroupDataBeanクラス型）を格納したArrayListを返す
	 */
	public static ArrayList<GroupDataBean> affiliationData(AccountDataBean userData) {
		ArrayList<GroupDataBean> groupDataList = new ArrayList<GroupDataBean>();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("select affiliation.groupid, affiliation.groupname, user.username FROM user, affiliation, groupdata where permit=? and affiliation.userid=? and affiliation.groupid=groupdata.groupid and user.userid=groupdata.userid");
			ps.setString(1, "1");
			ps.setString(2, userData.getUserid());
			rs = ps.executeQuery();
			while (rs.next()) {
				GroupDataBean groupData = new GroupDataBean();
				groupData.setGroupid(rs.getString("groupid"));
				groupData.setGroupName(rs.getString("groupname"));
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
	 * グループIDを元にgroupdataテーブルのデータとグループリーダーの名前を取得
	 * @param groupId
	 * グループID
	 * @return
	 * 取得したグループのデータ（GroupDataBeanクラス型）を格納したArrayListを返す
	 */
	public static GroupDataBean groupData(String groupId) {
		GroupDataBean groupData = new GroupDataBean();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			ps = db.prepareStatement("SELECT groupdata.groupid, groupdata.invitationcode, groupdata.groupname, groupdata.userid, user.username FROM groupdata, user where groupid=? and user.userid=groupdata.userid");
			ps.setString(1, groupId);
			rs = ps.executeQuery();
			while (rs.next()) {
				groupData.setInvitationcode(rs.getString("invitationcode"));
				groupData.setGroupid(rs.getString("groupid"));
				groupData.setGroupName(rs.getString("groupname"));
				groupData.setUserid(rs.getString("userid"));
				groupData.setUsername(rs.getString("username"));
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
		return groupData;
	}

	/**
	 * 新しいグループを作成するためのDAO
	 * @param newGroupData
	 * 作成するグループの情報（GroupDataBeanクラス型）
	 * @return
	 * 新しく作成したグループの情報（GroupDataBeanクラス型）を格納したArrayListを返す
	 */
	public static GroupDataBean newGroup (GroupDataBean newGroupData) {
		ResultSet rs = null;
	    PreparedStatement psGroupData = null;
	    PreparedStatement psAffiliation = null;
	    PreparedStatement psPickGroupID = null;
	    Connection db = null;
	    int newGroupCount = 0;
	    int newAffiliationCount = 0;
		try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			// グループ情報データベースにデータを登録
			psGroupData = db.prepareStatement("insert into groupdata(invitationcode, groupname, userid) values (?,?,?)");
			psGroupData.setString(1, newGroupData.getInvitationcode());
			psGroupData.setString(2, newGroupData.getGroupName());
			psGroupData.setString(3, newGroupData.getUserid());
			newGroupCount = psGroupData.executeUpdate();

			// 登録したデータのグループIDを取得
			psPickGroupID = db.prepareStatement("SELECT * FROM groupdata where invitationcode=?");
			psPickGroupID.setString(1, newGroupData.getInvitationcode());
			rs = psPickGroupID.executeQuery();
			while (rs.next()) {
				newGroupData.setGroupid(rs.getString("groupid"));
			}

			// 所属グループ情報データベースにデータを登録
			psAffiliation = db.prepareStatement("insert into affiliation(userid, groupid, groupname, permit) values (?,?,?,?)");
			psAffiliation.setString(1, newGroupData.getUserid());
			psAffiliation.setString(2, newGroupData.getGroupid());
			psAffiliation.setString(3, newGroupData.getGroupName());
			psAffiliation.setString(4, newGroupData.getPermit());
			newAffiliationCount = psAffiliation.executeUpdate();
		} catch (Exception e) {
			return null;
		} finally {
		try {
			if(rs != null) {rs.close();}
			if(psGroupData != null) {psGroupData.close();}
			if(psPickGroupID != null) {psPickGroupID.close();}
			if(psAffiliation != null) {psAffiliation.close();}
			if(db != null) {db.close();}
		} catch(Exception e) {}
		}
		// データベースへデータが登録できた場合そのデータの内容を含むnewGroupDataを返す
		if(newGroupCount * newAffiliationCount != 0) {
			return newGroupData;
		} else {
			return null;
		}
	}

	/**
	 * グループに所属している全てのユーザー情報を取得するメソッド
	 * @param groupData
	 * 取得したいグループの情報
	 * @return
	 * 取得した全ての情報（GroupDataBeanクラス型）を格納したArrayListを返す
	 */
	public static ArrayList<GroupDataBean> groupUserPick(GroupDataBean groupData) {
		ArrayList<GroupDataBean> groupUserList = new ArrayList<GroupDataBean>();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();

		    ps = db.prepareStatement("SELECT * FROM user, affiliation where user.userid=affiliation.userid and affiliation.groupid=? and affiliation.permit='1'");
		    ps.setString(1, groupData.getGroupid());
		    rs = ps.executeQuery();
			while (rs.next()) {
				GroupDataBean groupUser = new GroupDataBean();
				groupUser.setUserid(rs.getString("userid"));
				groupUser.setUsername(rs.getString("username"));
				groupUser.setGroupid(rs.getString("groupid"));
				groupUser.setGroupName(rs.getString("groupname"));
				groupUser.setPermit(rs.getString("permit"));
				groupUserList.add(groupUser);
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
		return groupUserList;
	}

	/**
	 * グループへの認証待ちのユーザー情報を取得するメソッド
	 * @param groupData
	 * 対象のグループの情報
	 * @return
	 * 取得した認証待ちのユーザー情報
	 */
	public static ArrayList<GroupDataBean> waitUserPick(GroupDataBean groupData) {
		ArrayList<GroupDataBean> waitUserList = new ArrayList<GroupDataBean>();
		ResultSet rs = null;
	    PreparedStatement ps = null;
	    Connection db = null;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();

		    ps = db.prepareStatement("SELECT * FROM user, affiliation where user.userid=affiliation.userid and affiliation.groupid=? and affiliation.permit='0'");
		    ps.setString(1, groupData.getGroupid());
		    rs = ps.executeQuery();
			while (rs.next()) {
				GroupDataBean waitUser = new GroupDataBean();
				waitUser.setUserid(rs.getString("userid"));
				waitUser.setUsername(rs.getString("username"));
				waitUser.setGroupid(rs.getString("groupid"));
				waitUser.setGroupName(rs.getString("groupname"));
				waitUser.setPermit(rs.getString("permit"));
				waitUserList.add(waitUser);
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
		return waitUserList;
	}

	/**
	 * グループへの加入申請用DAO
	 * @param invitationCode
	 * 入力された招待コード
	 * @param groupReqUserId
	 * 申請を行ったユーザーのユーザーID
	 * @return
	 * 申請が完了した件数
	 */
	public static int groupRequest(String invitationCode , String groupReqUserId) {
		ResultSet rs = null;
		PreparedStatement psGroupData = null;
	    PreparedStatement psInvitation = null;
	    Connection db = null;
	    int groupRequest = 0;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();

			// groupdateテーブルから招待コードに一致するデータを取得
			psGroupData = db.prepareStatement("select * from groupdata where invitationcode=?");
			psGroupData.setString(1, invitationCode);
		    rs = psGroupData.executeQuery();
		    GroupDataBean groupData = new GroupDataBean();

		    while (rs.next()) {
		    	groupData.setGroupid(rs.getString("groupid"));
		    	groupData.setGroupName(rs.getString("groupname"));
		    }
		    // affiliationテーブルにユーザーデータを追加
		    psInvitation = db.prepareStatement("insert into affiliation values (?,?,?,?)");
		    psInvitation.setString(1, groupReqUserId);
		    psInvitation.setString(2, groupData.getGroupid());
		    psInvitation.setString(3, groupData.getGroupName());
		    psInvitation.setString(4, "0");
		    groupRequest = psInvitation.executeUpdate();
	    } catch (Exception e) {
			return 0;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(psGroupData != null) {psGroupData.close();}
				if(psInvitation != null) {psInvitation.close();}
				if(db != null) {db.close();}
		} catch(Exception e) {}
		}
		return groupRequest;
	}

	/**
	 * グループに関わる全てのデータをデータベースから削除するメソッド
	 * @param groupId
	 * 削除対象のグループID
	 * @return
	 * 削除が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean deleteGroup(String groupId) {
		ResultSet rs = null;
		PreparedStatement psGroupDataDel = null;
		PreparedStatement psAffiliationDel = null;
		PreparedStatement psInfoDel = null;
	    Connection db = null;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			// groupdataテーブルのデータを削除
			psGroupDataDel = db.prepareStatement("delete from groupdata where groupid=?");
			psGroupDataDel.setString(1, groupId);
			psGroupDataDel.executeUpdate();

			// affiliationテーブルのデータを削除
			psAffiliationDel = db.prepareStatement("delete from affiliation where groupid=?");
			psAffiliationDel.setString(1, groupId);
			psAffiliationDel.executeUpdate();

			// infoテーブルのデータを削除
			psInfoDel = db.prepareStatement("delete from info where groupid=?");
			psInfoDel.setString(1, groupId);
			psInfoDel.executeUpdate();

		    db.commit(); // 削除を適用
	    } catch (Exception e) {
	    	try {
				db.rollback(); // 例外発生時削除を取り消し
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(psGroupDataDel != null) {psGroupDataDel.close();}
				if(psAffiliationDel != null) {psAffiliationDel.close();}
				if(psInfoDel != null) {psInfoDel.close();}
				if(db != null) {db.close();}
		} catch(Exception e) {}
		}
		return true;
	}

	/**
	 * グループへの加入申請を認証するメソッド
	 * @param userId
	 * 認証対象のユーザーのユーザーID
	 * @param groupId
	 * 加入対象のグループのグループID
	 * @return
	 * 認証が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean permit(String userId , String groupId){
		ResultSet rs = null;
		PreparedStatement ps = null;
	    Connection db = null;
	    boolean permit = false;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			// affiliationテーブルのpermitを'1'に書き換える
			ps = db.prepareStatement("UPDATE affiliation SET permit=? where userid=? and groupid=?");
			ps.setString(1, "1");
			ps.setString(2, userId);
			ps.setString(3, groupId);
			ps.executeUpdate();

		    db.commit(); // 変更を適用
		    permit = true;
	    } catch (Exception e) {
	    	try { // 例外発生時変更を取り消し
				db.rollback();
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			return permit;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
		} catch(Exception e) {}
		}
		return permit;
	}

	/**
	 * グループへの加入申請を削除するメソッド
	 * @param userId
	 * 申請削除対象のユーザーのユーザーID
	 * @param groupId
	 * 対象のグループのグループID
	 * @return
	 * 加入申請の削除が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean permitDelete (String userId , String groupId) {
		ResultSet rs = null;
		boolean permitDelete = false;
		PreparedStatement ps = null;
	    Connection db = null;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			// 削除実行
			ps = db.prepareStatement("delete from affiliation where userid=? and groupid=?");
			ps.setString(1, userId);
			ps.setString(2, groupId);
			ps.executeUpdate();

		    db.commit(); // 変更を適用
		    permitDelete = true;
	    } catch (Exception e) {
	    	try { // 例外発生時変更を取り消し
				db.rollback();
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			return permitDelete;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(ps != null) {ps.close();}
				if(db != null) {db.close();}
		} catch(Exception e) {}
		}
		return permitDelete;
	}

	/**
	 * グループ情報の編集用メソッド
	 * @param editingGroupData
	 * 編集後の内容
	 * @return
	 * 編集が正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean editingGroup(GroupDataBean editingGroupData){
		ResultSet rs = null;
		PreparedStatement psGroupData = null;
		PreparedStatement psAffilation = null;
	    Connection db = null;
	    boolean editGroup = false;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			// groupdataテーブルを変更
			psGroupData = db.prepareStatement("update groupdata set groupname=?, invitationcode=? where groupid=?");
			psGroupData.setString(1, editingGroupData.getGroupName());
			psGroupData.setString(2, editingGroupData.getInvitationcode());
			psGroupData.setString(3, editingGroupData.getGroupid());
			psGroupData.executeUpdate();

			// affiliationテーブルを変更
			psAffilation = db.prepareStatement("update affiliation set groupname=? where groupid=?");
			psAffilation.setString(1, editingGroupData.getGroupName());
			psAffilation.setString(2, editingGroupData.getGroupid());
			psAffilation.executeUpdate();

		    db.commit(); // 変更を適用
		    editGroup = true;
	    } catch (Exception e) {
	    	try { // 例外発生時変更を取り消し
				db.rollback();
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			return editGroup;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(psGroupData != null) {psGroupData.close();}
				if(psAffilation != null) {psAffilation.close();}
				if(db != null) {db.close();}
		} catch(Exception e) {}
		}
		return editGroup;
	}

	/**
	 * ユーザーをグループからブロックするメソッド
	 * @param userId
	 * 対象ユーザーのユーザーID
	 * @param groupId
	 * 対象のグループのグループID
	 * @return
	 * ユーザーのブロックが正しく完了したかどうかを示すboolean型変数（true＝成功）
	 */
	public static boolean userBlock (String userId , String groupId) {
		ResultSet rs = null;
		boolean userBlock = false;
		PreparedStatement psAffiliation = null;
//		PreparedStatement psInfo = null;
//		PreparedStatement psSchedule = null;
		PreparedStatement psThread = null;
	    Connection db = null;
	    try{
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/portal");
			db = ds.getConnection();
			db.setAutoCommit(false);

			// affiliationテーブルから削除
			psAffiliation = db.prepareStatement("delete from affiliation where userid=? and groupid=?");
			psAffiliation.setString(1, userId);
			psAffiliation.setString(2, groupId);
			psAffiliation.executeUpdate();

			// threadテーブルから削除
			psThread = db.prepareStatement("delete from thread where userid=? and groupid=?");
			psThread.setString(1, userId);
			psThread.setString(2, groupId);
			psAffiliation.executeUpdate();

			// infoテーブルから削除
//			psInfo = db.prepareStatement("delete from Info where userid=? and groupid=?");
//			psInfo.setString(1, userId);
//			psInfo.setString(2, groupId);
//			psInfo.executeUpdate();

			// scheduleテーブルから削除
//			psSchedule = db.prepareStatement("delete from schedule where userid=? and groupid=?");
//			psSchedule.setString(1, userId);
//			psSchedule.setString(2, groupId);
//			psSchedule.executeUpdate();

		    db.commit(); // 変更を適用
		    userBlock = true;
	    } catch (Exception e) {
	    	try { // 例外発生時変更を取り消し
				db.rollback();
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			return userBlock;
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(psAffiliation != null) {psAffiliation.close();}
				if(psThread != null) {psThread.close();}
//				if(psInfo != null) {psInfo.close();}
//				if(psSchedule != null) {psSchedule.close();}
				if(db != null) {db.close();}
		} catch(Exception e) {}
		}
		return userBlock;
	}

}
