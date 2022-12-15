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
<h1 class="title">新規グループ作成</h1>

<!-- パンくずリスト -->
<ol class="breadcrumb mb-1 mr-auto">
	<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> マイページ</a></li>
	<li class="breadcrumb-item"><a>新規グループ作成</a></li>

	<!-- ログインユーザーを表示 -->
	<li class="ml-auto p-0">
		<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
			<input type="hidden" name="PID" value="Logout"/>
			<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top" title="ログアウト"><%= loginData.getUsername() %>さんがログイン中 <i class="fas fa-sign-out-alt"></i></button>
		</form>
	</li>
</ol><br>
</div>
<form method="POST" action="GroupPageServlet" onsubmit="return confirm('本当にグループを作成してよろしいですか？');">
<input type="hidden" name="PID" value="NewGroup">
<h3>グループ名</h3>
<div class = "block" style="position: relative; width:350px;">
<input type="text" class="form-control" name="GroupName" size="30" maxlength ="10" required /><br><br>
</div>
<h3>招待コード</h3>
※他のユーザーが加入申請をする際に必要となります<br>
<div class = "block" style="position: relative; width:350px;">
<input type="text"  class="form-control" name="InvitationCode" size="30" maxlength ="10" required /><br><br><br><br>
</div>
<input type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-3" value="決定" /><br><br>
<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="キャンセル" onclick="location.href='UserPage.jsp'"/>
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