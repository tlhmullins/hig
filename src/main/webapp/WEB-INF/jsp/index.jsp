<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>
</head>
<body>
<h1>Welcome</h1>
<h2>${message}</h2>

<a href="${pageContext.request.contextPath}/customerList">Customer List</a>  
Welcome!! <a href="/customerList">Click here</a> to see customers.

</body>
</html>