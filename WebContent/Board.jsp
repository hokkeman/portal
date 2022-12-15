<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="portal.BoardDataBean, portal.AccountDataBean, portal.InfoBean, portal.GroupDataBean, portal.ThreadDataBean , java.util.* ,java.util.Date , java.text.*"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="Header.jsp"></jsp:include>
<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
</head>
<%
try{
%>
<script>
$(function () {
  $('[data-toggle="tooltip"]').tooltip()
})
</script>
<script>
function hoge(){
  var elm = document.documentElement;
  //scrollHeight ページの高さ clientHeight ブラウザの高さ
  var bottom = elm.scrollHeight - elm.clientHeight;
  //垂直方向へ移動
  window.scroll(0, bottom);
}
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
// セッションからログインしているユーザーと現在のグループの情報を取得
request.setCharacterEncoding("UTF-8");
GroupDataBean groupData = (GroupDataBean)session.getAttribute("GroupData");
AccountDataBean loginData = (AccountDataBean) session.getAttribute("LoginUser");

//セッションからスレッドタイトルと全ての書き込みを取得
String threadTitle = String.valueOf(session.getAttribute("ThreadTitle"));
ArrayList<BoardDataBean> boardDataList = (ArrayList<BoardDataBean>) session.getAttribute("allBoardList");
%>

<div class="container">
<div class = "block">
<h1 class="title">掲示板</h1>

<!-- パンくずリスト -->
<ol class="breadcrumb mb-1 mr-auto">
	<li class="breadcrumb-item"><a href="UserPage.jsp"><i class="fas fa-home"></i> マイページ</a></li>
	<li class="breadcrumb-item"><a href="GroupPage.jsp"><%= groupData.getGroupName() %></a></li>
	<li class="breadcrumb-item"><a href="Thread.jsp">掲示板</a></li>
	<li class="breadcrumb-item"><a><%= threadTitle %></a></li>

	<!-- ログインユーザーを表示 -->
	<li class="ml-auto">
		<form method="POST" class="d-inline" action="LoginLogoutServlet" onsubmit="return confirm('ログアウトしてよろしいですか？');">
			<input type="hidden" name="PID" value="Logout"/>
			<button type="submit" class="btn p-0" data-toggle="tooltip" data-placement="top" title="ログアウト"><%= loginData.getUsername() %>さんがログイン中 <i class="fas fa-sign-out-alt"></i></button>
		</form>
	</li>
</ol><br>

<!-- スレッドタイトル表示 -->
<h3><%= threadTitle %></h3>
	<button type="button" class="btn" onclick="hoge();"
		data-toggle="tooltip" data-placement="top" title="最新の書き込みへ">
    	<i class="fas fa-angle-double-down fa-2x"></i>
	</button>
</div><br>
<table class="table" style="width: 800px;">
	<%
	int i = 0;
	for(BoardDataBean boardData : boardDataList){
		i++;
		%>

		<tr class="table-active">
			<th class="table-secondary">
				<%= i %>　名前：<%= boardData.getUserName() %>　<%= boardData.getBoardDate() %>　<%= boardData.getBoardTime() %>
			</th>
		</tr>
		<tr><td align='left' style='white-space:pre-wrap'><%= boardData.getContents() %></td>
		<tr><td></td></tr>
		</tr>
		<%
	}
	%>
</table>
<div class = "block">
<div class="content">
	<form method="POST" class="ml-auto p-0" action="BoardServlet">
		<input type="hidden" name="PID" value="toBoardPage">
		<input type="hidden" name="ThreadId" value="<%= session.getAttribute("ThreadId") %>">
		<input type="hidden" name="ThreadTitle" value="<%= session.getAttribute("ThreadTitle") %>">
		<button type="submit" class="btn" data-toggle="tooltip" data-placement="top" title="更新">
    		<i class="fas fa-redo fa-2x"></i>
		</button>
	</form>
<br><br>
<button type="button" class="js-modal-open btn btn-primary btn-lg rounded-pill d-grid col-4" data-target="CreateThread">書き込む</button><br><br>
</div>
<!-- ここからモーダルウィンドウの中身 -->
	<div id="CreateThread" class="modal js-modal">
	<div class="modal__bg js-modal-close"></div>
		<div class="modal__content">
			<form method="POST"  action="BoardServlet">
			<input type="hidden" name="PID" value="BoardPost">
			<input type="hidden" name="ThreadId" value="<%= session.getAttribute("ThreadId") %>">
				<div class = "block">
					<textarea name="Contents" class="form-control" maxlength ="1000" rows="10" cols="40" required /></textarea><br><br>
				</div>
				<button type="submit" class="btn btn-primary btn-sm d-grid col-4">送信</button>
			</form>
				<br>
			<button type="button" class="js-modal-close btn btn-light btn-sm d-grid col-4">閉じる</button>
		</div><!--modal__inner-->
    </div><!--modal-->
	    <!-- ここまで -->

<input type="button" class="btn btn-danger btn-lg rounded-pill d-grid col-4" value="戻る" onclick="location.href='ThreadServlet'"/>
</div>
</div>
<br><br>
<%
} catch (Exception e) {
	// 不正なアクセスの場合トップ画面へリダイレクト
	response.sendRedirect("TopPage.jsp");
}
%>
</body>
</html>