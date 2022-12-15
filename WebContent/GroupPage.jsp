<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="portal.AccountDataBean , portal.GroupDataBean"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
</head>
<body>
<%
try{
%>
<div class="container">
	<div class = "block">
		<%
		request.setCharacterEncoding("UTF-8");
		GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");
		AccountDataBean loginData = (AccountDataBean) session.getAttribute("LoginUser");
		%>
		<h1 class="title"><%= groupData.getGroupName() %></h1>
	</div>
	<!-- パンくずリスト -->
	<ol class="breadcrumb mb-1 mr-auto">
		<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> マイページ</a></li>
		<li class="breadcrumb-item"><a><%= groupData.getGroupName() %></a></li>

		<!-- ログインユーザーを表示 -->
		<li class="ml-auto p-0">
			<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
				<input type="hidden" name="PID" value="Logout"/>
				<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top" title="ログアウト"><%= loginData.getUsername() %>さんがログイン中 <i class="fas fa-sign-out-alt"></i></button>
			</form>
		</li>
	</ol><br>
</div>

<div class = "block">
	<form method="POST" action="InfoPageServlet">
		<input type="hidden" name="PID" value="Info"/>
		<input type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-3" value="連絡事項" >
	</form><br><br>
	<form method="POST" action="ScheduleServlet">
		<input type="hidden" name="PID" value="thisMonth"/>
		<input type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-3" value="スケジュール">
	</form><br><br>
	<form method="POST" action="ThreadServlet">
		<input type="hidden" name="PID" value="toThreadPage"/>
		<input type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-3" value="掲示板">
	</form><br><br>
	<%
		// ユーザーがグループの管理者だった場合グループ管理ボタンを配置
		if(groupData.getUserid().equals(loginData.getUserid())){
	%>
			<form method="POST" action="GroupManageServlet">
				<input type="hidden" name="PID" value="GroupManage"/>
				<input type="submit" class="btn btn-secondary btn-lg rounded-pill d-grid col-3" value="グループ管理">
			</form><br><br>
	<%
	}
	%>
	<input type="submit" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="マイページ" onclick="location.href='UserPageServlet'"/>
	<%
	} catch (Exception e) {
		// 不正なアクセスの場合トップ画面へリダイレクト
		response.sendRedirect("TopPage.jsp");
	}
	%>
</div>
</body>
</html>