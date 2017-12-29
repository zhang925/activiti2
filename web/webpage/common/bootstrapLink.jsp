<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String pathBs = request.getContextPath();
String basePathBs = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+pathBs+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePathBs%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title></title>
	<link rel="stylesheet" href="<%=basePathBs%>resources/bootstrap/css/bootstrap.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=basePathBs%>resources/bootstrap/css/bootstrap-theme.css" type="text/css"></link>
	<!-- 引入 jquery -->
	<script type="text/javascript" src="<%=basePathBs%>resources/bootstrap/js/jquery-1.9.1.js"></script>
	<script src="<%=basePathBs%>resources/bootstrap/js/bootstrap.js" type="text/javascript"></script>

</head>
	<body>
	</body>
</html>