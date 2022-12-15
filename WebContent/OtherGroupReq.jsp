<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="portal.AccountDataBean ,portal.InfoBean , portal.GroupDataBean, java.util.* ,java.util.Date , java.text.*"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
</head>
<body>
<div class = "block">
<div class="container">
<%
try{
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

	<h3>グループへの加入リクエストを送信しました</h3>
	グループリーダーの認証完了後、グループへ入ることができます。
	<p>3秒後自動的にページを切り替えます</p>
	<meta http-equiv="refresh" content=" 3; url=UserPage.jsp">
	<%
	} catch (Exception e) {
		// 不正なアクセスの場合トップ画面へリダイレクト
		response.sendRedirect("TopPage.jsp");
	}
	%>
</div>
</div>
</body>
</html>