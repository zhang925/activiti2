<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>可发起的任务列表</title>
    <!-- 引入bootstrap 包含 jquery -->
    <jsp:include page="../common/bootstrapLink.jsp"></jsp:include>

    <style>
        .table th, .table td {
            text-align: center;
            vertical-align: middle!important;
        }
    </style>

    <script>



        function loadstarttask() {//加载可发起的任务数据
            $.ajax({
                url:"activitiController/starttaskList",
                dataType:"json",
                type:"GET",
                async:false,
                success:function (data) {
                    var list = data.list;
                    $("#tbody").html("");//清空以前的数据，加载新数据
                    var appendHtml = "";
                    if(list){
                        list.forEach(function (value) {
                            var id = value.id;
                            var name = value.name;
                            var deploytime = value.deploytime;
                            appendHtml += " <tr><td>"+id+"</td>";
                            appendHtml += " <td>"+name+"</td>";
                            appendHtml += " <td>"+deploytime+"</td>";
                            appendHtml += " <td>";
                            appendHtml += " <button type=\"button\" onclick=\"starttask('"+id+"')\" class=\"btn btn-info\">启动流程</button>";
                            appendHtml += " <button type=\"button\" onclick=\"tuxing('"+id+"','"+name+"')\" class=\"btn btn-warning\">流程图</button>";
                            appendHtml += " <button type=\"button\" onclick=\"qxbs('"+id+"')\" class=\"btn btn-danger\">取消部署</button>";
                            appendHtml += " </td></tr>";
                        });
                    }
                   $("#tbody").append(appendHtml);

                }
            });
        }


        function starttask(id) { //启动流程
            var url = "../activitiController/starttask";
            $.ajax({
                url:url,
                dataType:"json",
                type:"POST",
                data:{"id":id},
                async:false,
                success:function (data) {
                    parent.shownum(); //调用 父级方法 刷新 任务数量
                    window.parent.document.getElementById("allreadystarttaskiframe").contentWindow.loadallreadyStarttaskList();//刷新 已经启动的列表
                    window.parent.document.getElementById("waitingProcessingIframe").contentWindow.loadMyTaskList();//刷新 代办
                    alert(data.state);
                }
            });
        }

        function tuxing(definitionId,name) {//显示图形
            var url = "../activitiController/graphics?definitionId="+definitionId;
            var graphicsIframe = document.getElementById("graphics");
            graphicsIframe.setAttribute("width",(document.body.clientWidth)+"px");
            graphicsIframe.setAttribute("height",( document.body.clientHeight / 2 )+"px");
            graphicsIframe.setAttribute("src",url);
            document.getElementById("myModalLabel").innerHTML = name+"图";
            $("#tips").click();
        }

        function qxbs(id) {//取消部署
            $.ajax({
                url:"activitiController/delDeployed",
                dataType:"json",
                type:"POST",
                data:{"id":id},
                async:false,
                success:function (data) {
                    loadstarttask();
                    parent.shownum(); //调用 父级方法 刷新 任务数量
                    alert(data.state);
                }
            });
        }

        $(function () {
            loadstarttask();//加载数据
        });

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
                    <thead >

                    <tr>
                        <th >流程ID</th>
                        <th >流程名称</th>
                        <th >部署时间</th>
                        <th >操作</th>
                    </tr>

                    </thead>

                    <tbody id="tbody">

                        <%--<c:forEach var="temp" items="${list}">
                            <tr >
                                <td>${temp.id}</td>
                                <td>${temp.name}</td>
                                <td>
                                    <button type="button" onclick="starttask('${temp.id}')" class="btn btn-info">启动流程</button>
                                    <button type="button" onclick="tuxing('${temp.id}')" class="btn btn-warning">图形</button>

                                </td>
                            </tr>
                        </c:forEach>--%>

                    </tbody>
                </table>


            </div>
        </div>
    </div>
</div>






<!-- 按钮触发模态框 $("#tips").click(); -->
<button id="tips" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal" style="display: none">开始演示模态框</button>
<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel"></h4>
            </div>
            <div class="modal-body">
                <iframe id="graphics" frameborder="0" scrolling="no" ></iframe>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

</body>
</html>
