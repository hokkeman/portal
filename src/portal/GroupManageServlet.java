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
 * Servlet implementation class GroupManageServlet
 */
@WebServlet("/GroupManageServlet")
public class GroupManageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupManageServlet() {
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
		// セッションからログインしているユーザの情報とグループのデータを取得
		GroupDataBean groupData= (GroupDataBean)session.getAttribute("GroupData");
		AccountDataBean loginUser = (AccountDataBean)session.getAttribute("LoginUser");

		switch(pid) {
		case "GroupManage": // グループ管理画面へ
			GroupDataBean groupDataEdit = (GroupDataBean)session.getAttribute("GroupData");

			// グループに所属するユーザーの情報を取得
			ArrayList<GroupDataBean> groupUserList = new ArrayList<GroupDataBean>();
			groupUserList = (ArrayList<GroupDataBean>)GroupDAO.groupUserPick(groupDataEdit);

			// グループへの認証待ちのユーザーの情報を取得
			ArrayList<GroupDataBean> waitUserList = new ArrayList<GroupDataBean>();
			waitUserList = (ArrayList<GroupDataBean>)GroupDAO.waitUserPick(groupDataEdit);

			// 取得したデータをセッションへ保存
			if(groupUserList != null) {
				session.setAttribute("GroupUserList", groupUserList);
				if(waitUserList != null) {
					session.setAttribute("WaitUserList", waitUserList);
				}
				page = "/GroupManage.jsp";
			} else {
				session.setAttribute("ErrorMsg", "GroupManage");
				page = "/Error.jsp";
			}
			break;
		case "Permit": // 加入申請を認証
			boolean permit = GroupDAO.permit(request.getParameter("UserId") , request.getParameter("GroupId"));
			if(permit){
				page = "/GroupManageServlet";
				session.setAttribute("secondAccess", "1");
			} else {
				session.setAttribute("ErrorMsg", "PermitError");
				page = "/Error.jsp";
			}
			break;
		case "PermitDelete" :
			boolean permitDelete = GroupDAO.permitDelete(request.getParameter("UserId") , request.getParameter("GroupId"));
			if(permitDelete){
				page = "/GroupManageServlet";
				session.setAttribute("secondAccess", "1");
			} else {
				session.setAttribute("ErrorMsg", "PermitDeleteError");
				page = "/Error.jsp";
			}
			break;
		case "Block": // ユーザーをグループからブロック
			boolean userBlock = GroupDAO.userBlock(request.getParameter("UserId"), request.getParameter("GroupId"));
			if(userBlock) {
				page = "/GroupManageServlet";
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
			editingGroupData.setUserid(loginUser.getUserid());
			boolean editGroup = GroupDAO.editingGroup(editingGroupData);
			if(editGroup) {
				page = "/GroupManageServlet";
				session.setAttribute("secondAccess", "1");
			} else {
				session.setAttribute("ErrorMsg", "GroupEditError");
				page = "/Error.jsp";			}
			break;
		case "GroupDelete": // グループを削除
			boolean groupDelete = GroupDAO.deleteGroup(request.getParameter("GroupId"));
			if(groupDelete) {
				page = "/UserPageServlet";
				session.setAttribute("secondAccess", "1");
			} else {
				session.setAttribute("ErrorMsg", "DeleteError");
				page = "/Error.jsp";
			}
			break;
		case "View":
			GroupDataBean groupDataEditView = (GroupDataBean)session.getAttribute("GroupData");

			// グループに所属するユーザーの情報を取得
			ArrayList<GroupDataBean> groupUserListView = new ArrayList<GroupDataBean>();
			groupUserList = (ArrayList<GroupDataBean>)GroupDAO.groupUserPick(groupDataEditView);

			// グループへの認証待ちのユーザーの情報を取得
			ArrayList<GroupDataBean> waitUserListView = new ArrayList<GroupDataBean>();
			waitUserList = (ArrayList<GroupDataBean>)GroupDAO.waitUserPick(groupDataEditView);

			// 取得したデータをセッションへ保存
			if(groupUserList != null) {
				session.setAttribute("GroupUserList", groupUserList);
				if(waitUserList != null) {
					session.setAttribute("WaitUserList", waitUserList);
				}
				session.setAttribute("secondAccess", "0");
				page = "/GroupManage.jsp";
			} else {
				session.setAttribute("ErrorMsg", "GroupManage");
				page = "/Error.jsp";
			}
		}
		this.getServletContext().getRequestDispatcher(page).forward(request,response);
	}
}

