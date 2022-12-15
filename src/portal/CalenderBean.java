package portal;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalenderBean implements Serializable {

	/**
	 * カレンダーを作成するメソッド
	 * @param scheduleList
	 * 作成したい月のスケジュールの情報（ScheduleBeanクラス型）が格納されたArrayList
	 * @param cal
	 * 作成したいカレンダーの月を示す変数（Calendar型）
	 * @return
	 * 作成されたカレンダーを返す（htmlコード）
	 */
	public static String calenderMake(ArrayList<ScheduleBean> scheduleList, Calendar cal) {
		// 作成するカレンダーの年月を決定
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;

		// フォーマット書式を定義
		SimpleDateFormat dateStFormat = new SimpleDateFormat("yyyy-MM-dd");

		// 取得しているスケジュールの予定日を取得
		ArrayList<Integer> scheduleDays = new ArrayList<Integer>();
		if(scheduleList.size() != 0){
			for(ScheduleBean schedule : scheduleList){
				// sdateの情報をCalendar型に変換
				Date date = new Date();
				Calendar scheduleDate = Calendar.getInstance();
				try {
					date = dateStFormat.parse(schedule.getSdate());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				scheduleDate.setTime(date);

				// 予定のある日付をArrayListに追加
				int day = scheduleDate.get(Calendar.DATE);
				scheduleDays.add(day);
			}
		}

		// 1日の曜日を算出
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		// 今月が何日までかを確認する
		int monthLastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// うるう年の時は2月の最終日を変更
		if(month == 2){
			monthLastDay = 28;
			if(year>400 && year%400 == 0 || year%100 != 0 && year%4 == 0){
				monthLastDay = 29;
			}
		}

		//カレンダー生成
		StringBuilder sb = new StringBuilder();
		sb.append("<table class='table table-bordered schedule' style='width: 420px; height:330px'>");
		//表の一行目
		sb.append("<tr width='14.28%'>");
		sb.append("<th width='14.28%'>Sun</th>"); // 見出し
		sb.append("<th width='14.28%'>Mon</th>"); // 見出し
		sb.append("<th width='14.28%'>Tue</th>"); // 見出し
		sb.append("<th width='14.28%'>Wed</th>"); // 見出し
		sb.append("<th width='14.28%'>Thu</th>"); // 見出し
		sb.append("<th width='14.28%'>Fri</th>"); // 見出し
		sb.append("<th width='14.28%'>Sat</th>"); // 見出し
		sb.append("</tr>");
		// 表の二行目に空欄を入れる
		sb.append("<tr align='center'>");
		for(int i = 1; i<dayOfWeek; i++){
			sb.append("<td>　</td>");
		}
		// 表の二行目に日付を入れる
		int calenderDay = 1;
		for(int i = dayOfWeek; i<=7; i++){
			// 予定がある日付の場合は背景色を赤にする
			if(scheduleDays.contains(calenderDay)){
				sb.append("<td bgcolor='#ffe4e1'>");
			} else {
				sb.append("<td>");
			}
			if(i == 0){ // 日曜日の場合は文字の色を赤にする
				sb.append("<div class='content'>");
				sb.append("<button type='button' class='js-modal-open btn btn-sm text-danger' data-target='"+ calenderDay +"' bgcolor='#ffe4e1'>"+ calenderDay +"</button>");
			} else if (i < 7){
			sb.append("<div class='content'>");
				sb.append("<button type='button' class='js-modal-open btn btn-sm' data-target='"+ calenderDay +"' bgcolor='#ffe4e1'>"+ calenderDay +"</button>");
			} else { // 土曜日の場合は文字の色を青にする
				sb.append("<div class='content'>");
				sb.append("<button type='button' class='js-modal-open btn btn-sm text-primary' data-target='"+ calenderDay +"' bgcolor='#ffe4e1'>"+ calenderDay +"</button>");
			}

			// モーダルの内部処理
			sb.append(calendarTable(year, month, calenderDay));

			calenderDay++;
		}
		sb.append("</tr>");

		// 表の三行目以降の処理（最終日まで表が完成するまでループ）
		boolean monthEnd = false;
		while(!monthEnd){
			sb.append("<tr align='center'>");
			for(int i = 0; i<=6; i++){
				if(monthEnd){
					sb.append("<td>" + "　" + "</td>");
				} else {
					// 予定がある日付の場合は背景色を赤にする
					if(scheduleDays.contains(calenderDay)){
						sb.append("<td bgcolor='#ffe4e1'>");
					} else {
						sb.append("<td>");
					}
					if(i == 0){ // 日曜日の場合は文字の色を赤にする
						sb.append("<div class='content'>");
						sb.append("<button type='button' class='js-modal-open btn btn-sm text-danger' data-target='"+ calenderDay +"' bgcolor='#ffe4e1'>"+ calenderDay +"</button>");
					} else if (i < 6){
						sb.append("<div class='content'>");
						sb.append("<button type='button' class='js-modal-open btn btn-sm' data-target='"+ calenderDay +"' bgcolor='#ffe4e1'>"+ calenderDay +"</button>");
					} else { // 土曜日の場合は文字の色を青にする
						sb.append("<div class='content'>");
						sb.append("<button type='button' class='js-modal-open btn btn-sm text-primary' data-target='"+ calenderDay +"' bgcolor='#ffe4e1'>"+ calenderDay +"</button>");
					}
					// モーダルの内部処理
					sb.append(calendarTable(year, month, calenderDay));
					if(calenderDay == monthLastDay){
						monthEnd = true;
					}
				}
					calenderDay++;
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		 // 完成したカレンダーをString型に変換した上で返す
		String calendarComp = sb.toString();
		return calendarComp;
	}

	//
	/**
	 * 特定の日付に予定を追加するモーダルウィンドウをテーブル内に仕込むメソッド
	 * @param year
	 * 追加する予定の年
	 * @param month
	 * 追加する予定の月
	 * @param calenderDay
	 * 追加する予定の日付
	 * @return
	 * 完成したモーダルウィンドウを返す（htmlコード）
	 */
	public static String calendarTable(int year, int month, int calenderDay){
		StringBuilder table = new StringBuilder();

		// モーダルの内部処理
		table.append("<div id='"+ calenderDay +"' class='modal js-modal'>");
		table.append("<div class='modal__bg js-modal-close'></div>");
		table.append("<div class='modal__content'>");
		table.append("<div class = 'block'>");
		table.append("<h1 class='title2'>予定追加</h1>");
		table.append("<h3>" + year + "年" + month + "月" + calenderDay + "日</h3>");

		table.append("<form method='POST' action='ScheduleServlet'>");
		table.append("<input type='hidden' name='PID' value='Insert'>");
		table.append("<input type='hidden' name='Year' value='" + year + "'>");
		table.append("<input type='hidden' name='Month' value='" + month + "'>");
		table.append("<input type='hidden' name='Day' value='" + calenderDay + "'>");
		table.append("<br>");

		table.append("<div class = 'block' style='position: relative; width:500px;'>");
		table.append("<h5>予定名</h5>");
		table.append("<input type='text' class='form-control' name='Title' size='30' maxlength ='30' required/><br><br>");
		table.append("</div>");

		table.append("<div class = 'block' style='position: relative; width:250px;'>");
		table.append("<h5>開始時間</h5>");
		table.append("<input type='time' class='form-control' name='Time' required/><br><br>");
		table.append("</div>");

		table.append("<div class = 'block' style='position: relative; width:500px;'>");
		table.append("<h5>備考</h5>");
		table.append("<input type='text' class='form-control' name='Memo' size='30' maxlength ='50' /><br><br>");
		table.append("</div>");

		table.append("<input type='submit' class='btn btn-primary btn-lg rounded-pill d-grid col-4' value='追加'/><br><br>");
		table.append("</form>");

		table.append("<button type='button' class='js-modal-close btn btn-danger btn-lg rounded-pill d-grid col-4'>閉じる</button>");
		table.append("</div>");
		table.append("</div>");
		table.append("</td>");
		String tableComp = table.toString();
		return tableComp;
	}
}
