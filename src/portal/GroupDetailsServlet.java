package portal;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GroupDetailsServlet
 */
@WebServlet("/GroupDetailsServlet")
public class GroupDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupDetailsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/TopPage.jsp").forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String page = null; // 遷移先

		// 押されたボタンの種類を判定
		String pid = request.getParameter("PID");
		try {
			if(session.getAttribute("secondAccess").equals("1") || pid == null) {
				pid = "View";
			}
		} catch (Exception e) {
		}

		switch(pid) {
		case "Block": // ユーザーをグループからブロック
			boolean userBlock = GroupDAO.userBlock(request.getParameter("UserId"), request.getParameter("GroupId"));
			if(userBlock) {
				page = "/GroupDetailsServlet";
				session.setAttribute("secondAccess", "1");
			} else {
				session.setAttribute("ErrorMsg", "BlockError");
				page = "/Error.jsp";
			}
			break;
		case "GroupEdit": // グループを編集
			GroupDataBean editingGroupData = (GroupDataBean)session.getAttribute("GroupData");
			editingGroupData.setGroupid(request.getParameter("GroupId"));
			editingGroupData.setInvitationcode(request.getParameter("InvitationCode"));
			editingGroupData.setGroupName(request.getParameter("GroupName"));
			editingGroupData.setUserid(request.getParameter("UserId"));
			boolean editGroup = GroupDAO.editingGroup(editingGroupData);
			if(editGroup) {
				page = "/GroupDetailsServlet";
				session.setAttribute("secondAccess", "1");
			} else {
				session.setAttribute("ErrorMsg", "AdminGroupEditError");
				page = "/Error.jsp";			}
			break;
		case "GroupDelete": // グループを削除
			boolean groupDelete = GroupDAO.deleteGroup(request.getParameter("GroupId"));
			if(groupDelete) {
				page = "/AdminGroupServlet";
				session.setAttribute("secondAccess", "1");
			} else {
				session.setAttribute("ErrorMsg", "AdGroupDeleteError");
				page = "/Error.jsp";
			}
			break;
		case "Leader":
			boolean leaderOK = AdminGroupDAO.leader(request.getParameter("UserId"), request.getParameter("GroupId"));
			if(leaderOK) {
				page = "/GroupDetailsServlet";
				session.setAttribute("secondAccess", "1");
			} else {
				session.setAttribute("ErrorMsg", "LeaderError");
				page = "/Error.jsp";
			}
			break;
		case "View":
			// セッションからグループの情報とメンバーリストを取得
			GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");

			// グループの情報を取得
			groupData = GroupDAO.groupData(groupData.getGroupid());

			// グループのメンバーを全て取得
			ArrayList<GroupDataBean> memberList = GroupDAO.groupUserPick(groupData);

			// 取得したデータをセッションに保存
			if(groupData != null && memberList.size() != 0) {
				session.setAttribute("GroupData" , groupData);
				session.setAttribute("MemberList" , memberList);
				page = "/AdminGroupDetails.jsp";
			} else {
				session.setAttribute("ErrorMsg", "GroupDetailsError");
				page = "/Error.jsp";
			}
			session.setAttribute("secondAccess", "0");
			break;
		}
		this.getServletContext().getRequestDispatcher(page).forward(request,response);
	}
}
