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
 * Servlet implementation class AdminGroupServlet
 */
@WebServlet("/AdminGroupServlet")
public class AdminGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminGroupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			HttpSession session = request.getSession();
			String page = null; // 遷移先

			// 全てのグループの情報を取得
			ArrayList<GroupDataBean> allGroupList = new ArrayList<GroupDataBean>();
			allGroupList = (ArrayList<GroupDataBean>)AdminGroupDAO.allGroup();

			// 取得したデータをセッションへ保存
			if(allGroupList.size() != 0) {
				session.setAttribute("AllGroupList", allGroupList);
				session.setAttribute("secondAccess", "0");
				page = "/AdminGroupManage.jsp";
			}
			this.getServletContext().getRequestDispatcher(page).forward(request,response);
		} catch (Exception e) {
			this.getServletContext().getRequestDispatcher("/TopPage.jsp").forward(request,response);
		}
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
		case "AdminGroupManage":
			// 全てのグループの情報を取得
			ArrayList<GroupDataBean> allGroupList = new ArrayList<GroupDataBean>();
			allGroupList = (ArrayList<GroupDataBean>)AdminGroupDAO.allGroup();

			// 取得したデータをセッションへ保存
			if(allGroupList.size() != 0) {
				session.setAttribute("AllGroupList", allGroupList);
				session.setAttribute("secondAccess", "1");
				page = "/AdminGroupServlet";
			} else {
				session.setAttribute("ErrorMsg", "GroupManage");
				page = "/Error.jsp";
			}
			break;
		case "GroupDetails":
			// グループの情報を取得
			GroupDataBean groupData = GroupDAO.groupData(request.getParameter("GroupId"));

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
			break;
		case "View":
			// 全てのグループの情報を取得
			ArrayList<GroupDataBean> allGroupListView = new ArrayList<GroupDataBean>();
			allGroupListView = (ArrayList<GroupDataBean>)AdminGroupDAO.allGroup();

			// 取得したデータをセッションへ保存
			if(allGroupListView.size() != 0) {
				session.setAttribute("AllGroupList", allGroupListView);
			session.setAttribute("secondAccess", "0");
			page = "/AdminGroupManage.jsp";
			}
			break;
		}
		this.getServletContext().getRequestDispatcher(page).forward(request,response);
	}
}
