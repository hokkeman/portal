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
 * Servlet implementation class UserManage
 */
@WebServlet("/UserManageServlet")
public class UserManageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserManageServlet() {
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

		switch(pid){
		case "UserManage":
			// 全てのユーザーの情報を取得しセッションに保存
			ArrayList<AccountDataBean> allUserList = UserManageDAO.allUserData();
			session.setAttribute("AllUserList" , allUserList);

			// 全てのグループの情報を取得する(新規ユーザー作成画面用)
			ArrayList<GroupDataBean> allGroupList = UserManageDAO.allGroupData();
			session.setAttribute("AllGroupList" , allGroupList);
			page = "/UserManage.jsp";

			break;
		case "NewUser":
			// 新規作成するユーザー情報をリクエストから取得しBeansにセット
			AccountDataBean newUserData = new AccountDataBean();
			newUserData.setUserid(request.getParameter("UserId"));
			newUserData.setUsername(request.getParameter("UserName"));

			// パスワードにソルトを付加しハッシュ化する
			String salt = Hash.makeSalt();
			String hashPass = Hash.hashing(request.getParameter("UserPass") , salt);
			newUserData.setSalt(salt);
			newUserData.setUserpass(hashPass);

			// 新規作成するユーザーの初期所属グループをデータベースに登録
			boolean newUserGroupOK = false;
			String[] checkBox = request.getParameterValues("Group");
			GroupDataBean newUserGroup = new GroupDataBean();
			newUserGroup.setUserid(request.getParameter("UserId"));
			if(checkBox != null) {
				for(int i = 0; i<checkBox.length; i++) {
					newUserGroup.setGroupid(checkBox[i]);
					newUserGroupOK = UserManageDAO.newUserGroup(newUserGroup);
				}
			} else {
				newUserGroupOK = true;
			}

			// Beansを元に新規ユーザーをデータベースに追加
			boolean newUserOK = UserManageDAO.newUser(newUserData);
			if(newUserOK && newUserGroupOK) {
				session.setAttribute("secondAccess", "1");
				page = "/UserManageServlet";
			} else {
				session.setAttribute("ErrorMsg", "NewUserError");
				page = "/Error.jsp";
			}
			break;
		case "UserDetails":
			// ユーザーIDを元に所属グループの一覧を取得しセッションに保存
			AccountDataBean userData = new AccountDataBean();
			userData.setUserid(request.getParameter("UserId"));
			userData.setUsername(request.getParameter("UserName"));
			session.setAttribute("UserDetails", userData);
			ArrayList<GroupDataBean> groupDataList = GroupDAO.affiliationData(userData);
			session.setAttribute("GroupDataList", groupDataList);
			page = "/UserDetails.jsp";
			break;
		case "UserDelete":
			boolean userDeleteOK = UserManageDAO.userDelete(request.getParameter("UserId"));
			if(userDeleteOK) {
				session.setAttribute("secondAccess", "1");
				page = "/UserManageServlet";
			} else {
				session.setAttribute("ErrorMsg", "UserDeleteError");
				page = "/Error.jsp";
			}
			break;
		case "View":
			// 全てのユーザーの情報を取得する
			ArrayList<AccountDataBean> allUserListView = UserManageDAO.allUserData();
			session.setAttribute("AllUserList" , allUserListView);

			// 全てのグループの情報を取得する(新規ユーザー作成画面用)
			ArrayList<GroupDataBean> allGroupListView = UserManageDAO.allGroupData();
			session.setAttribute("AllGroupList" , allGroupListView);

			session.setAttribute("secondAccess", "0");
			page = "/UserManage.jsp";
			break;
		}
		this.getServletContext().getRequestDispatcher(page).forward(request,response);
	}

}