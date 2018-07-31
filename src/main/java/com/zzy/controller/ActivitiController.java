package com.zzy.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzy.service.UtilService;
import com.zzy.util.Util_Diagrams;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDiagramCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.*;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/activitiController")
public class ActivitiController {

    @Resource
    private ProcessEngine engine;

    @Autowired
    private UtilService utilService;

	/*@Resource
	private RuntimeService runtimeService;

	@Resource
	private IdentityService identityService;

	public void test(){
		identityService.setAuthenticatedUserId("12");//用来设置启动流程的人员ID

		//插入 流程
		Map variables = new HashMap();
		String businessKey = "";
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave",businessKey,variables);
		String ProcessInstanceId = processInstance.getId();//流程
		//查看自己为完成的任务
		ProcessInstanceQuery query =  runtimeService.createProcessInstanceQuery().processDefinitionKey("leave").active().orderByProcessInstanceId().desc();
		List<ProcessInstance> list = query.list();
	}*/

    /**
     * 列出所有流程模板
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView list(ModelAndView mav) {
        mav.addObject("list", Util_Diagrams.list());
        mav.setViewName("views/process/template");
        return mav;
    }

    /**
     * 部署流程
     */
    @RequestMapping("deploy")
    public ModelAndView deploy(String processName, ModelAndView mav) {

        RepositoryService service = engine.getRepositoryService();

        if (null != processName)
            service.createDeployment()
                    .addClasspathResource("diagrams/" + processName).deploy();

        List<ProcessDefinition> list = service.createProcessDefinitionQuery()
                .list();

        mav.addObject("list", list);
        mav.setViewName("views/process/deployed");
        return mav;
    }

    /**
     * 已部署流程列表
     */
    @RequestMapping("deployed")
    public ModelAndView deployed(ModelAndView mav) {

        RepositoryService service = engine.getRepositoryService();

        List<ProcessDefinition> list = service.createProcessDefinitionQuery()
                .list();

        mav.addObject("list", list);
        mav.setViewName("views/process/deployed");

        return mav;
    }

    /**
     * 启动一个流程实例
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("start")
    public ModelAndView start(String id, ModelAndView mav) {

        RuntimeService service = engine.getRuntimeService();

        service.startProcessInstanceById(id);

        List<ProcessInstance> list = service.createProcessInstanceQuery()
                .list();

        mav.addObject("list", list);
        mav.setViewName("views/process/started");

        return mav;
    }

    /**
     * 所有已启动流程实例
     */
    @RequestMapping("started")
    public ModelAndView started(ModelAndView mav) {

        RuntimeService service = engine.getRuntimeService();
        List<ProcessInstance> list = service.createProcessInstanceQuery().list();
        mav.addObject("list", list);
        mav.setViewName("views/process/started");

        return mav;
    }

    /**
     * 任务列表
     *
     * @param mav
     * @return
     */
    @RequestMapping("task")
    public ModelAndView task(ModelAndView mav) {
        TaskService service = engine.getTaskService();
        List<Task> list = service.createTaskQuery().list();
        mav.addObject("list", list);
        mav.setViewName("views/process/task");
        return mav;
    }

    /**
     * 完成任务
     */
    @RequestMapping("complete")
    public ModelAndView complete(ModelAndView mav, String id) {

        TaskService service = engine.getTaskService();

        service.complete(id);

        return new ModelAndView("redirect:task");
    }

