<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="portal.AccountDataBean ,portal.InfoBean , portal.GroupDataBean, portal.ThreadDataBean , java.util.* ,java.util.Date , java.text.*"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
<link rel="stylesheet" href="Personal.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
</head>

<%
// セッションからログインしているユーザーと現在のグループの情報を取得
request.setCharacterEncoding("UTF-8");
GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");
AccountDataBean loginData = (AccountDataBean) session.getAttribute("LoginUser");
%>
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
<h1 class="title">掲示板</h1>

<!-- パンくずリスト -->
<ol class="breadcrumb mb-1 mr-auto">
	<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> マイページ</a></li>
	<li class="breadcrumb-item"><a href="GroupPage.jsp"><%= groupData.getGroupName() %></a></li>
	<li class="breadcrumb-item"><a>掲示板</a></li>

	<!-- ログインユーザーを表示 -->
	<li class="ml-auto">
		<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
			<input type="hidden" name="PID" value="Logout"/>
			<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top" title="ログアウト"><%= loginData.getUsername() %>さんがログイン中 <i class="fas fa-sign-out-alt"></i></button>
		</form>
	</li>
</ol><br>
<%
// セッションからグループ内の全てのスレッド情報を取得
ArrayList<ThreadDataBean> allThreadList = (ArrayList<ThreadDataBean>) session.getAttribute("allThreadList");

// スレッドが存在する場合表示
if(allThreadList.size() != 0){
	%>
	<table class="table table-bordered table-hover" style="width: 700px;">
	<tr class="table-secondary"><th>スレッドタイトル</th><th>作成者</th><th>作成日</th><th>作成時刻</th><th>削除</th></tr>
	<%
	for(ThreadDataBean threadData : allThreadList){
		%>
		<tr>
			<td><form method="POST"  action="BoardServlet">
					<input type="hidden" name="PID" value="toBoardPage">
					<input type="hidden" name="ThreadId" value="<%= threadData.getThreadId() %>">
					<input type="hidden" name="ThreadTitle" value="<%= threadData.getThreadTitle() %>">
					<button type="submit" class="btn"><%= threadData.getThreadTitle() %></button>
				</form>
			</td>
			<td class="pt-3"><%= threadData.getUserName() %></td>
			<td class="pt-3"><%= threadData.getThreadDate() %></td>
			<td class="pt-3"><%= threadData.getThreadTime() %></td>
			<td>
			<%
			// ログインユーザーがこのグループのリーダーの場合だけ削除ボタンが表示される
			if(groupData.getUserid().equals(loginData.getUserid()) || loginData.getUsername().equals(threadData.getUserName())){
			%>
				<form method="POST" action="ThreadServlet" onclick="return confirm('本当に削除してよろしいですか？');">
					<input type="hidden" name="PID" value="delete">
					<input type="hidden" name="ThreadId" value="<%= threadData.getThreadId() %>">
					<Button type="submit" class="btn btn-sm"><i class="fas fa-trash-alt"></i></button>
				</form>
			<%
			}
			%>
			</td>
		</tr>
		<%
	}
	%>
	</table>
	<%
} else {
	%><br><h3>スレッドはありません</h3><%
}
	%>
</div>
<!-- モーダルウィンドウを開くボタン -->
<br><br><br>
<div class="content">
<button type="button" class="js-modal-open btn btn-primary btn-lg rounded-pill d-grid col-3" data-target="CreateThread">新規スレッド作成</button>
</div>
<!-- ここからモーダルウィンドウの中身 -->
	<div id="CreateThread" class="modal js-modal">
		<div class="modal__bg js-modal-close"></div>
		<div class="modal__content">
			<h3>新規スレッド作成</h3><br>
			<form method="POST"  action="ThreadServlet">
			<input type="hidden" name="PID" value="CreateThread">
				<h5>スレッドタイトル</h5>
				<div class = "block">
					<input type="text" class="form-control" name="ThreadTitle" maxlength ="20" required/><br><br>
				</div>
				<h5>書き込み内容</h5>
				<div class = "block">
					<textarea name="Contents" class="form-control" maxlength ="1000" rows="10" cols="40" required /></textarea><br><br>
				</div>
				<button type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-3">送信</button>
			</form>
				<br>
			<button type="button" class="js-modal-close btn btn-danger btn-lg rounded-pill d-grid col-3">閉じる</button>
		</div><!--modal__inner-->
    </div><!--modal-->
	    <!-- ここまで -->
<br>
<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='GroupPage.jsp'"/>

</div>
<%
} catch (Exception e){
	response.sendRedirect("TopPage.jsp");
}
%>
</body>
</html>