<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>可部署的流程</title>
    <!-- 引入bootstrap 包含 jquery -->
    <jsp:include page="../common/bootstrapLink.jsp"></jsp:include>

    <style>

    </style>

    <script>


    </script>

</head>
<body>

<div>

    <div class="panel panel-info">
        <div class="panel-heading">
            <h3 class="panel-title">可发起的任务列表</h3>
        </div>
        <div class="panel-body">
            <div class="table-responsive">


                <table class="table">
                    <%-- <caption>响应式表格布局</caption>--%>
                    <thead>
                    <tr>
                        <th>名称</th>
                        <th>操作</th>
                    </tr>
                    </thead>

                    <tbody>

                    <c:forEach var="temp" items="${list}">
                        <tr>
                            <td>${temp }</td>
                            <td><a href="activitiController/deploy?processName=${temp }">部署</a></td>
                        </tr>
                    </c:forEach>



                    </tbody>
                </table>


            </div>
        </div>
    </div>
</div>

</body>
</html>
