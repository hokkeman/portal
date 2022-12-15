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
<%
request.setCharacterEncoding("UTF-8");
GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");
AccountDataBean loginData = (AccountDataBean) session.getAttribute("LoginUser");
%>
<div class="container">
	<div class = "block">
		<h1 class="title mt-2 pt-2">連絡事項投稿</h1>
	</div>


	<!-- パンくずリスト -->
	<ol class="breadcrumb mb-1 mr-auto">
		<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> マイページ</a></li>
		<li class="breadcrumb-item"><a href="GroupPage.jsp"><%= groupData.getGroupName() %></a></li>
		<li class="breadcrumb-item"><a href="Info.jsp">連絡事項</a></li>
		<li class="breadcrumb-item"><a>投稿</a></li>

		<!-- ログインユーザーを表示 -->
		<li class="ml-auto p-0">
			<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
				<input type="hidden" name="PID" value="Logout"/>
				<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top" title="ログアウト"><%= loginData.getUsername() %>さんがログイン中 <i class="fas fa-sign-out-alt"></i></button>
			</form>
		</li>
	</ol><br>
	<div class = "block">
		<h3>投稿する連絡事項の内容を入力してください</h3><br><br>
		<form method="POST" action="InfoPageServlet"  onsubmit="return confirm('本当にこの内容で投稿してよろしいですか？？');">
			<input type="hidden" name="PID" value="InfoPost">
			<div class = "block" style="width:700px;">
				<h5>タイトル</h5>
				<input type="text" class="form-control" name="infotitle" size="35" maxlength ="20" required /><br>
				<h5>内容</h5>
				<textarea name="infotext" class="form-control" maxlength ="1000" rows="15" cols="50" required /></textarea><br><br><br>
			</div>
			<input type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-3" value="投稿" /><br><br>
		</form>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='Info.jsp'"/>
		<br><br><br><br>
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