<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
import="portal.AccountDataBean , portal.GroupDataBean,portal.ScheduleBean , java.sql .*, javax.naming .*, javax.sql .*, java.text .* , java.net.* ,java.util.Date , java.util.*" %>
<jsp:useBean id="allSchedule" class="java.util.ArrayList" scope="request" />
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
</head>
<%
try{
%>
<script>
$(function () {
  $('[data-toggle="tooltip"]').tooltip()
})
</script>
<!-- モーダルウィンドウ用js -->
	<script>
	$(function(){
	    $('.js-modal-open').each(function(){
	        $(this).on('click',function(){
	            var target = $(this).data('target');
	            var modal = document.getElementById(target);
	            $(modal).fadeIn();
	            return false;
	        });
	    });
	    $('.js-modal-close').on('click',function(){
	        $('.js-modal').fadeOut();
	        return false;
	    });
	});
	</script>
<!--  -->
<body>
<%
// セッションからカレンダーと予定の情報を取得
String calendar = String.valueOf(session.getAttribute("Calendar"));
ArrayList<ScheduleBean> thisMonthSchedule = new ArrayList<ScheduleBean>();
thisMonthSchedule = (ArrayList<ScheduleBean>)session.getAttribute("ThisMonthSchedule");
String date = String.valueOf(session.getAttribute("ScheduleDate"));

// セッションからログインしているユーザーと現在のグループの情報を取得
request.setCharacterEncoding("UTF-8");
GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");
AccountDataBean loginData = (AccountDataBean) session.getAttribute("LoginUser");
%>
<div class="container">
<div class = "block">
<h1 class="title">スケジュール</h1>
</div>

<!-- パンくずリスト -->
<ol class="breadcrumb mb-1">
	<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> マイページ</a></li>
	<li class="breadcrumb-item"><a href="GroupPage.jsp"><%= groupData.getGroupName() %></a></li>
	<li class="breadcrumb-item"><a>スケジュール</a></li>

	<!-- ログインユーザーを表示 -->
<li class="ml-auto">
	<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
		<input type="hidden" name="PID" value="Logout"/>
		<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top" title="ログアウト"><%= loginData.getUsername() %>さんがログイン中 <i class="fas fa-sign-out-alt"></i></button>
	</form>
</li>
</ol><br>
<%
// 年月比較用にフォーマット
SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
Date dateComparison = sdFormat.parse(date);
String dateComparisonSt = sdFormat.format(dateComparison);

// 年月表示用にフォーマット
SimpleDateFormat sdFormatView = new SimpleDateFormat("yyyy年M月");
Date dateView = sdFormat.parse(date);
String dateViewSt = sdFormatView.format(dateView); // 表示用
%>
<div class = "block">
<span style="display:inline-flex">
	<!-- 先月のデータを表示するボタン -->
	<form method="POST" action="ScheduleServlet">
	<input type="hidden" name="PID" value="lastMonth">
	<input type="hidden" name="Date" value="<%= date %>">
		<button type="submit" class="btn" data-toggle="tooltip" data-placement="top" title="先月のデータへ">
			<i class="far fa-arrow-alt-circle-left fa-2x"></i>
		</button>
	</form>

	<!-- 表示中のカレンダーの年月を取得 -->
	<h3 class="mgr-100 pt-2"><%= dateViewSt %></h3>
	<%
	String nowMonth = new SimpleDateFormat("yyyy-MM-01").format(Calendar.getInstance().getTime());
	%>
		<!-- 翌月のデータを表示するボタン -->
		<form method="POST" action="ScheduleServlet">
		<input type="hidden" name="PID" value="nextMonth">
		<input type="hidden" name="Date" value="<%= date %>">
			<button type="submit" class="btn" data-toggle="tooltip" data-placement="top" title="翌月のデータへ">
				<i class="far fa-arrow-alt-circle-right fa-2x" ></i>
			</button>
		</form>
</span>
<br>
<%
	if(!dateComparisonSt.equals(nowMonth)){ %>
		<!-- 今月のデータを表示するボタン -->
		<form method="POST" action="ScheduleServlet">
		<input type="hidden" name="PID" value="thisMonth"/>
		<button type="submit" class="btn btn-light btn-sm d-grid col-2"
		data-toggle="tooltip" data-placement="bottom" title="今月のデータへ">this month</button>
		</form>
<%	} else {
		%><br><%
	}%>
<!-- カレンダーを表示 -->
<%= session.getAttribute("Calendar") %>

<%
if(thisMonthSchedule.size() !=0){
%>
<table class="table table-bordered"">
<tr>
<th>日付</th><th>時刻</th><th>予定名</th><th style="width: 300px;">備考</th><th>投稿者</th><th>変更</th><th>削除</th>
</tr>
<%
//フォーマット書式を定義
SimpleDateFormat dateStFormat = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat timeStFormat = new SimpleDateFormat("HH:mm:ss");
SimpleDateFormat timeFormat = new SimpleDateFormat("H時mm分");

for(ScheduleBean schedule : thisMonthSchedule){
	Date sdate = new Date();
	Date stime = new Date();
	Calendar scheduleDate = Calendar.getInstance();
	Calendar scheduleTime = Calendar.getInstance();
	try {
		sdate = dateStFormat.parse(schedule.getSdate());
		stime = timeStFormat.parse(schedule.getStime());
	} catch (ParseException e) {
		e.printStackTrace();
	}
	scheduleDate.setTime(sdate);
	scheduleTime.setTime(stime);

	// 予定のある日付を取得
	String day = String.valueOf(scheduleDate.get(Calendar.DATE));
	String time = timeFormat.format(scheduleTime.getTime());
%>
	<tr>
		<td><%= day %>日</td>
		<td><%= time %></td>
		<td><%= schedule.getStitle()%></td>
		<td><%= schedule.getMemo()%></td>
		<td><%= schedule.getUsername()%></td>
		<%
		if(schedule.getUserid().equals(loginData.getUserid()) || loginData.getUserid().equals(groupData.getUserid())){%>
		<td>
			<form method="POST" action="ScheduleChange.jsp">
				<input type="hidden" name="sid" value="<%= schedule.getSid() %>">
				<input type="hidden" name="sdate" value="<%= schedule.getSdate() %>">
				<input type="hidden" name="stime" value="<%= schedule.getStime() %>">
				<input type="hidden" name="title" value="<%= schedule.getStitle() %>">
				<input type="hidden" name="memo" value="<%= schedule.getMemo() %>">
				<button type="submit" class="btn btn-light btn-sm"><i class="fas fa-pencil-alt"></i></button>
			</form>
		</td>
		<td>
			<form method="POST" action="ScheduleServlet" onclick="return confirm('本当に予定内容を削除してよろしいですか？');">
				<input type="hidden" name="PID" value="delete">
				<input type="hidden" name="sid" value= "<%= schedule.getSid() %>">
				<Button type="submit" class="btn btn-light btn-sm"><i class="fas fa-trash-alt"></i></button>
			</form>
		</td>
	</tr>
	<% } else { %>
			<td>　</td><td>　</td>
	<% }
	} %>
</table>
<%
} else {
	%>
	<br><h3>予定はありません</h3>
	<%
}
%>
<br><br>
<button type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" onclick="location.href='GroupPage.jsp'">戻る</button><br>
<br><br>
</div>
</div>

<%
} catch (Exception e){
	response.sendRedirect("TopPage.jsp");
}
%>
</body>
</html>