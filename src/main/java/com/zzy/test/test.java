package com.zzy.test;


public class test {
    public static void main(String[] args) throws Exception {
        String s = "adad.12313";
        System.out.println(s.substring(0,s.indexOf(".")+3));

        /*//方法一，画退回的路线，设置退回的条件值
//方法二，退回到指定环节
        @RequestMapping("/rollBackToAssgin.do")
        public void rollBackToAssignWoekFlow(@RequestParam("processInstanceId") String processInstanceId,String destTaskkey){
// 取得当前任务.当前任务节点
            destTaskkey ="usertask1";
            // HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            Map<String, Object> variables;
            ExecutionEntity entity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();
            ProcessDefinitionEntity definition = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService)
                    .getDeployedProcessDefinition(entity.getProcessDefinitionId());
            variables = entity.getProcessVariables();
//当前活动环节
            ActivityImpl currActivityImpl = definition.findActivity(entity.getActivityId());
//目标活动节点
            ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(destTaskkey);
            if(currActivityImpl !=null){
//所有的出口集合
                List<PvmTransition> pvmTransitions = currActivityImpl.getOutgoingTransitions();
                List<PvmTransition> oriPvmTransitions = new ArrayList<PvmTransition>();
                for(PvmTransition transition : pvmTransitions){
                    oriPvmTransitions.add(transition);
                }
//清除所有出口
                pvmTransitions.clear();
//建立新的出口
                List<TransitionImpl> transitionImpls = new ArrayList<TransitionImpl>();
                TransitionImpl tImpl = currActivityImpl.createOutgoingTransition();
                tImpl.setDestination(nextActivityImpl);
                transitionImpls.add(tImpl);

                List<Task> list = taskService.createTaskQuery().processInstanceId(entity.getProcessInstanceId())
                        .taskDefinitionKey(entity.getActivityId()).list();
                for(Task task:list){
                    taskService.complete(task.getId(), variables);
                    historyService.deleteHistoricTaskInstance(task.getId());
                }

                for(TransitionImpl transitionImpl:transitionImpls){
                    currActivityImpl.getOutgoingTransitions().remove(transitionImpl);
                }

                for(PvmTransition pvmTransition:oriPvmTransitions){
                    pvmTransitions.add(pvmTransition);
                }
            }
        }



        //方法三

        private static Logger logger = Logger.getLogger(HandlerPeocessAction.class);
//强制改变当前运行中的流程环节
        @RequestMapping("/rejectTaskTo.do")
        public void rejectTaskTo(@RequestParam("taskId") String taskId,@RequestParam("activityId") String activityId) throws Exception{

            logger.info("开始驳回任务，任务原点:::"+taskId+",驳回至:::"+activityId);
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                    .taskId(taskId).finished().singleResult();
            if(historicTaskInstance !=null){
                throw new Exception("任务已结束，不能进行回退操作！");
            }
            if(activityId == null || "".equals(activityId)){
                throw new Exception("回退目标节点不能为空！");
            }
            long count = taskService.createTaskQuery().taskId(taskId).count();
            if(count == 0){
                throw new Exception("要驳回的任务已不存在！");
            }

            Task currentTask = taskService.createTaskQuery().taskId(taskId).singleResult();
            ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getProcessDefinition(currentTask.getProcessDefinitionId());
            String instanceId = currentTask.getProcessInstanceId();
            ActivityImpl activityImpl = definitionEntity.findActivity(activityId);
            if(activityImpl == null){
                throw new Exception("要驳回的节点不存在！");
            }
            managerService.executeCommand(new JumpActivityCmd(activityId, instanceId)) ; //managerService activiti 七大服务之一
            logger.info("完成驳回操作============");
        }



package com.test;


import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;


        public class JumpActivityCmd implements Command<Object> {

            private String activityId;
            private String processInstanceId;
            private String jumpOrigin;


            public JumpActivityCmd(String activityId, String processInstanceId,String jumpOrigin) {
                this.activityId = activityId;
                this.processInstanceId = processInstanceId;
                this.jumpOrigin = jumpOrigin;
            }

            public JumpActivityCmd(String activityId, String processInstanceId) {
                this(activityId,processInstanceId,"jump");
            }


            public Object execute(CommandContext commandContext) {
                ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);
                executionEntity.destroyScope(jumpOrigin);
                ProcessDefinitionImpl processDefinition = executionEntity.getProcessDefinition();
                ActivityImpl activity = processDefinition.findActivity(activityId);
                executionEntity.executeActivity(activity);
                return executionEntity;
            }


        }*/

    }
}