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
		    elem.addEventListener('click', function() {
		      popup.classList.remove('is-show');
		    })
		  }
		}
	</script>
<!--  -->
<body>
<div class="container">
<div class = "block">
<h1 class="title">体調管理</h1>
</div>
<%
// セッション情報取得
request.setCharacterEncoding("UTF-8");
GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");
AccountDataBean loginData = (AccountDataBean) session.getAttribute("LoginUser");
%>
<!-- パンくずリスト -->
<ol class="breadcrumb mb-1 mr-auto">
	<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> マイページ</a></li>
	<li class="breadcrumb-item"><a>体調管理</a></li>

	<!-- ログインユーザーを表示 -->
	<li class="ml-auto p-0">
		<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
			<input type="hidden" name="PID" value="Logout"/>
			<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top" title="ログアウト"><%= loginData.getUsername() %>さんがログイン中 <i class="fas fa-sign-out-alt"></i></button>
		</form>
	</li>
</ol><br>


<%
// 今日の検温データが入力されていない場合入力用ポップアップを表示
String todayTemp = String.valueOf(session.getAttribute("TodayTemp"));
if(!todayTemp.equals("OK")){
	if(!(boolean)session.getAttribute("todayData")){
	%>
		<div class="popup" id="js-popup">
			<div class="popup-inner">
		    	<div class="close-btn" id="js-close-btn"><i class="fas fa-times"></i>
		    	</div>
		   		<div class = "block">
					<h3>今日の検温データを入力して下さい</h3>
					<form method="POST"  action="UserPageServlet">
					<input type="hidden" name="PID" value="TodayTemperature">
						検温時刻<br>
						<div class = "block" style="position: relative; width:350px;">
							<input type="time" class="form-control" name="Time" required/><br><br>
						</div>
						体温<br>
						<div class = "block" style="position: relative; width:350px;">
							<input type="number" class="form-control" name="Temperature" step="0.1" min="35" max="40" required>°C<br><br><br>
						</div>
						<input type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-4" value="送信" />
					</form>
				</div>
			</div>
		<div class="black-background" id="js-black-bg"></div>
		</div>
<%
	}
};
// セッションからユーザーの過去3週間の検温情報のリストを取得する
ArrayList<PersonalDataBean> personalDataList = new ArrayList<PersonalDataBean>();
personalDataList = (ArrayList<PersonalDataBean>) session.getAttribute("personalDataList");

// 現在時刻を取得
Date date = new Date();
Calendar calenderNow = Calendar.getInstance();
calenderNow.add(Calendar.DATE, - 22);
%>
<div class = "block">
<h3>直近3週間の体温</h3>

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
<div class = "block">
<table class="table" style="width: 800px;">
<tr class="table-light"><th>検温日</th><th>検温時刻</th><th>体温</th><th>記録</th></tr>
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
		<td></td></tr>
		<%
	} else {
		String modalDate = new SimpleDateFormat("M月d日").format(tableDate);
		%>
		<td>---</td>
		<td>---</td>
		<td>
		<div class="content">
		<button type="button" class="js-modal-open btn btn-light btn-sm" data-target="<%=tableDateSt%>"><i class="fas fa-pencil-alt"></i></button>
    	</div>
	    <div id="<%=tableDateSt%>" class="modal js-modal">
	        <div class="modal__bg js-modal-close"></div>
	        <div class="modal__content">
	            <h3><%=modalDate%> の検温データを入力してください</h3><br>
	            <form method="POST"  action="UserPageServlet">
	            	<input type="hidden" name="PID" value="PersonalEdit">
	            	<input type="hidden" name="Date" value="<%=tableDateSt%>">
	            	<h4>検温時刻</h4>
	            	<div class = "block" style="position: relative; width:350px;">
						<input type="time" class="form-control" name="Time" required /><br><br>
					</div>
					<h4>体温</h4>
					<div class = "block" style="position: relative; width:350px;">
						<input type="number" class="form-control" name="Temperature" step="0.1" min="35" max="40" required>°C<br><br><br>
					</div>
					<button type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-4">送信</button>
				</form>
					<br>
	            <button type="button" class="js-modal-close btn btn-danger btn-lg rounded-pill d-grid col-4">閉じる</button>
	        </div><!--modal__inner-->
	    </div><!--modal-->
		</td></tr>
		<%
	}
}
%>
</table>
<br><br><input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='UserPage.jsp'"/>
</div>
<br><br>
</div>
<%
} catch (Exception e) {
	// 不正なアクセスの場合トップ画面へリダイレクト
	response.sendRedirect("TopPage.jsp");
}
%>
</body>
</html>