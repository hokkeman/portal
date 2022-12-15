package portal;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ThreadServlet
 */
@WebServlet("/ThreadServlet")
public class ThreadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ThreadServlet() {
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
				// セッションからログインしているユーザの情報とグループのデータを取得
				GroupDataBean groupData= (GroupDataBean)session.getAttribute("GroupData");
				AccountDataBean loginUser = (AccountDataBean)session.getAttribute("LoginUser");

				// グループ内の全てのスレッドを取得する
				ArrayList<ThreadDataBean> allThreadListView = BoardDAO.allThreadGet(groupData);
				session.setAttribute("allThreadList", allThreadListView);
				page = "/Thread.jsp";
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
		// セッションからログインしているユーザの情報とグループのデータを取得
		GroupDataBean groupData= (GroupDataBean)session.getAttribute("GroupData");
		AccountDataBean loginUser = (AccountDataBean)session.getAttribute("LoginUser");
		switch(pid){
		case "toThreadPage":
			// グループ内の全てのスレッドを取得する
			ArrayList<ThreadDataBean> allThreadList = BoardDAO.allThreadGet(groupData);
			session.setAttribute("allThreadList", allThreadList);
			page = "/Thread.jsp";
			break;
		case "CreateThread":
		// スレッド作成に必要な情報をBeansにセット
			ThreadDataBean createThreadData = new ThreadDataBean();
			// スレッドタイトル
			createThreadData.setThreadTitle(request.getParameter("ThreadTitle"));
			// 現在の日時
			String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			String nowTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			createThreadData.setThreadDate(nowDate);
			createThreadData.setThreadTime(nowTime);
			// ユーザー名、グループID
			createThreadData.setUserName(loginUser.getUsername());
			createThreadData.setGroupId(groupData.getGroupid());

		// 作成するスレッドの情報をセットしたBeansと1件目の書き込み内容を渡し、データベース作成
			boolean createOK = BoardDAO.createThread(createThreadData, request.getParameter("Contents"));
			if(createOK) {
				session.setAttribute("secondAccess", "1");
				page = "/ThreadServlet";
			} else {
				session.setAttribute("secondAccess", "0");
				session.setAttribute("ErrorMsg", "ThreadCreateError");
				page = "/Error.jsp";
			}
			break;
		// スレッドを削除する
		case "delete":
			boolean deleteOK = BoardDAO.deleteThread(request.getParameter("ThreadId"));
			if(deleteOK) {
				session.setAttribute("secondAccess", "1");
				page = "/ThreadServlet";
			} else {
				session.setAttribute("secondAccess", "0");
				session.setAttribute("ErrorMsg", "ThreadDeleteError");
				page = "/Error.jsp";
			}
			break;
		case "View":
			// グループ内の全てのスレッドを取得する
			ArrayList<ThreadDataBean> allThreadListView = BoardDAO.allThreadGet(groupData);
			session.setAttribute("allThreadList", allThreadListView);
			session.setAttribute("secondAccess", "0");
			page = "/Thread.jsp";
		}

		this.getServletContext().getRequestDispatcher(page).forward(request,response);
	}

}
