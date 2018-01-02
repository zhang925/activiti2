<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>个人流程的主页</title>
    <!-- 引入bootstrap 包含 jquery -->
    <jsp:include page="../common/bootstrapLink.jsp"></jsp:include>
    <script>
        $(function () {
            //设置 ifame 属性 
            $("iframe").each(function (i,e) {
                //在这里可以动态的设置 iframe 的src
                e.setAttribute("width",(document.body.clientWidth)+"px");
                e.setAttribute("height",(document.body.clientHeight - 45)+"px");
                e.setAttribute("frameborder",0);
                e.setAttribute("scrolling","no");
            });
            
            // 获取 流程数量
            $.ajax({
                url:"activitiController/tasknum",
                dataType:"json",
                type:"GET",
                success:function (data) {
                    $("#deploynum").text(data.deploynum);//可部署的数量
                    $("#starttasknum").text(data.starttasknum);//可发起的数量
                }
            });
            

        });
    </script>
</head>


<body>

<div>




    <ul id="myTab" class="nav nav-tabs">
        <li class="active">
            <a href="#waitingProcessing" data-toggle="tab">待办任务(0)</a>
        </li>
        <li><a href="#mytask" data-toggle="tab">我的任务(0)</a></li>
        <li class="dropdown">
            <a href="#" id="myTabDrop1" class="dropdown-toggle" data-toggle="dropdown">
                队列(0) <b class="caret"></b>
            </a>
            <ul class="dropdown-menu" role="menu" aria-labelledby="myTabDrop1">
                <li><a href="#queue" tabindex="-1" data-toggle="tab">engineering(0)</a></li>
                <li><a href="#queue" tabindex="-1" data-toggle="tab">management(0)</a></li>
                <li><a href="#queue" tabindex="-1" data-toggle="tab">marketing(0)</a></li>
                <li><a href="#queue" tabindex="-1" data-toggle="tab">sales(0)</a></li>
            </ul>
        </li>
        <li><a href="#beInvited" data-toggle="tab">受邀(0)</a></li>
        <li><a href="#alreadyArchived" data-toggle="tab">已归档(0)</a></li>
        <li><a href="#starttask" data-toggle="tab">发起任务(<span id="starttasknum">0</span>)</a></li>
        <li><a href="#deployList" data-toggle="tab">部署任务(<span id="deploynum">0</span>)</a></li>
        <li><a href="#">返回首页</a></li>
    </ul>


    <div id="myTabContent" class="tab-content">

        <div class="tab-pane fade in active" id="waitingProcessing">
            <iframe src="webpage/lcpage/waitingProcessing.jsp"></iframe>
        </div>

        <div class="tab-pane fade" id="mytask">
            <iframe src="webpage/lcpage/mytask.jsp"></iframe>
        </div>

        <div class="tab-pane fade" id="queue">
            <iframe src="webpage/lcpage/queue.jsp"></iframe>
        </div>

        <div class="tab-pane fade" id="beInvited">
            <iframe src="webpage/lcpage/beInvited.jsp"></iframe>
        </div>

        <div class="tab-pane fade" id="alreadyArchived">
            <iframe src="webpage/lcpage/alreadyArchived.jsp"></iframe>
        </div>

        <!-- 发起任务 -->
        <div class="tab-pane fade" id="starttask">
            <iframe src="webpage/lcpage/starttask.jsp"></iframe>
        </div>

        <!-- 部署任务 -->
        <div class="tab-pane fade" id="deployList">
            <iframe src="activitiController/deployList"></iframe>
        </div>


    </div>




</div>



</body>
</html>
