<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<% 
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			 + request.getServerName() + ":"
			 + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Please Register!</title>
	</head>
	<body>
		<div>
			<h3>Please register:</h3><br>
			<form action="regist.sc" name="loginForm" accept-charset="utf-8">
				<div> New ID :<input type="text" name="id"/></div>
				<div>Password:<input type="password" name="password"/></div>
				<div><input type="submit" name="regist" value="Register"/></div>
			</form>
			<div>
				<font color="red">${sessionScope.registMessage }</font>
			</div>
			<br>
			<%
				session.removeAttribute("registMessage");
			%>
		</div>
	</body>
</html>