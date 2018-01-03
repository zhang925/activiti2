package com.zzy.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzy.service.UtilService;
import com.zzy.util.Util_Diagrams;
import org.activiti.engine.*;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDiagramCmd;
import org.activiti.engine.impl.interceptor.Command;
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
     * 可以 发起 流程的 列表
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
     * 部署流程
     */
    @RequestMapping("deploytask")
    public void deploytask(String processName, HttpServletResponse response) {
        RepositoryService service = engine.getRepositoryService();
        if (null != processName) {
            service.createDeployment().addClasspathResource("diagrams/" + processName).deploy();
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

    /**
     * 取消 发起 流程
     *
     * @param id
     * @param response
     */
    @RequestMapping("delStarted")
    public void delStarted(String id, HttpServletResponse response) {
        //判断是否 流程 已经执行
        if (1 == 1) {

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

}
