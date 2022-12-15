package portal;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AdminPersonal
 */
@WebServlet("/AdminPersonalServlet")
public class AdminPersonalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminPersonalServlet() {
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
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		switch(pid){
		case "TodayTemperature":
			// 現在時刻を取得
			String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

			ArrayList<PersonalDataBean> allTempList = (ArrayList<PersonalDataBean>)AdminPageDAO.pickTemperature(todayDate);
			session.setAttribute("adminTempDate", todayDate);
			session.setAttribute("tempList", allTempList);
			page = "/AdminPersonal.jsp";
			break;
		case "YesterdayTemp":
			// リクエストから日付を取得しDate型へ変換
			try {
				date = dateFormat.parse(request.getParameter("Date"));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// Calender型に変換して昨日の日付に変更
			Calendar yesterdayCal = Calendar.getInstance();
			yesterdayCal.setTime(date);
			yesterdayCal.add(Calendar.DATE, - 1);

			// Date型を経由してString型に変換
			String yesterday = new SimpleDateFormat("yyyy-MM-dd").format(yesterdayCal.getTime());

			// 前日の検温データを全て取得
			ArrayList<PersonalDataBean> yesterdayTempList = (ArrayList<PersonalDataBean>)AdminPageDAO.pickTemperature(yesterday);

			// セッションに必要データをセットし遷移先を決定
			session.setAttribute("adminTempDate", yesterday);
			session.setAttribute("tempList", yesterdayTempList);
			page = "/AdminPersonal.jsp";
			break;
		case "TomorrowTemp":
			// リクエストから日付を取得しDate型へ変換
			try {
				date = dateFormat.parse(request.getParameter("Date"));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// Calender型に変換して昨日の日付に変更
			Calendar tomorrowCal = Calendar.getInstance();
			tomorrowCal.setTime(date);
			tomorrowCal.add(Calendar.DATE, + 1);

			// Date型を経由してString型に変換
			String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(tomorrowCal.getTime());

			// 前日の検温データを全て取得
			ArrayList<PersonalDataBean> tomorrowTempList = (ArrayList<PersonalDataBean>)AdminPageDAO.pickTemperature(tomorrow);

			// セッションに必要データをセットし遷移先を決定
			session.setAttribute("adminTempDate", tomorrow);
			session.setAttribute("tempList", tomorrowTempList);
			page = "/AdminPersonal.jsp";
			break;
		case "tempDetail":
			// 選択されたユーザーのuseridをBeansにセットしセッションに保存
			AccountDataBean pickUserGroupData = new AccountDataBean();
			pickUserGroupData.setUserid(request.getParameter("UserId"));
			pickUserGroupData.setUsername(request.getParameter("UserName"));
			session.setAttribute("UserData", pickUserGroupData);

			// 選択されたユーザーの所属するグループの情報を取得しセッションに保存
			ArrayList<GroupDataBean> groupDataList = (ArrayList<GroupDataBean>) GroupDAO.affiliationData(pickUserGroupData);
			session.setAttribute("adUserGroupData", groupDataList);

			// ユーザーの過去3週間の検温データを取得しセッションに保存
			Object personalDataList = PersonalDAO.personalData(request.getParameter("UserId"));
			session.setAttribute("personalDataList" , personalDataList);

			// ユーザーの平常時（37.5℃以下）の体温の平均値を取得しセッションに保存
			String average = PersonalDAO.temperatureAverage(request.getParameter("UserId"));
			session.setAttribute("average" , average);

			// 遷移先を決定
			page = "/AdminPersonalDetail.jsp";
			break;
		case "View":

			break;
	}
	this.getServletContext().getRequestDispatcher(page).forward(request,response);
	}

}
