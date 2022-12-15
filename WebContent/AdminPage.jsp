<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="portal.AccountDataBean ,portal.GroupDataBean ,java.sql .*, javax.naming .*, javax.sql .*, java.text .* , java.net.* ,java.util.Date , java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
</head>
<script>
$(function () {
  $('[data-toggle="tooltip"]').tooltip()
})
</script>
<body>
<%
try{
%>
<div class = "block">
<div class="container">
<h1 class="adtitle">管理者画面</h1>
<!-- パンくずリスト -->
<ol class="breadcrumb mb-1 mr-auto">
	<li class="breadcrumb-item"><a><i class="fas fa-home"></i> 管理者メニュー</a></li>
	<li class="ml-auto p-0">
		<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
			<input type="hidden" name="PID" value="Logout"/>
			<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top">ログアウト <i class="fas fa-sign-out-alt"></i></button>
		</form>
	</li>
</ol><br>
</div>
<div class = "block">
	<form method="POST" action="AdminPersonalServlet">
	<input type="hidden" name="PID" value="TodayTemperature">
	<button type="submit" class="btn btn btn-secondary btn-lg rounded-pill d-grid col-3"
	data-toggle="tooltip" data-placement="top" title="全てのユーザーの健康状態を確認">健康状態</button>
	</form>
	<br><br>
	<form method="POST" action="UserManageServlet">
	<input type="hidden" name="PID" value="UserManage">
	<button type="submit" class="btn btn-secondary btn-lg rounded-pill d-grid col-3"
		data-toggle="tooltip" data-placement="top" title="全てのユーザーの情報を表示">ユーザー管理</button>
	</form>
	<br><br>
	<form method="POST" action="AdminGroupServlet">
	<input type="hidden" name="PID" value="AdminGroupManage">
	<button type="submit" class="btn btn-secondary btn-lg rounded-pill d-grid col-3"
	data-toggle="tooltip" data-placement="top" title="全てグループの情報を表示">グループ管理</button>
	</form>
	<br><br>
	<form method="POST" action="LoginLogoutServlet">
	<input type="hidden" name="PID" value="Logout"/>
	<button type="submit" class="btn btn-danger btn-lg rounded-pill d-grid col-3">ログアウト</button>
	</form>
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