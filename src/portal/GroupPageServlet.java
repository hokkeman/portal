package portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GroupPageServlet
 */
@WebServlet("/GroupPageServlet")
public class GroupPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupPageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String page = null; // 遷移先
			// ログイン済みか確認
			HttpSession session = request.getSession();
			boolean loginOK = false;
			if(session.getAttribute("LoginUser") != null) {
				loginOK = true;
			} else {
				this.getServletContext().getRequestDispatcher("/TopPage.jsp").forward(request,response);
			}

			// ログイン済みの場合実行
			if(loginOK) {
				String pid = request.getParameter("PID");

				// ログインユーザーの情報を取得
				AccountDataBean loginUser = (AccountDataBean)session.getAttribute("LoginUser");
				String userId = loginUser.getUserid();
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
		String page = null; // 遷移先
		// ログイン済みか確認
		HttpSession session = request.getSession();
		request.setCharacterEncoding("UTF-8");
		boolean loginOK = false;
		if(session.getAttribute("LoginUser") != null) {
			loginOK = true;
		} else {
			session.setAttribute("ErrorMsg", "SessionError");
			page = "/Error.jsp";
		}

		// ログイン済みの場合実行
		if(loginOK) {
			String pid = request.getParameter("PID");
			session.setAttribute("GroupName" , request.getParameter("GroupName"));
			session.setAttribute("GroupID" , request.getParameter("GroupID"));
			switch(pid) {
				case "GroupChoose":
					GroupDataBean groupData = (GroupDataBean)GroupDAO.groupData(request.getParameter("GroupID"));
					session.setAttribute("GroupData" , groupData);
					page = "/GroupPage.jsp";
					break;
				case "NewGroup":
					AccountDataBean loginUser = (AccountDataBean)session.getAttribute("LoginUser");
					GroupDataBean newGroupBean = new GroupDataBean();
					newGroupBean.setGroupName(request.getParameter("GroupName"));
					newGroupBean.setInvitationcode(request.getParameter("InvitationCode"));
					newGroupBean.setUserid(loginUser.getUserid());
					newGroupBean.setPermit("1");
					newGroupBean = GroupDAO.newGroup(newGroupBean);
					if(newGroupBean != null) {
						session.setAttribute("NewGroupData", newGroupBean);
						page = "/NewGroupResult.jsp";
					} else {
						session.setAttribute("ErrorMsg", "newGroupError");
						page = "/Error.jsp";
					}
					break;
				case "GroupRequest" :
					AccountDataBean groupReqUser = (AccountDataBean)session.getAttribute("LoginUser");
					int groupReqCnt = GroupDAO.groupRequest(request.getParameter("InvitationCode"), groupReqUser.getUserid());
					if(groupReqCnt != 0) {
						page="/OtherGroupReq.jsp";
					} else {
						session.setAttribute("ErrorMsg", "GroupRequest");
						page = "/Error.jsp";
					}
					break;
				case "CalenderChoose":
					page = "/CalenderChoose.jsp";
					break;
				case "Schedule" :
					break;
			}
			this.getServletContext().getRequestDispatcher(page).forward(request,response);
		}
	}

}
