<%@ page pageEncoding="UTF-8" %>
<%
// 不正なアクセスの場合はトップ画面へリダイレクト
if (session.getAttribute("LoginUser") == null){
	throw new Exception();
}
%>