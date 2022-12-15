<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  import="portal.AccountDataBean ,portal.GroupDataBean ,java.sql .*, javax.naming .*, javax.sql .*, java.text .* , java.net.* ,java.util.Date , java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
<link rel="stylesheet" href="Personal.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
</head>
<script>
$(function () {
  $('[data-toggle="tooltip"]').tooltip()
})
</script>

<!-- モーダルウィンドウ用js -->
	<script>
	$(function(){
	    $('.js-modal-open').each(function(){
	        $(this).on('click',function(){
	            var target = $(this).data('target');
	            var modal = document.getElementById(target);
	            $(modal).fadeIn();
	            return false;
	        });
	    });
	    $('.js-modal-close').on('click',function(){
	        $('.js-modal').fadeOut();
	        return false;
	    });
	});
	</script>
<!--  -->

<body>
<%
try{
%>
<div class = "block">
<div class="container">
<h1 class="adtitle">ユーザー管理画面</h1>
<!-- パンくずリスト -->
<ol class="breadcrumb mb-1 mr-auto">
	<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> 管理者メニュー</a></li>
	<li class="breadcrumb-item"><a>ユーザー管理</a></li>

	<li class="ml-auto p-0">
		<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
			<input type="hidden" name="PID" value="Logout"/>
			<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top">ログアウト <i class="fas fa-sign-out-alt"></i></button>
		</form>
	</li>
</ol><br>

<h3>ユーザーリスト</h3>

</div>
<%
ArrayList<AccountDataBean> allUserList = (ArrayList<AccountDataBean>)session.getAttribute("AllUserList");
%>
<table class="table table-hover" style="width: 600px;">
<tr>
	<th>名前</th><th>ユーザーID</th><th>詳細</th>
</tr>
<%
for(AccountDataBean userData : allUserList){
	%>
	<tr>
	<td><%= userData.getUsername() %></td>
	<td><%= userData.getUserid() %></td>
	<td>
		<form method="POST" action="UserManageServlet">
			<input type="hidden" name="PID" value="UserDetails">
			<input type="hidden" name="UserId" value="<%= userData.getUserid() %>">
			<input type="hidden" name="UserName" value="<%= userData.getUsername() %>">
			<button type="submit" class="btn"
			data-toggle="tooltip" data-placement="top" title="このユーザーの詳細を表示"><i class="fas fa-search"></i></button>
		</form>
	</td>
	</tr>
	<%
}
%>
</table>
<br><br><br>
<!-- 新規ユーザー追加用モーダルウィンドウを開くボタン -->
<div class="content">
	<button type="button" class="js-modal-open btn btn-secondary btn-lg rounded-pill d-grid col-3"
	data-toggle="tooltip" data-placement="top" data-target="NewUser" title="新しいユーザーを作成します">新規ユーザー</button>
</div>
<!-- 新規ユーザー追加用モーダルウィンドウ内容 -->
<div id="NewUser" class="modal js-modal">
	<div class="modal__bg js-modal-close"></div>
	<div class="modal__content">
		<h2>新規ユーザー作成</h2><br>
		<form method="POST"  action="UserManageServlet"  onsubmit="return confirm('本当にこの内容でユーザーを作成して宜しいですか？');">
			<input type="hidden" name="PID" value="NewUser">
			<div class = "block" style="position: relative; width:350px;">
				<h4>名前</h4>
				<input type="text" class="form-control" name="UserName" step="10" required/><br><br>
				<h4>ユーザーID</h4>
				<input type="text" class="form-control" name="UserId" size="20" maxlength ="20" required/><br><br>
				<h4>パスワード</h4>
				<input type="text" class="form-control" name="UserPass" size="20" maxlength ="20" required/><br><br>
				<h4>初期所属グループ</h4>
				<%
				// セッションから全てのグループデータを取得しチェックボックスを作成
				ArrayList<GroupDataBean> allGroupList = (ArrayList<GroupDataBean>)session.getAttribute("AllGroupList");
				%>
				<div class="form-group">
					<%
					int i = 0;
					for(GroupDataBean groupData : allGroupList){
						i++;
						%>
						<div class="custom-control custom-checkbox">
							<input class="custom-control-input" type="checkbox" value="<%= groupData.getGroupid() %>" name="Group" id="<%= i %>">
							<label class="custom-control-label" for="<%= i %>">
							<%= groupData.getGroupName() %>
							</label>
						</div>
					<%
					}
					%>
				</div>
			</div>
			<br><br>
			<button type="submit" class="btn btn-secondary btn-lg rounded-pill d-grid col-4">送信</button>
		</form><br>
		<button type="button" class="js-modal-close btn btn-danger btn-lg rounded-pill d-grid col-4">閉じる</button>
	</div>
</div><!--モーダルウィンドウ内容ここまで-->
<br>
<button class="btn btn-danger btn-lg rounded-pill d-grid col-3" onclick="location.href='AdminPage.jsp'">戻る</button>
</div>
<%
} catch (Exception e) {
	// 不正なアクセスの場合トップ画面へリダイレクト
	response.sendRedirect("TopPage.jsp");
}
%>
</body>
</html>