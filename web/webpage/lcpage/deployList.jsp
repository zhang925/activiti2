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


        //部署流程
        function deploytask(processName) {
            $.ajax({
                url: "activitiController/deploytask",
                dataType: "json",
                type: "POST",
                data: {"processName": processName},
                success: function (data) {
                    $("#tips").click();//显示提示语言
                }
            });
        }

        $(function () {

        });

    </script>

</head>
<body>

<div>
 <%--   <a href="#" class="list-group-item active">可部署的流程列表 </a>
    <a href="#" class="list-group-item">24*7 支持</a>
    <a href="#" class="list-group-item">免费 Window 空间托管</a>--%>

   <div class="panel panel-info">
        <div class="panel-heading">
            <h3 class="panel-title">可部署的流程列表【部署完成后就可以发起任务了】</h3>
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
                            <td><a href="javascript:void(0)" onclick="deploytask('${temp}')">部署</a></td>
                        </tr>
                    </c:forEach>



                    </tbody>
                </table>


            </div>
        </div>
    </div>









     <!-- 按钮触发模态框 -->
     <button id="tips" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal" style="display: none">开始演示模态框</button>
     <!-- 模态框（Modal） -->
     <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
         <div class="modal-dialog">
             <div class="modal-content">
                 <div class="modal-header">
                     <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                     <h4 class="modal-title" id="myModalLabel">提示</h4>
                 </div>
                 <div class="modal-body">已经部署成功！请到【发起任务】任务列表查看！</div>
                 <div class="modal-footer">
                     <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
                 </div>
             </div><!-- /.modal-content -->
         </div><!-- /.modal -->
     </div>



</div>

</body>
</html>
