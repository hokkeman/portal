<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="portal.AccountDataBean ,portal.InfoBean , portal.GroupDataBean, java.util.* ,java.util.Date , java.text.*"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
</head>
<body>
<%
try{
%>
<div class = "block">
<div class="container">
<%
request.setCharacterEncoding("UTF-8");
GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");
AccountDataBean loginData = (AccountDataBean) session.getAttribute("LoginUser");
%>
<h1 class="title">グループ加入申請</h1>

<!-- パンくずリスト -->
<ol class="breadcrumb mb-1 mr-auto">
	<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> マイページ</a></li>
	<li class="breadcrumb-item"><a>グループ加入申請</a></li>

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
<h3>招待コードを入力してください</h3>
<form method="POST" action="GroupPageServlet">
<input type="hidden" name="PID" value="GroupRequest">
<div class = "block" style="position: relative; width:350px;">
<input type="text" class="form-control" name="InvitationCode" size="35" maxlength ="20"required/>
</div>
<br><br><br>
<button type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-3">送信</button>
</form>
<br>
<button type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" onclick="location.href='UserPage.jsp'">戻る</button>
</div>
<%
} catch (Exception e) {
	// 不正なアクセスの場合トップ画面へリダイレクト
	response.sendRedirect("TopPage.jsp");
}
%>
</body>
</html>