<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>已经归档</title>
    <!-- 引入bootstrap 包含 jquery -->
    <jsp:include page="../common/bootstrapLink.jsp"></jsp:include>

    <style>
        .table th, .table td {
            text-align: center;
            vertical-align: middle!important;
        }
    </style>

    <script>


        function loadFlowHistory() {//加载可发起的任务数据
            $.ajax({
                url:"activitiController/flowHistory",
                dataType:"json",
                type:"GET",
                async:false,
                success:function (data) {
                    var list = data.list;
                    $("#tbody").html("");//清空以前的数据，加载新数据
                    var appendHtml = "";
                    if(list){
                        list.forEach(function (data) {
                             appendHtml += " <tr><td>"+data.id+"</td>";
                             appendHtml += " <td>"+data.pid+"</td>";
                             appendHtml += " <td>"+data.name+"</td>";
                             appendHtml += " <td>"+data.starttime+"</td>";
                            appendHtml += " <td>"+data.endtime+"</td>";
                            appendHtml += " <td>"+data.lasttime+"</td>";
                             appendHtml += " <td>";
                             appendHtml += " <button type=\"button\" onclick=\"tuxing('"+data.pid+"','"+data.name+"')\" class=\"btn btn-warning\">流程图</button>";
                             appendHtml += " </td></tr>";
                        });

                    }

                    $("#tbody").append(appendHtml);

                }
            });
        }


        function tuxing(definitionId,name) {//显示图形
            var url = "../activitiController/graphics?definitionId="+definitionId;//instanceId
            var graphicsIframe = document.getElementById("graphics");
            graphicsIframe.setAttribute("width",(document.body.clientWidth)+"px");
            graphicsIframe.setAttribute("height",( document.body.clientHeight / 2 )+"px");
            graphicsIframe.setAttribute("src",url);
            document.getElementById("myModalLabel").innerHTML = name+"图";
            $("#tips").click();
        }


        $(function () {
            loadFlowHistory();//加载数据
        });

    </script>
</head>
<body>
<div class="panel panel-info">
    <div class="panel-heading">
        <h3 class="panel-title">已经归档的任务</h3>
    </div>
    <div class="panel-body">
        <div class="table-responsive">


            <table class="table">
                <thead >

                <tr>
                    <th >归档ID</th>
                    <th >流程ID</th>
                    <th>流程名称</th>
                    <th >发起时间</th>
                    <th >结束时间</th>
                    <th >持续时间</th>
                    <th >操作</th>
                </tr>

                </thead>

                <tbody id="tbody">

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