    /**
     * 所有已启动流程实例 图
     */
    @RequestMapping("graphics")
    public void graphics(String definitionId, String instanceId,
                         String taskId, ModelAndView mav, HttpServletResponse response)
            throws IOException {

        response.setContentType("image/png");
        Command<InputStream> cmd = null;

        if (definitionId != null) {
            cmd = new GetDeploymentProcessDiagramCmd(definitionId);
        }

        if (instanceId != null) {
            cmd = new ProcessInstanceDiagramCmd(instanceId);
        }

        if (taskId != null) {
            Task task = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
            cmd = new ProcessInstanceDiagramCmd(
                    task.getProcessInstanceId());
        }

        if (cmd != null) {
            InputStream is = engine.getManagementService().executeCommand(cmd);
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = is.read(b, 0, 1024)) != -1) {
                response.getOutputStream().write(b, 0, len);
            }
        }
    }


    /**
     * 列出所有流程模板
     */
    @RequestMapping(value = "getFlowList")
    public ModelAndView getFlowList(ModelAndView mav) {
        mav.addObject("list", Util_Diagrams.list());
        mav.setViewName("views/process/template");
        return mav;
    }





    /*-------------- 分界线 自己写的 流程 --------------------------------------------------------*/


    @RequestMapping("goLChome")
    public ModelAndView goLChome(String id, ModelAndView mav) {
        mav.setViewName("lchome/home");
        return mav;
    }


    @RequestMapping(value = "tasknum", method = RequestMethod.GET)
    public void tasknum(HttpServletResponse response) {
        JSONObject json = new JSONObject();

        //可部署的流程数
        String deployList[] = Util_Diagrams.list();
        int deploynum = 0;
        if (deployList != null) {
            deploynum = deployList.length;
        }
        json.put("deploynum", deploynum);

        //可发起的 流程数
        RepositoryService service = engine.getRepositoryService();
        List<ProcessDefinition> starttasklist = service.createProcessDefinitionQuery().list();
        int starttasknum = 0;
        if (starttasklist != null) {
            starttasknum = starttasklist.size();
        }
        json.put("starttasknum", starttasknum);

        //获取 已经发起 流程数
        RuntimeService service2 = engine.getRuntimeService();
        List<ProcessInstance> allreadyStartlist = service2.createProcessInstanceQuery().list();
        int allreadyStartnum = 0;
        if (allreadyStartlist != null) {
            allreadyStartnum = allreadyStartlist.size();
        }
        json.put("allreadyStartnum", allreadyStartnum);

       //获取代办数量。
        int daibannum = getDaiBanNum("小马哥");//这里暂定为小马哥
        json.put("daibannum", daibannum);

        //已经归档的数量
        int flowHistorynum = getFlowHistorynum();
        json.put("flowHistorynum", flowHistorynum);

        try {
            response.getWriter().print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 查看 可以 部署 的 流程 文件列表【这个权限最好归 管理员】
     *
     * @return
     */
    @RequestMapping(value = "deployList", method = RequestMethod.GET)
    public ModelAndView deployList(ModelAndView mav) {
        String deployList[] = Util_Diagrams.list();
        mav.addObject("list", deployList);
        int num = 0;
        if (deployList != null) {
            num = deployList.length;
        }
        mav.addObject("num", num);
        mav.setViewName("lcpage/deployList");
        return mav;
    }


    /**
     * 跳转到 发起 流程的 列表
     *
     * @param mav
     * @return
     */
    @RequestMapping("gostarttaskList")
    public ModelAndView gostarttaskList(ModelAndView mav) {
        //RepositoryService service = engine.getRepositoryService();
        //List<ProcessDefinition> list = service.createProcessDefinitionQuery().list();
        //mav.addObject("list", list);
        mav.setViewName("lcpage/starttask");
        return mav;
    }

    /**
     * 可以 发起 流程的 列表
     * @return
     */
    @RequestMapping("starttaskList")
    public void starttaskList(HttpServletRequest request, HttpServletResponse response) {
        RepositoryService service = engine.getRepositoryService();
        List<ProcessDefinition> list = service.createProcessDefinitionQuery().list();
        //这是个接口 所以没有办法 转换成 json 对象。这里 先暂时 手动转换
        List list1 = new ArrayList();
        if (list != null && list.size() > 0) {
            for (ProcessDefinition p : list) {
                Map map = new HashMap();
                map.put("id", p.getId());
                //获取 部署时间
                List deploytimelist = utilService.getListBySql("select DEPLOY_TIME_ from act_re_deployment where ID_=" + p.getDeploymentId());
                String date = deploytimelist.get(0).toString();
                map.put("deploytime", date.substring(0, 19));
                map.put("name", p.getName());
                list1.add(map);
            }
        }

        JSONObject json = new JSONObject();
        json.put("list", list1);
        try {
            response.getWriter().print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 已经 发起 流程的 列表
     * @return
     */
    @RequestMapping("allreadyStarttaskList")
    public void allreadyStarttaskList(HttpServletRequest request, HttpServletResponse response) {
        RepositoryService service01 = engine.getRepositoryService();
        //获取 已经发起任务的列表
        RuntimeService service = engine.getRuntimeService();
        List<ProcessInstance> list = service.createProcessInstanceQuery().list();

        List list1 = new ArrayList();
        if (list != null && list.size() > 0) {
            for (ProcessInstance p : list) {
               Map map = new HashMap();
               map.put("id", p.getId());
                //获取 发起 时间
               List deploytimelist = utilService.getListBySql("select CREATE_TIME_ from act_ru_task where PROC_INST_ID_ =" +  p.getId() );
               String date = deploytimelist.get(0).toString();
               map.put("createtime", date.substring(0, 19));
               ProcessDefinition p01 = service01.createProcessDefinitionQuery().processDefinitionId(p.getProcessDefinitionId()).singleResult();
               map.put("name", p01.getName());
               map.put("pid", p01.getId());//PROC_INST_ID_ 流程的ID
               list1.add(map);
            }
        }




        JSONObject json = new JSONObject();
        json.put("list", list1);
        try {
            response.getWriter().print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 部署流程，需要报表单部署上
     */
    @RequestMapping("deploytask")
    public void deploytask(String processName, HttpServletResponse response) {
        //要判断，是否已经部署，如果已经部署， 部署的流程是否已经结束， 删除还是保留，都是问题

        RepositoryService service = engine.getRepositoryService();
        if (null != processName) {
            if(processName.equals("Bxsp.bpmn")){
                service.createDeployment().addClasspathResource("diagrams/" + processName)
                        .addClasspathResource("form/bxsp.form")
                        .deploy();
            }else if(processName.equals("Hello.bpmn")){//请假表单
                service.createDeployment().addClasspathResource("diagrams/" + processName)
                        .addClasspathResource("form/qingjia.form")
                        .addClasspathResource("form/qingjia_audit.form")
                        .deploy();
            }else{
                service.createDeployment().addClasspathResource("diagrams/" + processName).deploy();
            }
        }
        JSONObject json = new JSONObject();
        json.put("state", "success");
        try {
            response.getWriter().print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 启动一个流程实例 josn
     */
    @RequestMapping("starttask")
    public void starttask(String id, HttpServletResponse response) {
        RuntimeService service = engine.getRuntimeService();
        service.startProcessInstanceById(id);
        //List<ProcessInstance> list = service.createProcessInstanceQuery().list();
        JSONObject json = new JSONObject();
        json.put("state", "已经成功启动流程！");
        try {
            response.getWriter().print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 任务列表 代办任务
     * @return
     */
    @RequestMapping("myTaskList")
    public void task(HttpServletRequest request,HttpServletResponse response) {
        String userid = request.getParameter("userid");
        TaskService service = engine.getTaskService();
        List<Task> list = service.createTaskQuery().list();
        //如果传递了 userid 则按照 userid 来 查询
        if(list!=null && userid!=null && !"".equals(userid) ){
            for(Task task : list){
                if(!userid.equals(task.getAssignee())){
                    list.remove(task);
                }
            }
        }

        List listResult = new ArrayList();
        //这里 要根据不同的流程进行分组。
        //第一次 取出 流程的 name 或者 ID
        List flowIDList = new ArrayList();
        for(int i=0;i< list.size();i++){
            Task task = list.get(i);
            String flowID = task.getProcessDefinitionId();
            if(!flowIDList.contains(flowID)){//流程ID
                flowIDList.add(flowID);
            }
        }
        for(Object flowid : flowIDList){//分组
            List temList = new ArrayList();
            for(Task task : list){
                String thisFlowId = task.getProcessDefinitionId();
                if(thisFlowId.equals(flowid.toString())){
                    // json 不支持 转换 间接放到 Map  中去
                    Map map = new HashMap();
                    map.put("id",task.getId());//任务ID
                    ProcessDefinition p01 = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(thisFlowId).singleResult();
                    map.put("pname",p01.getName()); // 流程名字
                    map.put("piid",task.getProcessInstanceId()); // 部署信息的ID
                    map.put("assignee",task.getAssignee()); // 任务执行人
                    map.put("nodeid",task.getTaskDefinitionKey()); // 当前节点ID
                    map.put("nodename",task.getName()); // 节点名称
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    map.put("createtime",sdf.format(task.getCreateTime()));//任务创建时间
                    map.put("pid",task.getProcessDefinitionId());//流程ID 长的 是 流程启动的id
                    temList.add(map);
                }
            }
            //内部循环一次结束后
            Map map2 = new HashMap();
            map2.put("num",temList.size());
            map2.put("result",temList);
            listResult.add(map2);
            temList = new ArrayList();//重置一次list
        }



        JSONObject json = new JSONObject();
        json.put("list", listResult);
        try {
            response.getWriter().print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 完成任务
     */
    @RequestMapping("initFormInfo")
    public void initFormInfo(HttpServletRequest request,HttpServletResponse response) {
        TaskService taskService = engine.getTaskService();
        FormService formService= engine.getFormService();
        String taskID = request.getParameter("id");//任务ID
        Task task = taskService.createTaskQuery().taskId(taskID).singleResult();//获取任务实体
        if(task==null){
            return;
        }
        //获取流程实体
        String flowID = task.getProcessDefinitionId();//流程ID
        ProcessDefinition processDefinition = engine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionId(flowID).singleResult();
        //获取流程的表单组件
        //ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), formProperties);
        boolean isHaveFormKey = processDefinition.hasStartFormKey();//判断是否有from
        Object form = "";
        List formData = new ArrayList();
        if(isHaveFormKey){
            //StartFormData startFormData = formService.getStartFormData(flowID);
            //String formKey = startFormData.getFormKey();//表单的key
            //List<FormProperty> formProperties = startFormData.getFormProperties();
            form = formService.getRenderedStartForm(flowID);

            //查看到那个节点了。
           /* String delployid = task.getExecutionId();
            String sql = "select REV_ from act_ru_execution WHERE ID_="+delployid ;
            List list = utilService.getListBySql(sql);
            Object nodeNum = "1";
            if(list!=null && list.size()>0){
                nodeNum = list.get(0);
            }*/

            //首先判断是否 有之前的数据
            String tableName = "a_oa_leave";//根据自己的配置文件读取相应的table名字,这里不做处理，只有一个。
            String existSql = "SELECT * FROM "+tableName+" WHERE TASK_ID='"+taskID+"' AND PRO_ID = '"+flowID+"'";
            formData = utilService.getListBySql(existSql);

        }


        Map map = new HashMap();
        map.put("flowid",processDefinition.getId());//流程名字
        map.put("flowname",processDefinition.getName());//流程名字
        map.put("nodename",task.getName());//当前节点
        map.put("state","success");
        map.put("form",form);
        map.put("formData",formData);

        JSONObject json = new JSONObject();
        json.put("result", map);
        try {
            response.getWriter().print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 完成任务
     */
    @RequestMapping("completeTask")
    public void complete(HttpServletRequest request,HttpServletResponse response) {
        FormService formService= engine.getFormService();
        TaskService service = engine.getTaskService();
        //获取流程的表单组件
        String flowid = request.getParameter("flowid");//流程ID
        String taskid = request.getParameter("id");//任务ID
        String formData = request.getParameter("form");//表单信息
        String loginUser = "当前登陆人待定";//当前登陆人,这里暂时没有做，固定一个数值
        String loginUserID = "loginUserID001";//当前登陆人,这里暂时没有做，固定一个数值
        //ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), formProperties);
        //Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        //Object renderedTaskForm = formService.getRenderedTaskForm(taskid);//根据taskid 能获取对应表单 内容 不含 值
        String tableName = "a_oa_leave";//根据自己的配置文件读取相应的table名字,这里不做处理，只有一个。
        String executeSql  = "";
        String paramNames = "(";
        String paramValues = "(";
        if(formData!=null && formData!=""){
            //首先判断是否 有之前的数据
            String existSql = "SELECT * FROM "+tableName+" WHERE TASK_ID='"+taskid+"' AND PRO_ID = '"+flowid+"'";
            List list = utilService.getListBySql(existSql);

            String dataArr[] = formData.split("&=&");
            for(int i=0;i<dataArr.length;i++){
                String param = dataArr[i];
                String paremArr[] = param.split("=&");
                String paramName = paremArr[0];//参数名
                String paramValue = "";
                if(paremArr.length == 2){
                    paramValue = paremArr[1];//参数值
                }
                if(list!=null && list.size()>0){
                    executeSql += paramName+"="+"'"+paramValue+"',";
                }else{
                    paramNames += ""+paramName+",";
                    paramValues += "'"+paramValue+"',";
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String newDate = sdf.format(new Date());
            if(list!=null && list.size()>0){//存在，修改
                executeSql = "UPDATE "+ tableName + " SET " + executeSql +" UPDATE_TIME ='"+newDate+"' WHERE TASK_ID='"+taskid+"' AND PRO_ID = '"+flowid+"'";
            }else{//插入
                paramNames +="TASK_ID,PRO_ID,USER,USER_ID,CREATE_TIME)";
                paramValues += "'"+taskid+"','"+flowid+"','"+loginUser+"','"+loginUserID+"','"+newDate+"')";
                executeSql = "INSERT INTO "+ tableName + paramNames + " VALUES "+paramValues;
            }
            utilService.executeSql(executeSql);//执行插入或者修改操作
        }
        service.complete(taskid);
        JSONObject json = new JSONObject();
        json.put("state", "任务已经完成！");
        try {
            response.getWriter().print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 撤回任务节点,退回
     * @param request
     * @param response
     */
    @RequestMapping("revokeTask")
    public void revokeTask(HttpServletRequest request,HttpServletResponse response){
        String taskId = request.getParameter("id");//任务ID
        String flowId = request.getParameter("pid");//流程ID
        String activityId = request.getParameter("backId");//退回的目标节点ID
        String nodeid = request.getParameter("nodeid");//当前节点ID
        //这里假设往前退回1
        String state = "";
        PrintWriter out = null;
        JSONObject json = new JSONObject();
        try {
            out =  response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HistoricTaskInstance historicTaskInstance = engine.getHistoryService().createHistoricTaskInstanceQuery()
                .taskId(taskId).finished().singleResult();
        if(historicTaskInstance !=null){
            state = "任务已结束，不能进行回退操作！";
            json.put("state",state);
            out.print(json.toString());
            out.close();
            return;
        }
        if(activityId == null || "".equals(activityId)){
            state = "回退目标节点不能为空！";
            json.put("state",state);
            out.print(json.toString());
            out.close();
            return;
        }
        long count = engine.getTaskService().createTaskQuery().taskId(taskId).count();
        if(count == 0){
            state = "要驳回的任务已不存在！";
            json.put("state",state);
            out.print(json.toString());
            out.close();
            return;
        }

        Task currentTask = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) engine.getRepositoryService()).getProcessDefinition(currentTask.getProcessDefinitionId());
        //String instanceId = currentTask.getProcessInstanceId();
        ActivityImpl activityImpl = definitionEntity.findActivity(activityId);
        if(activityImpl == null){
            state = "要驳回的节点不存在！";
            json.put("state",state);
            out.print(json.toString());
            out.close();
            return;
        }


        TaskService taskService = engine.getTaskService() ;
        Task task = taskService.createTaskQuery().taskId(Arrays.asList(taskId.split(",")).get(0)).singleResult();
        String executionId = task.getExecutionId();
        String lcdyId = task.getProcessDefinitionId();
        //String processTaskId = task.getId();//当前节点id
        String processTaskId = task.getTaskDefinitionKey();
        RepositoryService repositoryService = engine.getRepositoryService();
        ReadOnlyProcessDefinition processDefinitionEntity = (ReadOnlyProcessDefinition) repositoryService.getProcessDefinition(lcdyId);
        ActivityImpl destinationActivity = (ActivityImpl) processDefinitionEntity.findActivity(activityId);//目标节点
        ActivityImpl currentActivity = (ActivityImpl)processDefinitionEntity.findActivity(processTaskId);//当前节点
        engine.getManagementService().executeCommand( new JumpTaskCmd(executionId,destinationActivity,currentActivity) );
        json.put("state", "已经撤回！");
        out.print(json.toString());
        out.close();
    }


    public class JumpTaskCmd implements Command<Void>{
        private String executionId;
        private ActivityImpl desActivity;
        private Map<String, Object> paramvar;
        private ActivityImpl currentActivity;

        public Void execute(CommandContext commandContext) {
            ExecutionEntityManager executionEntityManager = Context.getCommandContext().getExecutionEntityManager();
            // 获取当前流程的executionId，因为在并发的情况下executionId是唯一的。
            ExecutionEntity executionEntity = executionEntityManager.findExecutionById(executionId);
            executionEntity.setVariables(paramvar);
            executionEntity.setEventSource(this.currentActivity);
            executionEntity.setActivity(this.currentActivity);
            // 根据executionId 获取Task
            Iterator<TaskEntity> localIterator = Context.getCommandContext().getTaskEntityManager()
                    .findTasksByExecutionId(this.executionId).iterator();

            while (localIterator.hasNext()) {
                TaskEntity taskEntity = (TaskEntity) localIterator.next();
                // 触发任务监听
                //taskEntity.fireEvent("complete");
                // 删除任务的原因
                Context.getCommandContext().getTaskEntityManager().deleteTask(taskEntity, "completed", false);
            }
            executionEntity.executeActivity(this.desActivity);
            return null;
        }

        /**
         * 构造参数 可以根据自己的业务需要添加更多的字段
         * @param executionId
         * @param desActivity
         * @param currentActivity
         */
        public JumpTaskCmd(String executionId, ActivityImpl desActivity,
                           ActivityImpl currentActivity) {
            this.executionId = executionId;
            this.desActivity = desActivity;
            this.currentActivity = currentActivity;
        }
    }



    /**
     * 已经 归档 的 任务
     * @param request
     * @param response
     */
    @RequestMapping("flowHistory")
    public void flowHistory(HttpServletRequest request,HttpServletResponse response){
        //List<HistoricActivityInstance> list = engine.getHistoryService().createHistoricActivityInstanceQuery()
         //       .orderByHistoricActivityInstanceStartTime().asc().list();

        List<HistoricProcessInstance> list =  engine.getHistoryService().createHistoricProcessInstanceQuery().list();

        List resultList = new ArrayList();
        if(list!=null && list.size()>0){
            for(HistoricProcessInstance hp: list){
                if(hp.getEndTime()!=null){//说明已经 归档了
                    Map map = new HashMap();
                    map.put("startActivitiId", hp.getStartActivityId());//activity的ID
                    map.put("id", hp.getId());
                    //String a = hp.getSuperProcessInstanceId();//这个和hp的ID一样 act_hi_procinst 的id//HistoricProcessInstanceEntity
                    map.put("pid", hp.getProcessDefinitionId());//流程ID
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    map.put("starttime", sdf.format(hp.getStartTime()));// 开始时间
                    map.put("endtime", sdf.format(hp.getEndTime()));//  结束时间
                    Long d = hp.getEndTime().getTime() - hp.getStartTime().getTime() ;
                    double timetemp2 = (double)( d / 1000 ); //单位秒
                    String lasttime = "";
                    if(timetemp2/60<1){//单位为秒
                        lasttime = timetemp2 + "秒";
                    }else if(timetemp2/60>=1 && timetemp2/60/60<=1 ){//在以分钟为单位的时间完成
                        lasttime = timetemp2/60 + "分";
                    }else if(timetemp2/60/60>1 && timetemp2/60/60/24<=1){//小时为单位
                        lasttime = timetemp2/60/60 + "小时";
                    }else if(timetemp2/60/60/24>1){//天
                        lasttime = timetemp2/60/60/24 + "天";
                    }
                    map.put("lasttime", lasttime.substring(0,lasttime.indexOf(".")+3));//  经历 时间

                    //Date lastdate = hp.getEndTime() - hp.getStartTime();
                    ProcessDefinition p = engine.getRepositoryService().createProcessDefinitionQuery()
                            .processDefinitionId(hp.getProcessDefinitionId()).singleResult();
                    map.put("name",p.getName());//流程的名字
                    resultList.add(map);
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("list", resultList);
        try {
            response.getWriter().print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }













    /**
     * 取消部署 流程
     * @param id
     * @param response
     */
    @RequestMapping("delDeployed")
    public void delDeployed(String id,String flag, HttpServletResponse response) {
        PrintWriter out = null;
        JSONObject json = new JSONObject();

        //判断是否 流程 已经执行
        RepositoryService service = engine.getRepositoryService();
       String state = "";
        try {
            out = response.getWriter();
            //根据 流程 ID 获取当前流程
            ProcessDefinition processDefinition = service.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
            //查询该流程是否已经，启动任务,如果忽略启动，可采取
            //service.deleteDeployment(processDefinition.getDeploymentId(),true);
            //方式删除
            //如果流程 已经启动会生成 一个task
            /*
            // 这种 方法 采用循环 数量大的 情况下 不可取
            List<Task> taskList = engine.getTaskService().createTaskQuery().list();
            for(Task t : taskList){
                if (t.getProcessDefinitionId().equals(id)){//说明 该流程已经启动
                    break;
                }
            }*/
            List list = utilService.getListBySql("select ID_ from act_ru_task where PROC_DEF_ID_ ='"+id+"'");
            if(list!=null && list.size()>0){//流程已经启动 ， 不可以删除
                if(flag!=null && "qz".equals(flag)){//强制删除
                    service.deleteDeployment(processDefinition.getDeploymentId(),true);//强制 级联删除
                    state = "已经清除相关启动的流程，并且取消部署!";
                }else {
                    state = "该流程已经启动，不能撤回！";
                }
            }else{//说明 流程没有启动可以删除
                state = "已经取消部署!";
                service.deleteDeployment(processDefinition.getDeploymentId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        json.put("state", state);
        out.print(json.toString());
    }

    /*****      ***这个方法不可 取 暂时 没 弄 出来 有待 完善*****
     * 取消 发起 流程
     * @param response
     */
    @RequestMapping("delStarted")
    public void delStarted(HttpServletRequest request,HttpServletResponse response) {
        String id,  flag ,pid;
        id = request.getParameter("id");
        pid = request.getParameter("pid");
        flag = request.getParameter("id");


        JSONObject json = new JSONObject();
        String state = "";
        RepositoryService service = engine.getRepositoryService();

        //RuntimeService service2 = engine.getRuntimeService();
        ///List list01 = service2.createProcessInstanceQuery().list();//启动流程后的关联表
        //Task task = engine.getTaskService().createTaskQuery().taskId("").singleResult();
        //engine.getTaskService().deleteTask("2474",true);
        // engine.getTaskService().delegateTask("2008","小马哥");
        //判断是否 流程 已经执行 act_hi_taskinst
        // PROC_INST_ID_ = id 是 act_ru_execution 表的 ID_
        // PROC_DEF_ID_  = pid 流程id
        String sql = "select ID_ from act_hi_taskinst where PROC_DEF_ID_ = '"+pid+"' and  PROC_INST_ID_=" + id +" and DELETE_REASON_ = 'completed'" ;
        List list = utilService.getListBySql(sql);

        //根据 流程 ID 获取当前流程
        ProcessDefinition processDefinition = service.createProcessDefinitionQuery().processDefinitionId(pid).singleResult();
        String dpid = processDefinition.getDeploymentId();
        if(list!=null && list.size()>0){//任务已经执行不能删除
            if(flag!=null && "qz".equals(flag)){//强制删除
                service.deleteDeployment(dpid,true); //这个方法不可 取 暂时 没 弄 出来 有待 完善
                state = "已经清除相关启动的流程，并且取消部署!";
            }else {
                state = "该流程已经启动，不能撤回！";
            }
        }else{//执行 普通删除
            service.deleteDeployment(dpid,true);//这个方法不可 取 暂时 没 弄 出来 有待 完善
            state = "已经清除相关启动的流程，并且取消部署!";
        }
       try {
           PrintWriter out =  response.getWriter();
           json.put("state", state);
           out.print(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除所有流程信息【慎用】
     */
    @RequestMapping("clearAllData")
    public void clearAllData(HttpServletResponse response) {
	/*



		Task task = engine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		TaskService service1=engine.getTaskService();
		RuntimeService service2 = engine.getRuntimeService();

            List list1 = service.createModelQuery().list();
            List list2 = service.createProcessDefinitionQuery().list();//获取流程
            List list3 = service.createDeploymentQuery().list();//部署信息

            RuntimeService service2 = engine.getRuntimeService();
            List list01 = service2.createProcessInstanceQuery().list();//启动流程后的关联表

            List list02 = service2.createExecutionQuery().list();
            System.out.println(123);

		*/
	    //后期 可以把 这个 删除 语句 放到 存储 过程去,直接调用 一个sql就行了
        String tables[] = {
                    "act_ge_bytearray","act_hi_actinst","act_hi_comment",
                    "act_hi_detail","act_hi_identitylink","act_hi_procinst",
                    "act_hi_taskinst","act_hi_varinst","act_re_deployment",
                    "act_re_model","act_ru_identitylink","act_ru_event_subscr",
                    "act_ru_identitylink","act_ru_variable","act_ru_job",
                    "act_ru_task","act_ru_execution","act_re_procdef",
                    "act_hi_attachment"
        };
        for(String table : tables){
            String sql ="DELETE FROM " + table ;
            utilService.executeSql(sql);
        }
        JSONObject json = new JSONObject();
        json.put("state", "流程数据，清除完毕！");
        try {
            response.getWriter().print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public int getDaiBanNum(String userid){
        int num = 0;
        //获取 代办数量
        List<Task> list = engine.getTaskService().createTaskQuery().list();
        if(list!=null){
            for(Task task : list){
               if(userid.equals(task.getAssignee())){
                   num++;
               }
            }
        }
        return num;
    }

    /**
     * 这里 查询的是 全部 已经 归档 的实际上 需要 查询 某 个人的
     * @return
     */
    public int getFlowHistorynum(){
        List<HistoricProcessInstance> flowHistoryList =  engine.getHistoryService().createHistoricProcessInstanceQuery().list();
        int flowHistorynum = 0;
        if(flowHistoryList!=null){//act_hi_procinst where END_TIME_ is not null
            for(HistoricProcessInstance h : flowHistoryList){
                if(h.getEndTime()!=null){
                    flowHistorynum++;
                }
            }
        }
        return flowHistorynum;
    }


/*
    public RepositoryService repositoryService;

    public String test(String taskId,String destinationTaskId){//新的退回



        TaskService taskService = engine.getTaskService() ;
        Task task = taskService.createTaskQuery().taskId(Arrays.asList(taskId.split(",")).get(0)).singleResult();
        String executionId = task.getExecutionId();
        String lcdyId = task.getProcessDefinitionId();
        //String processTaskId = task.getId();//当前节点id
        String processTaskId = task.getTaskDefinitionKey();

        ReadOnlyProcessDefinition processDefinitionEntity = (ReadOnlyProcessDefinition) repositoryService.getProcessDefinition(lcdyId);
        ActivityImpl destinationActivity = (ActivityImpl) processDefinitionEntity.findActivity(destinationTaskId);//目标节点
        ActivityImpl currentActivity = (ActivityImpl)processDefinitionEntity.findActivity(processTaskId);//当前节点
        engine.getManagementService().executeCommand(new JumpTaskCmd(executionId,destinationActivity,currentActivity));

        return destinationTaskId;
    }*/

}
