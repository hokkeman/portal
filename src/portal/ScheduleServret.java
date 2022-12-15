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
 * Servlet implementation class Schedule
 */
@WebServlet("/ScheduleServlet")
public class ScheduleServret extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScheduleServret() {
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
		try {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String page = null;

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
		String calendarComp = null;
		String dateSt = null;

		// セッションからログインユーザーとグループのデータを取得
		GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");
		AccountDataBean loginData = (AccountDataBean) session.getAttribute("LoginUser");

		switch(pid) {
		case "thisMonth":
			// 今月のスケジュールを取得しセッションに保存
			ArrayList<ScheduleBean> thisMonthSchedule = new ArrayList<ScheduleBean>();
			dateSt = new SimpleDateFormat("yyyy-MM-dd").format(date);
			thisMonthSchedule = (ArrayList<ScheduleBean>)ScheduleDAO.thisMonthSchedule(groupData.getGroupid(),dateSt);
			session.setAttribute("ThisMonthSchedule", thisMonthSchedule);

			// 今月のカレンダーを作成しセッションに保存
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DATE, 1);
			calendarComp = CalenderBean.calenderMake(thisMonthSchedule, calendar);

			date = calendar.getTime();
			dateSt = new SimpleDateFormat("yyyy-MM-dd").format(date); // セッション・引数用
			session.setAttribute("Calendar", calendarComp);
			session.setAttribute("ScheduleDate",dateSt);
			page = "/Schedule.jsp";
			break;
		case "lastMonth":
			try {
				date = dateFormat.parse(request.getParameter("Date"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// Calender型に変換して昨日の日付に変更
			Calendar lastMonthCal = Calendar.getInstance();
			lastMonthCal.setTime(date);
			lastMonthCal.add(Calendar.MONTH, - 1);
			lastMonthCal.set(Calendar.DATE, 1);
			date = lastMonthCal.getTime();
			dateSt = new SimpleDateFormat("yyyy-MM-dd").format(date); // セッション・引数用

			// 今月のスケジュールを取得しセッションに保存
			ArrayList<ScheduleBean> lastMonthSchedule = new ArrayList<ScheduleBean>();
			lastMonthSchedule = (ArrayList<ScheduleBean>)ScheduleDAO.thisMonthSchedule(groupData.getGroupid(),dateSt);
			session.setAttribute("ThisMonthSchedule", lastMonthSchedule);

			// カレンダーを作成
			calendarComp = CalenderBean.calenderMake(lastMonthSchedule, lastMonthCal);
			session.setAttribute("Calendar", calendarComp);
			session.setAttribute("ScheduleDate",dateSt);
			page = "/Schedule.jsp";
			break;
		case "nextMonth":
			try {
				date = dateFormat.parse(request.getParameter("Date"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// Calender型に変換して昨日の日付に変更
			Calendar nextMonthCal = Calendar.getInstance();
			nextMonthCal.setTime(date);
			nextMonthCal.add(Calendar.MONTH, + 1);
			nextMonthCal.set(Calendar.DATE, 1);
			date = nextMonthCal.getTime();
			dateSt = new SimpleDateFormat("yyyy-MM-dd").format(date); // セッション・引数用

			// 今月のスケジュールを取得しセッションに保存
			ArrayList<ScheduleBean> nextMonthSchedule = new ArrayList<ScheduleBean>();
			nextMonthSchedule = (ArrayList<ScheduleBean>)ScheduleDAO.thisMonthSchedule(groupData.getGroupid(),dateSt);
			session.setAttribute("ThisMonthSchedule", nextMonthSchedule);

			// カレンダーを作成
			calendarComp = CalenderBean.calenderMake(nextMonthSchedule, nextMonthCal);
			session.setAttribute("Calendar", calendarComp);
			session.setAttribute("ScheduleDate",dateSt);
			page = "/Schedule.jsp";
			break;
		case "Insert":
			// リクエストから予定の日付を取得し文字列に変換
			Calendar insertCal = Calendar.getInstance();
			insertCal.set(Calendar.YEAR, Integer.parseInt(request.getParameter("Year")));
			insertCal.set(Calendar.MONTH, Integer.parseInt(request.getParameter("Month")) -1);
			insertCal.set(Calendar.DATE, Integer.parseInt(request.getParameter("Day")));
			Date insertDate = insertCal.getTime();
			String insertDateSt = dateFormat.format(insertDate);

			// 追加する予定のデータをBeansにセット
			ScheduleBean scheduleData = new ScheduleBean();
			scheduleData.setStitle(request.getParameter("Title"));
			scheduleData.setSdate(insertDateSt);
			scheduleData.setStime(request.getParameter("Time"));
			scheduleData.setMemo(request.getParameter("Memo"));
			scheduleData.setGroupid(groupData.getGroupid());
			scheduleData.setUserid(loginData.getUserid());

			page = "/ScheduleServlet";
			session.setAttribute("secondAccess", "1");
			session.setAttribute("ScheduleDate",insertDateSt);
			ScheduleDAO.insertSchedule(scheduleData);
			break;
		case "change" :
			ScheduleBean changeData = new ScheduleBean();
			changeData.setSid(request.getParameter("sid"));
			changeData.setStitle(request.getParameter("title"));
			changeData.setSdate(request.getParameter("Date"));
			changeData.setStime(request.getParameter("Time"));
			changeData.setMemo(request.getParameter("memo"));
			boolean changeOK = ScheduleDAO.changeSchedule(changeData);
			if(changeOK) {
				page = "/ScheduleServlet";
				session.setAttribute("secondAccess", "1");
			} else {
				session.setAttribute("ErrorMsg", "scheduleChangeNG");
				page = "/Error.jsp";
			}
			break;
		case "delete" :
			boolean deleteOK = ScheduleDAO.deleteSchedule(request.getParameter("sid"));
			if(deleteOK) {
				page = "/ScheduleServlet";
				session.setAttribute("secondAccess", "1");
			} else {
				session.setAttribute("ErrorMsg", "scheduleDeleteNG");
				page = "/Error.jsp";
			}
			break;
		case "View":
			String schedule = String.valueOf(session.getAttribute("ScheduleDate"));
			thisMonthSchedule = (ArrayList<ScheduleBean>)ScheduleDAO.thisMonthSchedule(groupData.getGroupid(),schedule);
			session.setAttribute("ThisMonthSchedule", thisMonthSchedule);

			// 日付を初日に変更してフォーマット（Date型）
			Date dateView = new Date();
			SimpleDateFormat dateFormatView = new SimpleDateFormat("yyyy-MM-dd");
			try {
				dateView = dateFormatView.parse(schedule);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			Calendar viewCal = Calendar.getInstance();
			viewCal.setTime(dateView);
			viewCal.set(Calendar.DATE, 1);
			dateView = viewCal.getTime();

			// String型に変換
			String firstDate = dateFormat.format(dateView);

			// カレンダーを作成しセッションに保存
			Calendar calendarView = Calendar.getInstance();
			calendarView.setTime(dateView);
			calendarView.set(Calendar.DATE, 1);
			calendarComp = CalenderBean.calenderMake(thisMonthSchedule, calendarView);
			session.setAttribute("Calendar", calendarComp);
			session.setAttribute("ScheduleDate",firstDate);

			session.setAttribute("secondAccess", "0");
			page = "/Schedule.jsp";
			break;
		}
			this.getServletContext().getRequestDispatcher(page).forward(request,response);
		} catch (Exception e) {
			this.getServletContext().getRequestDispatcher("/TopPage.jsp").forward(request,response);
		}
	}
}

