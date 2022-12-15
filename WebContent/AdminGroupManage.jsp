<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="portal.AccountDataBean ,portal.InfoBean , portal.GroupDataBean, java.util.* ,java.util.Date , java.text.*"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
<link rel="stylesheet" href="Personal.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
</head>
<body>
<%
try{
%>
	<div class = "block">
		<div class="container">
			<%
			// 不正アクセスの場合トップページに遷移
			AccountDataBean loginData = (AccountDataBean) session.getAttribute("LoginAdminUser");
			if(loginData == null){
				throw new Exception();
			}

			// 全てのグループの情報をセッションから取得
			ArrayList<GroupDataBean> allGroupList = (ArrayList<GroupDataBean>)session.getAttribute("AllGroupList");
			%>
			<h1 class="adtitle">グループ管理</h1>

			<!-- パンくずリスト -->
			<ol class="breadcrumb mb-1 mr-auto">
				<li class="breadcrumb-item"><a href="AdminPage.jsp"><i class="fas fa-home"></i>管理者メニュー</a></li>
				<li class="breadcrumb-item"><a>グループ管理</a></li>

			<!-- ログインユーザーを表示 -->
			<li class="ml-auto p-0">
				<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
					<input type="hidden" name="PID" value="Logout"/>
					<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top">ログアウト <i class="fas fa-sign-out-alt"></i></button>
				</form>
			</li>
			</ol><br>


			<table class="table" style="width: 700px;">
				<tr class="table-light"><th>グループ名</th><th>グループリーダー</th><th>詳細</th></tr>
				<%
				for(GroupDataBean groupData : allGroupList){
					%>
					<tr>
						<td><%= groupData.getGroupName() %></td>
						<td><%= groupData.getUsername() %></td>
						<td>
							<form method="POST" action="AdminGroupServlet">
								<input type="hidden" name="PID" value="GroupDetails">
								<input type="hidden" name="GroupId" value="<%= groupData.getGroupid() %>">
								<button type="submit" class="btn"
								data-toggle="tooltip" data-placement="top" title="このグループの詳細を表示"><i class="fas fa-search"></i></button>
							</form>
						</td>
					</tr>
				<%
				}
				%>
			</table>
			<br><br><input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='AdminPage.jsp'"/>
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