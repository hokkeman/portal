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
			<h3>新しいグループの作成が下記の通り完了しました</h3>
			<%
			GroupDataBean newGroupData = (GroupDataBean) session.getAttribute("NewGroupData");
			%><br>
		</div>
		<table class="table table-bordered" style="width: 500px;">
			<tr><th><h5>グループ名</h5></th><th><h5>招待コード</h5></th></tr>
			<tr>
				<td><%= newGroupData.getGroupName() %></td>
				<td><%= newGroupData.getInvitationcode() %></td>
			</tr>
		</table>
		<br><br>
		<form action="UserPageServlet">
			<input type="hidden" name="PID" value="NewGroupOK">
			<input type="submit" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="マイページ"/>
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