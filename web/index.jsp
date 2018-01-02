<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>首页Activiti</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <script type="text/javascript" src="resources/js/jquery-1.11.1.min.js"></script>
</head>
<body>

<div>
    <form id="ff">
        <input id="username" name="username" /><br>
        <input id="psd" name="psd" /><br>
        <input id="sub" type="button" value="登录" /><br>
    </form>
</div>

<a href="webpage/lcpage/home.jsp">进入个人流程主页</a>

<a href="http://localhost:8888/activitiController">进入流程</a>


</body>

<script type="text/javascript">
    $(function () {

        $("#sub").click(function () {
            $.ajax({
                type: "POST",
                async: false,
                dataType: "json",
                url: "userController/login",
                data:$("#ff").serialize(),
                success: function(data) {
                   if(data.result == "success"){
                       location.href="activitiController";
                   }else{
                       alert("用户名或者密码错误");
                   }
                }
            });
        });

    });

</script>

</html>

