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
try{
%>
<div class="container">
<%

// セッションからユーザーの情報と過去3週間の検温情報のリストを取得する
ArrayList<PersonalDataBean> personalDataList = new ArrayList<PersonalDataBean>();
personalDataList = (ArrayList<PersonalDataBean>) session.getAttribute("personalDataList");
AccountDataBean userData = (AccountDataBean)session.getAttribute("UserData");

// 現在時刻を取得
Date date = new Date();
Calendar calenderNow = Calendar.getInstance();
calenderNow.add(Calendar.DATE, - 22);
%>
<div class = "block">
<h1 class="adtitle"><%= userData.getUsername() %>さんの体調詳細</h1>
</div>
<!-- パンくずリスト -->
<ol class="breadcrumb mb-1 mr-auto">
	<li class="breadcrumb-item"><a href="AdminPage.jsp"><i class="fas fa-home"></i> 管理者メニュー</a></li>
	<li class="breadcrumb-item"><a href="AdminPersonal.jsp">体調管理</a></li>
	<li class="breadcrumb-item"><a>詳細</a></li>

	<!-- ログインユーザーを表示 -->
	<li class="ml-auto p-0">
		<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
			<input type="hidden" name="PID" value="Logout"/>
			<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top">ログアウト <i class="fas fa-sign-out-alt"></i></button>
		</form>
	</li>
</ol><br>
<div class = "block">
<h3>直近3週間の体温</h3>
</div>
<div class="chart-container">
<canvas id="myChart" class="chart"></canvas>
</div>

<script>
var ctx = document.getElementById('myChart').getContext('2d');
var myChart = new Chart(ctx, {
	type: 'line',
	data:{
		labels: [
			<%
				ArrayList<Date> dateList = new ArrayList<Date>();
				// 表の下部に3週間分の日付を表示
				for(int i = 0; i<22; i++){
					Date dateNow = new Date();
					calenderNow.add(Calendar.DATE, + 1);
					int month = calenderNow.get(Calendar.MONTH) + 1;
					int day = calenderNow.get(Calendar.DATE);
					dateNow = calenderNow.getTime();
					dateList.add(dateNow);
					%>'<%= month %>/<%= day %>'<%
					if(i != 22){
						%>,<%
					}
				}
			%>
			],
		datasets: [{
			label: '体温',
			data: [
				<%
				boolean existence = false;
				String temp = null;
				for(int i = 0; i<22; i++){
					for(PersonalDataBean personalData : personalDataList){
						String tempDate = new SimpleDateFormat("yyyy-MM-dd").format(dateList.get(i));
						if(tempDate.equals(personalData.getTdate())){
							existence = true;
							temp = personalData.getTemperature();
							break;
						} else {
							existence = false;
						}
					}
					if(existence){
						%><%= temp %><%
					} else {
						%>　<%
					}
					if(i != 22){
						%>,<%
					}
				}
				%>
				],
				backgroundColor: "rgba(238, 130, 238,0.4)"
    		}]
		},
		options: {
			scales: {
				xAxes: [{id:'xAxis'}],
				yAxes: [
					{
						id:'yAxis',
						ticks:{
							beginAtZero: true,
							min: 35.0,
							max: 40.0
						}
					}
				]
			},
			// ここから横線の記述
			annotation: {
				annotations: [
					{
						type: 'line', // 線分を指定
						drawTime: 'afterDatasetsDraw',
						id: 'a-line-1', // 線のid名を指定（他の線と区別するため）
						mode: 'horizontal', // 水平を指定
						scaleID: 'yAxis', // 基準とする軸のid名
						value: <%= session.getAttribute("average") %>, // 引きたい線の数値（始点）
						endValue: <%= session.getAttribute("average") %>, // 引きたい線の数値（終点）
						borderColor: 'green', // 線の色
						borderWidth: 2, // 線の幅（太さ）
						borderDash: [2, 2],
						borderDashOffset: 1,
						label: { // ラベルの設定
							backgroundColor: 'rgba(255,255,255,0.8)',
							bordercolor: 'rgba(200,60,60,0.8)',
							borderwidth: 2,
							fontSize: 10,
							fontStyle: 'bold',
							fontColor: 'rgba(200,60,60,0.8)',
							xPadding: 10,
							yPadding: 10,
							cornerRadius: 3,
							position: 'left',
							xAdjust: 0,
							yAdjust: 0,
							enabled: true,
							content: '平均体温<%= session.getAttribute("average") %>℃'
						}
					},// ここまで横線の記述
				// ここからボックスの記述
			    	{
						type: 'box', // 矩形を指定
						drawTime: 'afterDatasetsDraw',
						id: 'a-box-1', // 矩形のid名を指定（他と区別するため）
						xScaleID: 'xAxis', // 基準とするx軸のid名
						yScaleID: 'yAxis',// 基準とするy軸のid名
						xMin: 0, // 基準とするx軸の値（始点）
						xMax: 22, // 基準とするx軸の値（終点）
						yMin: 37.5, // 基準とするy軸の値（始点）
						yMax: 38.25, // 基準とするy軸の値（終点）
						backgroundColor: 'rgba(178,34,34,.05)',
						borderWidth: 0,
						borderColor: 'rgba(255,0,0,0)'
					},
			    	{
						type: 'box', // 矩形を指定
						drawTime: 'afterDatasetsDraw',
						id: 'a-box-2', // 矩形のid名を指定（他と区別するため）
						xScaleID: 'xAxis', // 基準とするx軸のid名
						yScaleID: 'yAxis',// 基準とするy軸のid名
						xMin: 0, // 基準とするx軸の値（始点）
						xMax: 22, // 基準とするx軸の値（終点）
						yMin: 38.25, // 基準とするy軸の値（始点）
						yMax: 39.0, // 基準とするy軸の値（終点）
						backgroundColor: 'rgba(178,34,34,.1)',
						borderWidth: 0,
						borderColor: 'rgba(255,0,0,0)'
					},
			    	{
						type: 'box', // 矩形を指定
						drawTime: 'afterDatasetsDraw',
						id: 'a-box-3', // 矩形のid名を指定（他と区別するため）
						xScaleID: 'xAxis', // 基準とするx軸のid名
						yScaleID: 'yAxis',// 基準とするy軸のid名
						xMin: 0, // 基準とするx軸の値（始点）
						xMax: 22, // 基準とするx軸の値（終点）
						yMin: 39, // 基準とするy軸の値（始点）
						yMax: 40.0, // 基準とするy軸の値（終点）
						backgroundColor: 'rgba(178,34,34,.2)',
						borderWidth: 0,
						borderColor: 'rgba(255,0,0,0)'
					}
				] // ここまでボックスの記述
			}
		}
	}
);
</script>
<br>
</div>
<div class = "block">
<table class="table" style="width: 800px;">
<tr class="table-light"><th>検温日</th><th>検温時刻</th><th>体温</th></tr>
<%
String tableDateView = null;
String tableTimeView = null;
String tableTemparature = null;
Date personalDate = new Date();
String day = null;
boolean dataExistence = false;
for(Date tableDate  : dateList){
	String tableDateSt = new SimpleDateFormat("yyyy-MM-dd").format(tableDate); // データベースとの比較用にフォーマット
	tableDateView = new SimpleDateFormat("M/d").format(tableDate); // テーブル内に表示用の表記にフォーマット
	%> <tr><td> <%= tableDateView %> </td> <%
	for(PersonalDataBean personalData : personalDataList){
		if(tableDateSt.equals(personalData.getTdate())){
			String time = personalData.getTtime();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			personalDate = sdf.parse(time);
			tableTimeView = new SimpleDateFormat("H時mm分").format(personalDate);
			tableTemparature = personalData.getTemperature();
			dataExistence = true;
			break;
		} else {
			dataExistence = false;
		}
	}
	if(dataExistence){
		%>
		<td><%= tableTimeView %></td>
		<%
			// 発熱の場合背景を赤くする
			Double tempDouble = Double.parseDouble(tableTemparature);
			if(tempDouble>37.5){
				%><td class="table-danger"><%= tableTemparature %>℃ </td><%
			} else {
				%><td><%= tableTemparature %>℃ </td><%
			}
		%>
		</tr>
		<%
	} else {
		String modalDate = new SimpleDateFormat("M月d日").format(tableDate);
		%>
		<td>---</td>
		<td>---</td>
		</tr>
		<%
	}
}
%>
</table>
<br><br>
<!-- このユーザーの詳細をモーダルウィンドウに表示 -->
<%
// ユーザーの所属グループをセッションから取得
ArrayList<GroupDataBean> userGroupList = (ArrayList<GroupDataBean>)session.getAttribute("adUserGroupData");
%>
<div class="content">
<!-- モーダルウィンドウを開くボタン -->
<button type="button" class="js-modal-open btn btn-primary btn-lg rounded-pill d-grid col-3" data-target="modal">ユーザー情報</button>
</div>
	<div id="modal" class="modal js-modal">
	<div class="modal__bg js-modal-close"></div>
		<div class="modal__content">
			<h2>ユーザー情報</h2>
			<div class="block">
			<table class="table table-bordered" style="width: 300px;" >
			<tr class="table-active"><th>名前</th><th>ID</th></tr>
			<tr><td><%= userData.getUsername() %></td><td><%= userData.getUserid() %></td></tr>
			</table>
			</div>
			<br><br>
			<h2>所属</h2>
			<table class="table table-bordered">
			<tr class="table-active"><th>グループ名</th><th>グループリーダー</th></tr>
			<%
			for (GroupDataBean userGroup : userGroupList){
				%>
				<tr>
				<td><%= userGroup.getGroupName() %></td>
				<td><%= userGroup.getUsername() %></td>
				</tr>
				<%
			}
			%>
			</table>
			<br>
			<button type="button" class="js-modal-close btn btn-danger btn-lg rounded-pill d-grid col-3">閉じる</button>
		</div><!--modal__inner-->
	</div><!--modal-->
<br>
<form method="POST" action="AdminPersonal.jsp">
	<input type="hidden" name="PID" value="TodayTemperature">
	<button class="btn btn-danger btn-lg rounded-pill d-grid col-3">戻る</button>
</form>
</div>
<br><br>
<%
} catch (Exception e) {
	// 不正なアクセスの場合トップ画面へリダイレクト
	response.sendRedirect("TopPage.jsp");
}
%>
</body>
</html>