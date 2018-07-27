package com.zzy.listener;

import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;



@SuppressWarnings("serial")
public class BaseTaskListener implements TaskListener {
	
	@Autowired
	private RepositoryServiceImpl repositoryService;
	@Autowired
	private RuntimeService runtimeService;


	@Autowired
	private TaskService taskService;
	
	public BaseTaskListener() {
		super();
		//this.repositoryService = SpringUtils.getBean(RepositoryServiceImpl.class);
		//this.runtimeService = SpringUtils.getBean(RuntimeService.class);
	}

	public void notify(DelegateTask delegateTask) {
		String processInstanceId = delegateTask.getProcessInstanceId();
		String processDefinitionId = delegateTask.getProcessDefinitionId();
		String task = delegateTask.getId();
		String taskKey = delegateTask.getTaskDefinitionKey();
		/**
		 * 审核任务自定义参数
		 */
		String user = delegateTask.getVariable("user") == null ? "" : delegateTask.getVariable("user").toString();//审核人
		String linkurl = delegateTask.getVariable("linkurl") == null ? "" : delegateTask.getVariable("linkurl").toString();//连接地址
		String opinion = delegateTask.getVariable("opinion") == null ? "" : delegateTask.getVariable("opinion").toString();//审核意见
		String approve = delegateTask.getVariable("approve") == null ? "" : delegateTask.getVariable("approve").toString();//审核结果
		String other = delegateTask.getVariable("other") == null ? "" : delegateTask.getVariable("other").toString();//审核结果
		
		//获取流程名
		ProcessDefinition processDefinition =  repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		String processDefinitionName = processDefinition.getName();
		//获取当前节点信息



		
	}

}
