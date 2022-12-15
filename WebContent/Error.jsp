<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<%
String errorMsg = String.valueOf(session.getAttribute("ErrorMsg"));
switch (errorMsg){
	case "SessionError":
		%>
		<br><br><br>
		<h3>ページを表示できません</h3><br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='TopPage.jsp'"/>
		<%
		break;
	case "infoPostError":
		%>
		<h3>連絡事項を投稿できませんでした</h3><br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='InfoPost.jsp'"/>
		<%
		break;
	case "infoChangeError":
		%>
		<h3>連絡事項を変更できませんでした</h3><br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='Info.jsp'"/>
		<%
		break;
	case "infoDeleteError":
		%>
		<h3>連絡事項を削除できませんでした</h3><br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='Info.jsp'"/>
		<%
		break;
	case "LoginError":
		%>
		<div class="container">
			<h1 class="title">ログイン</h1><br><br><br>
			<h3>ログインに失敗しました</h3><br><br><br>
			<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='TopPage.jsp'"/>
		</div>
		<%
		break;
	case "AdminLoginError":
		%>
		<div class="container">
			<h1 class="adtitle">ログイン</h1><br><br><br>
			<h3>ログインに失敗しました</h3><br><br><br>
			<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='TopPage.jsp'"/>
		</div>
		<%
		break;
	case "newGroupError":
		%>
		<h3>新しいグループを作成できませんでした</h3><br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='NewGroup.jsp'"/>
		<%
		break;
	case "GroupManage":
		%>
		<h3>ページを表示できません</h3><br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='GroupPage.jsp'"/>
		<%
		break;
	case "GroupRequest":
		%>
		<div class="container">
			<h1 class="title">グループ加入申請</h1><br><br><br>
		<h3>リクエストを送信できませんでした</h3><br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='OtherGroup.jsp'"/>
		</div>
		<%
		break;
	case "DeleteError":
		%>
		<h3>グループを削除できませんでした</h3><br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='GroupPage.jsp'"/>
		<%
		break;
	case "PermitError":
		%>
		<h3>加入申請を認証できませんでした</h3><br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='GroupPage.jsp'"/>
		<%
		break;
	case "GroupEditingError":
		%>
		<h3>グループ情報を編集できませんでした</h3><br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='GroupPage.jsp'"/>
		<%
		break;
	case "ThreadCreateError":
		%>
		<h3>スレッドを作成できませんでした</h3>
		既にこのスレッド名は存在します<br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='ThreadServlet'"/>
		<%
		break;
	case "GroupEditError":
		%>
		<h3>グループを編集できませんでした</h3><br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='GroupManage.jsp'"/>
		<%
		break;
	case "PermitDeleteError":
		%>
		<h3>削除できませんでした</h3><br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='GroupManage.jsp'"/>
		<%
		break;
	case "NewUserError":
		%>
		<h3>新規ユーザーを作成できませんでした</h3>
		既に使われているユーザーIDの可能性があります
		<br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='UserManage.jsp'"/>
		<%
		break;
	case "UserDeleteError":
		%>
		<h3>ユーザーを削除できませんでした</h3>
		<br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='UserManage.jsp'"/>
		<%
		break;
	case "GroupDetailsError":
		%>
		<h3>ページを表示できません</h3>
		<br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='AdminGroupServlet'"/>
		<%
		break;
	case "AdminGroupEditError":
		%>
		<h3>グループデータを編集できませんでした</h3>
		<br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='GroupDetailsServlet'"/>
		<%
		break;
	case "AdGroupDeleteError":
		%>
		<h3>グループを削除できませんでした</h3>
		<br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='GroupDetailsServlet'"/>
		<%
		break;
	case "LeaderError":
		%>
		<h3>リーダーを変更できませんでした</h3>
		<br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='GroupDetailsServlet'"/>
		<%
		break;
	case "scheduleChangeNG":
		%>
		<h3>スケジュールを変更できませんでした</h3>
		<br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='ScheduleServlet'"/>
		<%
		break;
	case "scheduleDeleteNG":
		%>
		<h3>スケジュールを削除できませんでした</h3>
		<br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='ScheduleServlet'"/>
		<%
		break;
	case "ThreadDeleteError":
		%>
		<h3>スレッドを削除できませんでした</h3>
		<br><br><br>
		<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='Thread.jsp'"/>
		<%
		break;
}
%>
</div>
<%
} catch (Exception e) {
	// 不正なアクセスの場合トップ画面へリダイレクト
	response.sendRedirect("TopPage.jsp");
}
%>
</body>
</html>