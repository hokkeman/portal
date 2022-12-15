<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="portal.AccountDataBean , portal.GroupDataBean,portal.ScheduleBean , java.sql .*, javax.naming .*, javax.sql .*, java.text .* , java.net.* ,java.util.Date , java.util.*" %>
<!DOCTYPE html>

<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
</head>
<body>
<%
try{
%>
<%!
// セレクトボックスを生成するメソッド
public String select(int start , int end , int selected){
	StringBuilder sb = new StringBuilder();
	for(int i= start; i<= end; i++){
		if(selected == i){
			sb.append("<option value=" + i + " selected" + ">" + i + "</option>");
		} else {
		sb.append("<option value=" + i + ">" + i + "</option>");
		}
	}
	return new String(sb);
}
%>
<%
// クエリ情報（パラメータ名・パラメータ値）格納用のArrayListを用意
ArrayList<String> nameArray = new ArrayList<String>();
ArrayList<String> valueArray = new ArrayList<String>();

// クエリ情報の文字コードをUTF-8に変更した上で、sidとtitleとmemoを変数に代入
request.setCharacterEncoding("UTF-8");
String sid = request.getParameter("sid");
String title = request.getParameter("title");
String memo = request.getParameter("memo");

// String型で取得した日時をDate型に変換する
SimpleDateFormat dateStFormat = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat timeStFormat = new SimpleDateFormat("HH:mm:ss");
Date date = dateStFormat.parse(request.getParameter("sdate"));
Date time = timeStFormat.parse(request.getParameter("stime"));

// カレンダーにDateオブジェクトをセット
Calendar dateCalendar = Calendar.getInstance();
Calendar timeCalendar = Calendar.getInstance();
dateCalendar.setTime(date);
timeCalendar.setTime(time);

// 日時を分解して取得しそれぞれ変数に代入
int year = dateCalendar.get(Calendar.YEAR);
int month = dateCalendar.get(Calendar.MONTH)+1;
int day = dateCalendar.get(Calendar.DATE);
int hour = timeCalendar.get(Calendar.HOUR_OF_DAY);
int minute = timeCalendar.get(Calendar.MINUTE);

//セッションからログインしているユーザーと現在のグループの情報を取得
request.setCharacterEncoding("UTF-8");
GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");
AccountDataBean loginData = (AccountDataBean) session.getAttribute("LoginUser");
%>

<div class="container">
<div class = "block">
<h1 class="title">予定変更</h1>
<!-- パンくずリスト -->
<ol class="breadcrumb mb-1">
	<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> マイページ</a></li>
	<li class="breadcrumb-item"><a href="GroupPage.jsp"><%= groupData.getGroupName() %></a></li>
	<li class="breadcrumb-item"><a href="Schedule.jsp">スケジュール</a></li>
	<li class="breadcrumb-item"><a>変更</a></li>

	<!-- ログインユーザーを表示 -->
<li class="ml-auto">
	<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
		<input type="hidden" name="PID" value="Logout"/>
		<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top" title="ログアウト"><%= loginData.getUsername() %>さんがログイン中 <i class="fas fa-sign-out-alt"></i></button>
	</form>
</li>
</ol><br>


		<form method="POST" action="ScheduleServlet">
			<input type="hidden" name="PID" value="change">
			<input type="hidden" name="sid" value= "<%= sid %>"><br>
			<div class = "block" style="position: relative; width:400px;">
				<h4>予定名</h4>
				<input type="text" class="form-control" name="title" size="30" maxlength ="20" value="<%= title %>"/><br>
			</div>
			<div class = "block" style="position: relative; width:200px;">
				<h4>日付</h4>
				<input type="date" class="form-control" value="<%= request.getParameter("sdate") %>" name="Date"/><br>
				<h4>開始時間</h4>
				<input type="time" class="form-control" value="<%= request.getParameter("stime") %>" name="Time" step="1"/><br>
			</div>
			<div class = "block" style="position: relative; width:400px;">
				<h4>備考</h4>
				<input type="text" class="form-control" name="memo" size="30" maxlength ="20" value="<%= memo %>"/>
			</div>
			<br><br><br>
			<Button type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-3" onclick="return confirm('本当に予定内容を変更してよろしいですか？');">確定</button><br><br>
		</form>
		<button class="btn btn-danger btn-lg rounded-pill d-grid col-3" onclick="location.href='Schedule.jsp'">キャンセル</button>
	</div>
</div>
<%
} catch (Exception e) {
	// 不正なアクセスの場合トップ画面へリダイレクト
	response.sendRedirect("TopPage.jsp");
}
%>
</body>
</html>