<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="portal.AccountDataBean ,portal.InfoBean , portal.GroupDataBean, java.util.* ,java.util.Date , java.text.*"%>
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
request.setCharacterEncoding("UTF-8");
GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");
AccountDataBean loginData = (AccountDataBean) session.getAttribute("LoginUser");
%>
<div class="container">
<div class = "block">
<h1 class="title">グループ管理 - <%= groupData.getGroupName() %></h1>
</div>

<!-- パンくずリスト -->
<ol class="breadcrumb mb-1 mr-auto">
	<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> マイページ</a></li>
	<li class="breadcrumb-item"><a href="GroupPage.jsp"><%= groupData.getGroupName() %></a></li>
	<li class="breadcrumb-item"><a>グループ管理</a></li>

	<!-- ログインユーザーを表示 -->
	<li class="ml-auto p-0">
		<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
			<input type="hidden" name="PID" value="Logout"/>
			<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top" title="ログアウト"><%= loginData.getUsername() %>さんがログイン中 <i class="fas fa-sign-out-alt"></i></button>
		</form>
	</li>
</ol><br>
<div class = "block">
	<!-- モーダルウィンドウ(編集)内容 -->
	<div id="Edit" class="modal js-modal">
		<div class="modal__bg js-modal-close"></div>
		<div class="modal__content">
			<h1 class="title2 pt-1">グループ情報編集</h1><br><br><br>
			<form method="POST" action="GroupManageServlet" onsubmit="return confirm('本当にグループ名と招待コードを変更してよろしいですか？');">
				<input type="hidden" name="PID" value="GroupEdit">
				<input type="hidden" name="GroupId" value="<%= groupData.getGroupid() %>">
				<h5>グループ名</h5>
				<div class = "block" style="position: relative; width:350px;">
					<input type="text" class="form-control" name="GroupName" size="30" maxlength ="20" value="<%= groupData.getGroupName() %>"required /><br><br>
				</div>
				<h5>招待コード</h5>
				<div class = "block" style="position: relative; width:350px;">
					<input type="text" class="form-control" name="InvitationCode" size="30" maxlength ="20" value="<%= groupData.getInvitationcode() %>" required /><br><br><br>
				</div>
				<br>
			<input type="submit" class="btn btn-primary btn-lg rounded-pill d-grid col-4" value="確定" /><br><br>
			</form>
			<button type="button" class="js-modal-close btn btn-danger btn-lg rounded-pill d-grid col-4">閉じる</button>
		</div><!--modal__inner-->
	</div><!--modal-->
	<!-- モーダルウィンドウ（削除）内容 -->
		<div id="Delete" class="modal js-modal">
		<div class="modal__bg js-modal-close"></div>
		<div class="modal__content">
			<h1 class="caution  pt-1">グループ削除</h1><br>
			<h3 class="text-danger">本当にグループを削除してよろしいですか？</h3>
			<h3 class="text-danger">※グループとグループ内のデータが全て削除されます</h3><br><br>

			<form method="POST" action="GroupManageServlet"
			onsubmit="return confirm('一度削除すると取り消すことはできません。本当によろしいですか？');">
				<input type="hidden" name="PID" value="GroupDelete">
				<input type="hidden" name="GroupId" value="<%= groupData.getGroupid() %>">
				<input type="submit" class="btn btn-warning btn-sm rounded-pill d-grid col-2 text-dark" value="削除" /><br><br>
			</form>
			<button type="button" class="js-modal-close btn btn-danger btn-lg rounded-pill d-grid col-4 ">閉じる</button>
		</div><!--modal__inner-->
	</div><!--modal-->

<h3>所属メンバー</h3>
<table class="table table-bordered" style="width:500px;">
	<tr class="table-secondary">
		<th>ユーザー名</th><th>ユーザーID</th><th>ブロック</th>
	</tr>
<%
	ArrayList<GroupDataBean> groupUserList = (ArrayList<GroupDataBean>) session.getAttribute("GroupUserList");
	for(GroupDataBean groupUser : groupUserList){
%>
<tr>
	<td><%= groupUser.getUsername() %></td>
	<td><%= groupUser.getUserid() %></td>
	<td>
		<form method="POST" action="GroupManageServlet" onsubmit="return confirm('本当に<%= groupUser.getUsername()%>さんをブロックしてよろしいですか？');">
		<input type="hidden" name="PID" value="Block">
		<input type="hidden" name="UserId" value="<%= groupUser.getUserid() %>">
		<input type="hidden" name="GroupId" value="<%= groupUser.getGroupid() %>">
		<% if(!loginData.getUserid().equals(groupUser.getUserid())){ %>
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
	<hr><br>
	<h3>加入認証待ちユーザー</h3>
<%
	ArrayList<GroupDataBean> waitUserList = (ArrayList<GroupDataBean>) session.getAttribute("WaitUserList");
	if(waitUserList.size() != 0){
%>
		<table class="table table-bordered" style="width:500px;">
			<tr class="table-secondary">
				<th>ユーザー名</th><th>ユーザーID</th><th>認証</th><th>削除</th>
			</tr>
	<% for(GroupDataBean waitUser : waitUserList){ %>
		<tr>
			<td><%= waitUser.getUsername() %></td>
			<td><%= waitUser.getUserid() %></td>
			<td>
				<form method="POST" action="GroupManageServlet" onsubmit="return confirm('本当に<%= waitUser.getUsername()%>さんを認証してよろしいですか？');">
				<input type="hidden" name="PID" value="Permit">
				<input type="hidden" name="UserId" value="<%= waitUser.getUserid() %>">
				<input type="hidden" name="GroupId" value="<%= waitUser.getGroupid() %>">
				<input type="submit" class="btn btn-outline-primary btn-sm" value="　" />
				</form>
			</td>
			<td>
				<form method="POST" action="GroupManageServlet" onsubmit="return confirm('本当に<%= waitUser.getUsername()%>さんのリクエストを削除してよろしいですか？');">
				<input type="hidden" name="PID" value="PermitDelete">
				<input type="hidden" name="UserId" value="<%= waitUser.getUserid() %>">
				<input type="hidden" name="GroupId" value="<%= waitUser.getGroupid() %>">
				<input type="submit" class="btn btn-outline-danger btn-sm" value="　" />
				</form>
			</td>
		</tr>
<%
		}
		%></table><%
	} else {
		%>認証待ちユーザーはいません<br><br><%
	}
%>
<br><hr><br>
<h3>グループ情報編集</h3>
<button type="button" class="js-modal-open btn btn-light" data-target="Edit"><i class="fas fa-pencil-alt fa-2x"></i></button>
<br><br><hr><br><br>
<h3>グループ削除</h3>
<button type="button" class="js-modal-open btn btn-light" data-target="Delete"><i class="fas fa-trash-alt fa-2x"></i></button>
<br><br>
<hr>
<br>
<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-4" value="戻る" onclick="location.href='GroupPage.jsp'"/>
<br><br><br>
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