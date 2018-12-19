<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Please Login!</title>
	</head>
	<body>
		<div>
			<h3>Please login:</h3><br>
			<form action="login.sc" name="loginForm" accept-charset="utf-8">
				<div>   ID   :<input type="text" name="id"/></div>
				<div>Password:<input type="password" name="password"/></div>
				<div><input type="submit" name="login" value="Login"/></div>
			</form>
			<div>
				<font color="blue">${sessionScope.loginMessage }</font>
			</div>
			<br>
			<div>
				Please register:<a href="/UseSC/views/regist.jsp">Register</a>
			</div>
			<%
				session.removeAttribute("loginMessage");
			%>
		</div>
	</body>
</html>