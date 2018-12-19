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
		<title>Welcome!</title>
	</head>
	<body>
		<div>Dear ${sessionScope.id}:</div>
		<div>
			<a href="/UseSC/views/login.jsp">Logout</a>
		</div>
	</body>
	
</html>