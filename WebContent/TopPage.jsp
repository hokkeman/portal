<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
</head>
<body>
<div class="container">
	<div class = "block">
		<h1 class="title">グループSNSアプリ</h1>
		<br><br><br>
		<input type="button" class="btn btn-primary btn-lg rounded-pill d-grid col-4" value="ログイン" onclick="location.href='Login.jsp'"/>
		<br><br>
		<input type="button" class="btn btn-secondary btn-lg rounded-pill d-grid col-4" value="管理者ログイン" onclick="location.href='AdminLogin.jsp'"/>
	</div>
</div>
</body>
</html>