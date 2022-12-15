<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  import="portal.AccountDataBean ,portal.InfoBean , portal.GroupDataBean, java.util.* ,java.util.Date , java.text.*"%>
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
<%
// セッションからユーザーの詳細情報を取得
ArrayList<GroupDataBean> groupDataList = (ArrayList<GroupDataBean>)session.getAttribute("GroupDataList");
AccountDataBean userData = (AccountDataBean)session.getAttribute("UserDetails");
%>
<div class = "block">
	<div class="container">
		<h1 class="adtitle">ユーザー情報</h1>

		<!-- パンくずリスト -->
		<ol class="breadcrumb mb-1 mr-auto">
			<li class="breadcrumb-item"><a href="AdminPage.jsp"><i class="fas fa-home"></i> 管理者メニュー</a></li>
			<li class="breadcrumb-item"><a href="UserManage.jsp">ユーザー管理</a></li>
			<li class="breadcrumb-item"><a>詳細</a></li>

			<!-- ログインユーザーを表示 -->
			<li class="ml-auto p-0">
				<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
					<input type="hidden" name="PID" value="Logout"/>
					<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top">ログアウト <i class="fas fa-sign-out-alt"></i></button>
				</form>
			</li>
		</ol><br>

		<div class="block">
			<table class="table table-bordered" style="width: 300px;" >
				<tr class="table-active"><th>名前</th><th>ID</th></tr>
				<tr><td><%= userData.getUsername() %></td><td><%= userData.getUserid() %></td></tr>
			</table>
		</div>
		<br><br>
		<h3>所属</h3>
		<table class="table table-bordered"  style="width: 600px;">
		<tr class="table-active"><th>グループ名</th><th>グループリーダー</th></tr>
		<%
		for (GroupDataBean userGroup : groupDataList){
			%>
			<tr>
			<td><%= userGroup.getGroupName() %></td>
			<td><%= userGroup.getUsername() %></td>
			</tr>
			<%
		}
		%>
		</table>
		<br><br><br>
		<%
		// アプリの管理ユーザー以外の場合削除ボタンを表示
		if(!userData.getUserid().equals("yokoo")){
		%>
		<button type="button" class="js-modal-open btn btn-danger btn-lg rounded-pill d-grid col-3" data-target="Delete">ユーザー削除</button>
			<!-- モーダルウィンドウ（削除）内容 -->
				<div id="Delete" class="modal js-modal">
				<div class="modal__bg js-modal-close"></div>
				<div class="modal__content">
					<h2 class="caution">ユーザー削除</h2><br>
					<h3 class="text-danger">本当にユーザーを削除してよろしいですか？</h3><br><br>

					<form method="POST" action="UserManageServlet"
					onsubmit="return confirm('一度削除すると取り消すことはできません。本当によろしいですか？');">
						<input type="hidden" name="PID" value="UserDelete">
						<input type="hidden" name="UserId" value="<%= userData.getUserid() %>">
						<input type="submit" class="btn btn-warning btn-sm rounded-pill d-grid col-2 text-black-100" value="削除" /><br><br>
					</form>
					<button type="button" class="js-modal-close btn btn-danger btn-lg rounded-pill d-grid col-4 ">閉じる</button>
				</div><!--modal__inner-->
			</div><!--modal-->
		<%
		}
		%>
		<br><br>
		<button class="btn btn-danger btn-lg rounded-pill d-grid col-3" onclick="location.href='UserManage.jsp'">戻る</button>
	</div>
</div>
<%
} catch (Exception e){
	response.sendRedirect("TopPage.jsp");
}
%>
</body>
</html>