<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.net.*" import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
</head>
<body>
<div class = "block">
	<div class="container">
		<h1 class="adtitle">管理者ログイン</h1>

	<br><br>
		<form method="POST" action="LoginLogoutServlet">
			<div class = "block" style="position: relative; width:350px;">
			<input type="hidden" name="PID" value="AdminLogin">
			ユーザーID
			<input type="text" class="form-control" name="AdminId" size="20" maxlength ="20" required/><br><br>
			パスワード
			<input type="password" class="form-control" name="AdminPass" size="20" maxlength ="20" required/><br><br>
			<br><br><br>
			</div>
			<input type="submit" class="btn btn-secondary btn-lg rounded-pill d-grid col-4" value="ログイン" /><br><br>
			<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-4" value="戻る" onclick="location.href='TopPage.jsp'"/>
		</form>
	</div>
</div>
</body>
</html>