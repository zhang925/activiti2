//生成日期插件
$(function () {
    $("[flag='datetime']").datetimepicker({
        language: 'zh-CN',//显示中文
        format: 'yyyy-mm-dd HH:ii',//显示格式
        //minView: "month",//设置只显示到月份
        initialDate: new Date() ,//初始化当前日期
        autoclose: true,//选中自动关闭
        todayBtn: true//显示今日按钮

    });
});
//生成 表单的 js
function generForm(id) {//初始化表单
    //1. 获取信息
    var formData;
    $.ajax({
        url:"activitiController/initFormInfo",
        dataType:"json",
        data:{'id':id},
        type:"GET",
        async:false,
        success:function (data) {
            formData = data.result;
        },
        error:function (data) {
            //alert("获取表单信息失败！");
        }
    });
    if(!formData){//无表单信息
        return;
    }
    var flowname = formData.flowname + " -- " + formData.nodename;
    //1. 取 标题
    $("#initFormLabel").html(flowname);
    //2.获取form信息
    var formdata = formData.form;
    $("#initFormContent").html(formdata);

    //给流程添加 完成事件。
    $("#initFormOk").attr("onclick","completeTask(\""+id+"\")");

    // 最后 显示表单
    $('#initForm').modal({keyboard: true});

}