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
 * Servlet implementation class UserPageServlet
 */
@WebServlet("/UserPageServlet")
public class UserPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserPageServlet() {
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
				session.setAttribute("ErrorMsg", "SessionError");
				page = "/Error.jsp";
			}

			// ログイン済みの場合実行
			if(loginOK) {
				String pid = request.getParameter("PID");

				// ログインユーザーの情報を取得
				AccountDataBean loginUser = (AccountDataBean)session.getAttribute("LoginUser");
				String userId = loginUser.getUserid();

				// ログインユーザーの所属グループデータを取得
				ArrayList<GroupDataBean>affiliationList = GroupDAO.affiliationData(loginUser);
				session.setAttribute("AffiliationList", affiliationList); // 所属するグループのデータをセッションに保存

				page = "/UserPage.jsp";
			}
			this.getServletContext().getRequestDispatcher(page).forward(request,response);
		} catch(Exception e) {
			this.getServletContext().getRequestDispatcher("/TopPage.jsp").forward(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = null;
		HttpSession session = request.getSession();

		// ログイン済みか確認
		boolean loginOK = false;
		if(session.getAttribute("LoginUser") != null) {
			loginOK = true;
		} else {
			session.setAttribute("ErrorMsg", "SessionError");
			page = "/Error.jsp";
		}

		// ログイン済みの場合実行
		if(loginOK) {
			// 押されたボタンの種類を判定
			String pid = request.getParameter("PID");
			try {
				if(session.getAttribute("secondAccess").equals("1") || pid == null) {
					pid = "View";
				}
			} catch (Exception e) {
			}

			// ログインユーザーの情報を取得
			AccountDataBean loginUser = (AccountDataBean)session.getAttribute("LoginUser");
			String userId = loginUser.getUserid();

			// 今日の日付を取得
			String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

			// セッションから取得するログインユーザーの検温データ用
			Object personalDataList = null;

			// 平常時（37.5℃以下）の体温の平均値
			String average = null;

			switch(pid) {
				case "PersonalData":
					// 今日の検温データがあるかどうか確認
					boolean todayDataOK = PersonalDAO.todayPersonalChack(userId, nowDate);
					session.setAttribute("todayData" , todayDataOK);

					// 遷移先を指定
					page = "/Personal.jsp";
					break;
				case "TodayTemperature":
					// 今日の検温データを投稿
					PersonalDataBean personalData = new PersonalDataBean();
					personalData.setUserid(userId);
					personalData.setTemperature(request.getParameter("Temperature"));
					personalData.setTdate(nowDate);
					personalData.setTtime(request.getParameter("Time"));
					PersonalDAO.insertTemperature(personalData);

					 // 今日の検温データが記録済みであることを示すフラグを立てる
					session.setAttribute("TodayTemp", "OK");

					page = "/Personal.jsp";
					break;
				case "PersonalEdit":
					// 検温データを投稿
					PersonalDataBean personalDataEdit = new PersonalDataBean();
					personalDataEdit.setUserid(userId);
					personalDataEdit.setTemperature(request.getParameter("Temperature"));
					personalDataEdit.setTdate(request.getParameter("Date"));
					personalDataEdit.setTtime(request.getParameter("Time"));
					PersonalDAO.insertTemperature(personalDataEdit);

					page = "/Personal.jsp";
					break;
				case "View":
				AccountDataBean loginUserData = (AccountDataBean)session.getAttribute("LoginUser");
					// ログインユーザーの所属グループデータを取得
					ArrayList<GroupDataBean> affiliationList = GroupDAO.affiliationData(loginUserData);
					session.setAttribute("AffiliationList", affiliationList); // 所属するグループのデータをセッションに保存
					session.setAttribute("secondAccess", "0");
					page = "/UserPage.jsp";
					break;
			}
			// ログインユーザーの過去3週間の検温データを取得しセッションに保存
			personalDataList = PersonalDAO.personalData(userId);
			session.setAttribute("personalDataList" , personalDataList);

			// ログインユーザーの平常時（37.5℃以下）の体温の平均値を取得しセッションに保存
			average = PersonalDAO.temperatureAverage(userId);
			session.setAttribute("average" , average);
		}
		this.getServletContext().getRequestDispatcher(page).forward(request,response);
	}

}
