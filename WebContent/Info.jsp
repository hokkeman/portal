<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="portal.AccountDataBean ,portal.InfoBean , portal.GroupDataBean, java.util.* ,java.util.Date , java.text.*"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
<link rel="stylesheet" href="Personal.css">
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
<h1 class="title"><%= groupData.getGroupName() %>の連絡事項</h1>

<!-- パンくずリスト -->
<ol class="breadcrumb mb-1 mr-auto">
	<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> マイページ</a></li>
	<li class="breadcrumb-item"><a href="GroupPage.jsp"><%= groupData.getGroupName() %></a></li>
	<li class="breadcrumb-item"><a>連絡事項</a></li>

	<!-- ログインユーザーを表示 -->
	<li class="ml-auto">
		<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
			<input type="hidden" name="PID" value="Logout"/>
			<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top" title="ログアウト"><%= loginData.getUsername() %>さんがログイン中 <i class="fas fa-sign-out-alt"></i></button>
		</form>
	</li>
</ol><br>
<%
	request.setCharacterEncoding("UTF-8");
	ArrayList<InfoBean> allInfo = new ArrayList<InfoBean>();
	allInfo = (ArrayList<InfoBean>) session.getAttribute("AllInfo");

	// 連絡事項がある場合は表示
	if(allInfo.size() != 0){
%>
<div class = "block">
<%
		for(InfoBean infoData : allInfo){
%>
			<table class="table table-bordered text-dark">
			<tr>
				<th class="table-secondary"><h3 class="pt-2"><%= infoData.getInfoTitle() %></h3></th>
			</tr>
			<tr>
				<td align='left'><b>　作成者：</b><%=infoData.getUserName()%></td>
			</tr>
			<tr>
				<td align='left'><b>作成日時：</b><%=infoData.getInfoDate() %> <%=infoData.getInfoTime() %></td>
			</tr>
			<%
			if(infoData.getEditDate() != null){
				out.print("<tr><td align='left'><b>変更日時：</b>" + infoData.getEditDate() + " " + infoData.getEditTime() + "</td></tr>");
			}
			out.print("<tr><td align='left' style='white-space:pre-wrap'>" + infoData.getInfoText() + "</td></tr>");

			// ユーザーが連絡事項の作成者の場合編集ボタンを表示
			if(infoData.getUserId().equals(loginData.getUserid()) || loginData.getUserid().equals(groupData.getUserid())){
%>
				<td>
					<form method="POST" class="d-inline p-2" action="InfoEdit.jsp">
						<input type="hidden" name="InfoId" value="<%= infoData.getInfoId() %>">
						<input type="hidden" name="InfoTitle" value="<%= infoData.getInfoTitle() %>">
						<input type="hidden" name="InfoText" value="<%= infoData.getInfoText() %>">
						<button type="submit" class="btn btn-outline-secondary rounded-pill d-grid col-3">編集</button>
					</form>
					<form method="POST" class="d-inline p-2" action="InfoPageServlet" onclick="return confirm('本当に削除してよろしいですか？');">
						<input type="hidden" name="PID" value="InfoDelete">
						<input type="hidden" name="infoid" value="<%= infoData.getInfoId() %>"/>
						<button type="submit" class="btn btn-outline-secondary rounded-pill d-grid col-3">削除</button>
					</form>
				</td>
<%
			}
%>
		</table>
<%
		}
%></div><%
	} else {
		%><br><h3>連絡事項はありません</h3><%
	}
%>
</div>
<br><br><br><input type="button" class="btn btn-primary btn-lg rounded-pill d-grid col-3" value="投稿" onclick="location.href='InfoPost.jsp'"/>
<br><br><input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='GroupPage.jsp'"/>
<br><br><br><br>
</div>
<%
} catch (Exception e) {
	// 不正なアクセスの場合トップ画面へリダイレクト
	response.sendRedirect("TopPage.jsp");
}
%>
</body>
</html>
