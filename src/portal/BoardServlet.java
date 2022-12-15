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
 * Servlet implementation class BoardServlet
 */
@WebServlet("/BoardServlet")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardServlet() {
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

		// セッションからグループのデータを取得
		GroupDataBean groupData= (GroupDataBean)session.getAttribute("GroupData");
		AccountDataBean loginUser = (AccountDataBean)session.getAttribute("LoginUser");

		switch(pid){
		case "toBoardPage":
			// 全ての書き込みデータを取得する
			ArrayList<BoardDataBean> allBoardList = BoardDAO.allBoardGet(request.getParameter("ThreadId"));
			session.setAttribute("ThreadTitle", request.getParameter("ThreadTitle"));
			session.setAttribute("ThreadId", request.getParameter("ThreadId"));
			session.setAttribute("allBoardList", allBoardList);
			page = "/Board.jsp";
			break;
		case "BoardPost":
			// 現在の日時を取得
			String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
			String nowTime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

			// 書き込み投稿に必要な情報をBeansにセット
			BoardDataBean boardData = new BoardDataBean();
			boardData.setBoardDate(nowDate);
			boardData.setBoardTime(nowTime);
			boardData.setThreadId(request.getParameter("ThreadId"));
			boardData.setContents(request.getParameter("Contents"));
			boardData.setUserName(loginUser.getUsername());
			boardData.setGroupId(groupData.getGroupid());

			// 書き込み内容をデータベースに追加
			boolean postOK = BoardDAO.boardPost(boardData);
			if(postOK) {
				session.setAttribute("secondAccess", "1");
				page = "/BoardServlet";
			} else {
				session.setAttribute("ErrorMsg", "BoardPostError");
				page = "/Error.jsp";
			}
			break;
		case "View":
			// 全ての書き込みデータを取得する
			ArrayList<BoardDataBean> allBoardListView = BoardDAO.allBoardGet(request.getParameter("ThreadId"));
			session.setAttribute("allBoardList", allBoardListView);
			session.setAttribute("secondAccess", "0");
			page = "/Board.jsp";
			break;
		}
		this.getServletContext().getRequestDispatcher(page).forward(request,response);
	}

}