package com.zzy.util;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 如果设置了监听类就不
 * 需要在流程xml中设置Assignee的值
 */
public class Util_TaskListener { // implements TaskListener

   /* public void notify(DelegateTask delegateTask) {
        //这里可以根据登录人 设置下一步 审批人 是谁
        delegateTask.setAssignee("审批人");
    }*/
}
