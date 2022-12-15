<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import="portal.PersonalDataBean , java.text.SimpleDateFormat , java.util.Calendar,
    portal.AccountDataBean ,portal.InfoBean , portal.GroupDataBean, java.util.* ,java.util.Date,
    java.text.*, java.time.LocalDate, java.time.temporal.ChronoUnit" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
<link rel="stylesheet" href="Personal.css">
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
	window.onload = function() {
		var popup = document.getElementById('js-popup');
		if(!popup) return;
		popup.classList.add('is-show');

		var blackBg = document.getElementById('js-black-bg');
		var closeBtn = document.getElementById('js-close-btn');

		closePopUp(blackBg);
		closePopUp(closeBtn);

		function closePopUp(elem) {
			if(!elem) return;
			elem.addEventListener('click', function() { popup.classList.remove('is-show'); } )
		}
	}
	</script>
<!--  -->
<body>

<div class = "block">
<div class="container">

<h1 class="adtitle">健康状態</h1>
<!-- パンくずリスト -->
<ol class="breadcrumb mb-1 mr-auto">
	<li class="breadcrumb-item"><a href="AdminPage.jsp"><i class="fas fa-home"></i> 管理者メニュー</a></li>
	<li class="breadcrumb-item"><a>体調管理</a></li>

	<!-- ログインユーザーを表示 -->
	<li class="ml-auto p-0">
		<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
			<input type="hidden" name="PID" value="Logout"/>
			<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top">ログアウト <i class="fas fa-sign-out-alt"></i></button>
		</form>
	</li>
</ol><br>
</div>
<%
//セッションから日付と検温データを取得
String date = String.valueOf(session.getAttribute("adminTempDate"));
ArrayList<PersonalDataBean> tempList = (ArrayList<PersonalDataBean>)session.getAttribute("tempList");

// 発熱者がいた場合ポップアップを表示
ArrayList<String> heatUser = new ArrayList<String>();
ArrayList<String> heatTemp = new ArrayList<String>();

boolean alert = false;
Double temp = null;
for(PersonalDataBean temperatureData  : tempList){
	temp = Double.parseDouble(temperatureData.getTemperature());
	if(temp >= 37.5){
		alert = true;
		heatUser.add(temperatureData.getUserName());
		heatTemp.add(temperatureData.getTemperature());
		break;
	}
}

// 時刻表示用に変換
SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
Date dateView = sdFormat.parse(date);
String dateViewSt = new SimpleDateFormat("yyyy年M月d日").format(dateView);

String todayTemp = String.valueOf(session.getAttribute("TodayTemp"));
if(alert){
	%>
	<div class="popup" id="js-popup">
		<div class="popup-inner">
	    	<div class="close-btn" id="js-close-btn"><i class="fas fa-times"></i></div>
	   		<div class = "block">
	   			<h3><%= dateViewSt %></h3>
				<h1 class="colorRed">発熱者がいます！</h1>
				<p>体温が37.5℃を超えている方が<%= heatUser.size() %>名います</p><br>
				<%
				if(heatUser.size() < 10){
				%>
				<table class="table">
					<tr><th>ユーザー名</th><th>体温</th></tr>
					<%
					for(int i = 0; i<heatUser.size(); i++){
					%>
						<tr>
							<td><%= heatUser.get(i) %></td>
							<td><%= heatTemp.get(i) %>℃</td>
						</tr>
					<%
					}
					%>
				</table>
				<%
				}
				%>
			</div>
		</div>
		<div class="black-background" id="js-black-bg"></div>
	</div>
<%
};
%>

<span style="display:inline-flex">

	<!-- 前日のデータを表示するボタン -->
	<form method="POST" action="AdminPersonalServlet">
	<input type="hidden" name="PID" value="YesterdayTemp">
	<input type="hidden" name="Date" value="<%= date %>">
		<button type="submit" class="btn" data-toggle="tooltip" data-placement="top" title="前日のデータへ">
			<i class="far fa-arrow-alt-circle-left fa-2x"></i>
		</button>
	</form>

	<!-- 日付を表示 -->
	<h3 class="mgr-100"><%= dateViewSt %></h3>
<%
	String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
	if(!date.equals(nowDate)){ %>
		<!-- 翌日のデータを表示するボタン -->
		<form method="POST" action="AdminPersonalServlet">
		<input type="hidden" name="PID" value="TomorrowTemp">
		<input type="hidden" name="Date" value="<%= date %>">
			<button type="submit" class="btn" data-toggle="tooltip" data-placement="top" title="翌日のデータへ">
				<i class="far fa-arrow-alt-circle-right fa-2x" ></i>
			</button>
		</form>
	<%} else {%>
		<!-- 表示しているデータが今日の場合翌日のデータを表示するボタンを無効化 -->
		<button type="submit" class="btn" data-toggle="tooltip" data-placement="top" title="翌日のデータへ" disabled >
				<i class="far fa-arrow-alt-circle-right fa-2x" ></i>
		</button>
<%
	}
%>
</span>
<br>
<%
if(!date.equals(nowDate)){ %>
	<!-- 今日のデータを表示するボタン -->
	<form method="POST" action="AdminPersonalServlet">
	<input type="hidden" name="PID" value="TodayTemperature">
	<button type="submit" class="btn btn-light btn-sm d-grid col-2"
	data-toggle="tooltip" data-placement="bottom" title="本日のデータへ">today</button>
	</form>
	<%
}
	%>
<br><br>
<!-- 指定された日の検温データをtable形式で表示 -->
<%
if(tempList.size() != 0){
%>
<table class="table table-hover" style="width: 800px;">
<tr class="table-light"><th>名前</th><th>ユーザーID</th><th>検温時刻</th><th>体温</th></tr>
<%
for(PersonalDataBean temperatureData  : tempList){
	temp = Double.parseDouble(temperatureData.getTemperature());
	if(temp < 37.5){
		%><tr><%
	} else {
		%><tr class="table-danger"><%
	} %>
		<td>
			<form method="POST" action="AdminPersonalServlet">
			<input type="hidden" name="PID" value="tempDetail">
			<input type="hidden" name="UserName" value="<%= temperatureData.getUserName() %>">
			<input type="hidden" name="UserId" value="<%= temperatureData.getUserid() %>">
			<button type="submit" class="btn"
			data-toggle="tooltip" data-placement="bottom" title="詳細"><%= temperatureData.getUserName() %></button>
			</form>
		</td>
		<td><%= temperatureData.getUserid() %></td>
		<td><%= temperatureData.getTtime() %></td>
		<td><%= temperatureData.getTemperature() %></td>
	</tr>
	<%
}
%>
</table>
<%
} else {
	%>
	<h3>データなし</h3>
	<%
}
%>
<br><br><br><br>
<form method="POST" action="AdminPage.jsp">
	<input type="hidden" name="PID" value="TodayTemperature">
	<button class="btn btn-danger btn-lg rounded-pill d-grid col-3">戻る</button>
</form>
</div>
<%
} catch (Exception e) {
	// 不正なアクセスの場合トップ画面へリダイレクト
	response.sendRedirect("TopPage.jsp");
}
%>
</body>
</html>