<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/12/29
  Time: 11:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>队列</title>
    <!-- 引入bootstrap 包含 jquery -->
    <jsp:include page="../common/bootstrapLink.jsp"></jsp:include>
    <script>
        function clearAllData() {
            var psd = prompt("请输入删除密码","");
            if(psd=="zzy"){
                var url = "../activitiController/clearAllData";
                $.ajax({
                    url:url,
                    dataType:"json",
                    type:"POST",
                    async:false,
                    success:function (data) {
                        alert(data.state);
                        parent.location.reload();
                    }
                });
            }else{
                alert("没有权限!");
            }
        }
    </script>
</head>
<body>
<div class="panel panel-info">
    <div class="panel-heading">
        <h1 class="panel-title">队列,暂时做最高权限页面</h1>
    </div>
    <div class="panel-body">
        <div class="table-responsive">

            <button type="button" onclick="clearAllData()" class="btn btn-danger">清除全部流程数据</button>

        </div>
    </div>
</div>





</body>
</html>
