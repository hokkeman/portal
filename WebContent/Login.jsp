<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.net.*" import="java.util.*"%>
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
		<h1 class="title">ログイン</h1>
		<br><br>
		<form method="POST" action="LoginLogoutServlet">
			<div class = "block" style="position: relative; width:350px;">
				<input type="hidden" name="PID" value="UserLogin">
				ユーザーID
				<input type="text" class="form-control" name="userid" size="20" maxlength ="20" required/><br><br>
				パスワード
				<input type="password" class="form-control" name="userpass" size="20" maxlength ="20" required/><br><br>
				<br><br><br>
			</div>
			<input type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-4" value="ログイン" /><br><br>
			<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-4" value="戻る" onclick="location.href='TopPage.jsp'"/>
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