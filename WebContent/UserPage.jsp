<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="portal.AccountDataBean ,portal.GroupDataBean ,java.sql .*, javax.naming .*, javax.sql .*, java.text .* , java.net.* ,java.util.Date , java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
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
<div class="container">
	<div class = "block">
		<%
			AccountDataBean loginData = (AccountDataBean) session.getAttribute("LoginUser");
		%>
		<h1 class="title">マイページ</h1>
	</div>

	<!-- パンくずリスト -->
	<ol class="breadcrumb mb-1 mr-auto">
		<li class="breadcrumb-item"><a><i class="fas fa-home"></i> マイページ</a></li>

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
	<div class="btn-group">
	<div class="dropdown-wrapper">
		<button class="btn dropdown-toggle btn-primary btn-lg rounded-pill col-3" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
		<span data-toggle="tooltip" data-placement="top" title="所属するグループのページへ">
		グループ
		</span>
		</button>

		<ul class="dropdown-menu">
			<li>
				<%
				ArrayList<GroupDataBean> affiliationList = (ArrayList<GroupDataBean>) session.getAttribute("AffiliationList");

				for(GroupDataBean affiliationData : affiliationList){
				%>
					<div class = "block">
					<form method="POST" action="GroupPageServlet">
					<input type="hidden" name="PID" value="GroupChoose">
					<input type="hidden" name="GroupName" value="<%= affiliationData.getGroupName() %>">
					<input type="hidden" name="GroupID" value="<%= affiliationData.getGroupid() %>">
					<button type="submit" class="btn btn-outline-primary rounded-pill btn-lg col-3"><%= affiliationData.getGroupName() %></button>
					<div class="dropdown-divider"></div>
					</form>
					</div>
				<%
				}
				%>
			</li>
		</ul>
	</div>
	</div>
<br><br><br>
	<form method="POST" action="UserPageServlet">
	<input type="hidden" name="PID" value="PersonalData">
	<button type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-3"
		data-toggle="tooltip" data-placement="top" title="直近3週間の体温記録">体調管理</button>
	</form>
<br><br>

	<button type="button" class="btn btn-secondary btn-lg rounded-pill d-grid col-3" onclick="location.href='NewGroup.jsp'"
	data-toggle="tooltip" data-placement="top" title="自分がリーダーとなる新しいグループを作成">新規グループ</button>
	<br><br><br>
	<button type="button" class="btn btn-secondary btn-lg rounded-pill d-grid col-3" onclick="location.href='OtherGroup.jsp'"
	data-toggle="tooltip" data-placement="top" title="未所属のグループへ加入申請">その他のグループ</button>
	<br><br><br>
	<form method="POST" action="LoginLogoutServlet">
	<input type="hidden" name="PID" value="Logout"/>
	<button type="submit" class="btn btn-danger btn-lg rounded-pill d-grid col-3">ログアウト</button>
	</form>
</div>
<%
} catch (Exception e) {
	// 不正なアクセスの場合トップ画面へリダイレクト
	response.sendRedirect("TopPage.jsp");
}
%>
</div>
</body>
</html>