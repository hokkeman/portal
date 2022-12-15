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
 * Servlet implementation class InfoPageServlet
 */
@WebServlet("/InfoPageServlet")
public class InfoPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InfoPageServlet() {
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
			request.setCharacterEncoding("UTF-8");
			GroupDataBean groupDataGet;
			if(session.getAttribute("LoginUser") != null || session.getAttribute("GroupData") != null) {
				// セッションから現在のグループの情報を取得
				groupDataGet = (GroupDataBean)session.getAttribute("GroupData");
				ArrayList<InfoBean> allInfo = new ArrayList<InfoBean>();
				allInfo = InfoDAO.allInfo(groupDataGet.getGroupid());
				session.setAttribute("AllInfo" , allInfo);
				page = "/Info.jsp";
			} else {
				this.getServletContext().getRequestDispatcher("/TopPage.jsp").forward(request,response);
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
			page = "/SessionError.jsp";
		}

		// ログイン済みの場合実行
		if(loginOK) {
			String pid = request.getParameter("PID");
			try {
				if(session.getAttribute("secondAccess").equals("1") || pid == null) {
					pid = "View";
				}
			} catch (Exception e) {
			}

			// セッションから現在のグループの情報を取得
			GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");

			switch(pid) {
				case "Info":
					ArrayList<InfoBean> allInfo = new ArrayList<InfoBean>();
					allInfo = InfoDAO.allInfo(groupData.getGroupid());
					session.setAttribute("AllInfo" , allInfo);
					page = "/Info.jsp";
					break;
				case "InfoPost":
					GroupDataBean groupDataPost = (GroupDataBean)session.getAttribute("GroupData");
					InfoBean infoPostBean = new InfoBean();
					AccountDataBean loginUser = (AccountDataBean)session.getAttribute("LoginUser");

					// リクエストから投稿内容の情報を取得し連絡事項投稿用のInfoBeanにセット
					infoPostBean.setGroupId(groupDataPost.getGroupid());
					infoPostBean.setInfoTitle(request.getParameter("infotitle"));
					infoPostBean.setInfoText(request.getParameter("infotext"));
					infoPostBean.setUserId(loginUser.getUserid());
					infoPostBean.setUserName(loginUser.getUsername());


					// 現在時刻を取得し連絡事項投稿用のInfoBeanにセット
					String nowDatePost = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
					String nowTimePost = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
					infoPostBean.setInfoDate(nowDatePost);
					infoPostBean.setInfoTime(nowTimePost);

					// InfoDAOのinfoPostメソッドを呼び出しデータベースへ連絡事項を投稿
					if(InfoDAO.infoPost(infoPostBean) != 0) {
						session.setAttribute("OkMsg", "infoPostOK");
						page = "/InfoResult.jsp";
					} else {
						session.setAttribute("ErrorMsg", "infoPostError");
						page = "/Error.jsp";
					}
					break;
				case "InfoChange":
					InfoBean infoChangeBean = new InfoBean();
					// リクエストから変更内容の情報を取得し連絡事項変更用のInfoBeanにセット
					infoChangeBean.setInfoTitle(request.getParameter("infotitle"));
					infoChangeBean.setInfoText(request.getParameter("infotext"));
					infoChangeBean.setInfoId(request.getParameter("infoid"));

					// 現在時刻を取得し連絡事項投稿用のInfoBeanにセット
					String nowDateChange = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
					String nowTimeChange = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
					infoChangeBean.setEditDate(nowDateChange);
					infoChangeBean.setEditTime(nowTimeChange);

					// InfoDAOのinfoChangeメソッドを呼び出しデータベースへ編集内容を投稿
					if(InfoDAO.infoChange(infoChangeBean) != 0) {
						session.setAttribute("OkMsg", "infoChangeOK");
						page = "/InfoResult.jsp";
					} else {
						session.setAttribute("ErrorMsg", "infoChangeError");
						page = "/Error.jsp";
					}
					break;
				case "InfoDelete":
					InfoBean infoDeleteBean = new InfoBean();
					infoDeleteBean.setInfoId(request.getParameter("infoid"));

					// InfoDAOのinfoDeleteメソッドを呼び出し連絡事項を削除
					if(InfoDAO.infoDelete(infoDeleteBean) != 0) {
						session.setAttribute("OkMsg", "infoDeleteOK");
						page = "/InfoResult.jsp";
					} else {
						session.setAttribute("ErrorMsg", "infoDeleteError");
						page = "/Error.jsp";
					}
					break;
				case "View":
					ArrayList<InfoBean> allInfoView = new ArrayList<InfoBean>();
					allInfoView = InfoDAO.allInfo(groupData.getGroupid());
					session.setAttribute("AllInfo" , allInfoView);
					page = "/Info.jsp";
					break;
			}
			this.getServletContext().getRequestDispatcher(page).forward(request,response);
		}
	}
}
