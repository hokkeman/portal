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
<div class = "block">
<div class="container">
		<h1 class="title"><%= groupData.getGroupName() %>の連絡事項</h1>


	<!-- パンくずリスト -->
	<ol class="breadcrumb mb-1 mr-auto">
		<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> マイページ</a></li>
		<li class="breadcrumb-item"><a href="GroupPage.jsp"><%= groupData.getGroupName() %></a></li>
		<li class="breadcrumb-item"><a>連絡事項</a></li>

		<!-- ログインユーザーを表示 -->
		<li class="ml-auto p-0">
			<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
				<input type="hidden" name="PID" value="Logout"/>
				<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top" title="ログアウト"><%= loginData.getUsername() %>さんがログイン中 <i class="fas fa-sign-out-alt"></i></button>
			</form>
		</li>
	</ol><br>


<%
// 連絡事項の投稿・変更・削除完了時のセッション情報を受け取る
String infoOkMsg = String.valueOf(session.getAttribute("OkMsg"));
switch(infoOkMsg){
	case "infoPostOK" :
		%><h3>連絡事項の投稿が完了しました</h3><%
		break;
	case "infoChangeOK" :
		%><h3>連絡事項の変更が完了しました</h3><%
		break;
	case "infoDeleteOK" :
		%><h3>連絡事項の削除が完了しました</h3><%
		break;
	}
%>
<h5>3秒後自動的にページを切り替えます</h5>
<meta http-equiv="refresh" content=" 3; url=InfoPageServlet">
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