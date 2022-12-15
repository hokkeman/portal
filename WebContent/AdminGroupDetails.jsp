<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="portal.AccountDataBean ,portal.GroupDataBean ,java.sql .*, javax.naming .*, javax.sql .*, java.text .* , java.net.* ,java.util.Date , java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
<link rel="stylesheet" href="Personal.css">
<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
</head>
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
<%
// セッションからグループの情報とメンバーリストを取得
GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");
ArrayList<GroupDataBean> memberList = (ArrayList<GroupDataBean>)session.getAttribute("MemberList");
%>
<div class="block">
<div class="container">
<h1 class="adtitle">グループ詳細</h1>

<!-- パンくずリスト -->
		<ol class="breadcrumb mb-1 mr-auto">
			<li class="breadcrumb-item"><a href="AdminPage.jsp"><i class="fas fa-home"></i>管理者メニュー</a></li>
			<li class="breadcrumb-item"><a href="AdminGroupManage.jsp">グループ管理</a></li>
			<li class="breadcrumb-item"><a>詳細</a></li>

		<!-- ログインユーザーを表示 -->
		<li class="ml-auto p-0">
			<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
				<input type="hidden" name="PID" value="Logout"/>
				<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top">ログアウト <i class="fas fa-sign-out-alt"></i></button>
			</form>
		</li>
		</ol><br>
		</div>

<table class="table table-bordered" style="width:500px;">
	<tr class="table-secondary"><th>グループ名</th><th>招待コード</th></tr>
	<tr><td><%= groupData.getGroupName() %></td><td><%= groupData.getInvitationcode() %></td></tr>
</table>
<br><br>
<h3>グループリーダー</h3>
<table class="table table-bordered" style="width:500px;">
<tr class="table-secondary"><th>ユーザー名</th><th>ユーザーID</th></tr>
<tr>
<td><%= groupData.getUsername() %></td>
<td><%= groupData.getUserid() %></td>
</tr>
</table>
<br><br>
<h3>メンバーリスト</h3>
<table class="table table-bordered" style="width:500px;">
	<tr class="table-secondary">
		<th>ユーザー名</th><th>ユーザーID</th><th>リーダー変更</th><th>ブロック</th>
	</tr>
<%
	ArrayList<GroupDataBean> groupUserList = (ArrayList<GroupDataBean>) session.getAttribute("GroupUserList");
	for(GroupDataBean groupUser : memberList){
%>
<tr>
	<td><%= groupUser.getUsername() %></td>
	<td><%= groupUser.getUserid() %></td>
	<td>
	<form method="POST" action="GroupDetailsServlet" onsubmit="return confirm('本当に<%= groupUser.getUsername()%>さんをグループリーダーにしてよろしいですか？');">
		<input type="hidden" name="PID" value="Leader">
		<input type="hidden" name="GroupId" value="<%= groupUser.getGroupid() %>">
		<input type="hidden" name="UserId" value="<%= groupUser.getUserid() %>">
		<% if(!groupData.getUserid().equals(groupUser.getUserid())){ %>
		<input type="submit" class="btn btn-outline-permit btn-sm" value="　" />
	<% } else {	%>
		<input type="submit" class="btn btn-outline-permit btn-sm " value="　" disabled/>
	<%
	}
	%>
	</form>
	</td>
	<td>
		<form method="POST" action="GroupDetailsServlet" onsubmit="return confirm('本当に<%= groupUser.getUsername()%>さんをグループからブロックしてよろしいですか？');">
		<input type="hidden" name="PID" value="Block">
		<input type="hidden" name="UserId" value="<%= groupUser.getUserid() %>">
		<input type="hidden" name="GroupId" value="<%= groupUser.getGroupid() %>">
		<% if(!groupData.getUserid().equals(groupUser.getUserid())){ %>
			<input type="submit" class="btn btn-outline-danger btn-sm" value="　" />
		<% } else {	%>
			<input type="submit" class="btn btn-outline-danger btn-sm " value="　" disabled/>
			<%
		}
		 %>
		</form>
	</td>
</tr>
<%
	}
%>
	</table>
	<br><br>
<table class="table table-bordered" style="width:500px;"">
	<tr><th><h3>グループ情報編集</h3></th><th><h3>グループ削除</h3></th></tr>
	<td>
		<button type="button" class="js-modal-open btn btn-light" data-target="Edit"><i class="fas fa-pencil-alt fa-2x"></i></button>
	</td>
	<td>
		<button type="button" class="js-modal-open btn btn-light" data-target="Delete"><i class="fas fa-trash-alt fa-2x"></i></button>
	</td>
</table>
<br><br>

<!-- モーダルウィンドウ(編集)内容 -->
	<div id="Edit" class="modal js-modal">
		<div class="modal__bg js-modal-close"></div>
		<div class="modal__content">
			<h1 class="adtitle">グループ情報編集</h1><br><br><br>
			<form method="POST" action="GroupDetailsServlet" onsubmit="return confirm('本当にグループ名と招待コードを変更してよろしいですか？');">
				<input type="hidden" name="PID" value="GroupEdit">
				<input type="hidden" name="GroupId" value="<%= groupData.getGroupid() %>">
				<input type="hidden" name="UserId" value="<%= groupData.getUserid() %>">
				<h5>グループ名</h5>
				<div class = "block" style="position: relative; width:350px;">
					<input type="text" class="form-control" name="GroupName" size="30" maxlength ="20" value="<%= groupData.getGroupName() %>"required /><br><br>
				</div>
				<h5>招待コード</h5>
				<div class = "block" style="position: relative; width:350px;">
					<input type="text" class="form-control" name="InvitationCode" size="30" maxlength ="20" value="<%= groupData.getInvitationcode() %>" required /><br><br><br>
				</div>
				<br>
			<input type="submit" class="btn btn-secondary btn-lg rounded-pill d-grid col-4" value="確定" /><br><br>
			</form>
			<button type="button" class="js-modal-close btn btn-danger btn-lg rounded-pill d-grid col-4">閉じる</button>
		</div><!--modal__inner-->
	</div><!--modal-->
	<!-- モーダルウィンドウ（削除）内容 -->
		<div id="Delete" class="modal js-modal">
		<div class="modal__bg js-modal-close"></div>
		<div class="modal__content">
			<h2 class="caution">グループ削除</h2><br>
			<h3 class="text-danger">本当にグループを削除してよろしいですか？</h3>
			<h3 class="text-danger">※グループとグループ内のデータが全て削除されます</h3><br><br>

			<form method="POST" action="GroupDetailsServlet"
			onsubmit="return confirm('一度削除すると取り消すことはできません。本当によろしいですか？');">
				<input type="hidden" name="PID" value="GroupDelete">
				<input type="hidden" name="GroupId" value="<%= groupData.getGroupid() %>">
				<input type="submit" class="btn btn-warning btn-sm rounded-pill d-grid col-2 text-black-100" value="削除" /><br><br>
			</form>
			<button type="button" class="js-modal-close btn btn-danger btn-lg rounded-pill d-grid col-4 ">閉じる</button>
		</div><!--modal__inner-->
	</div><!--modal-->

<br><br><input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-3" value="戻る" onclick="location.href='AdminGroupServlet'"/>
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