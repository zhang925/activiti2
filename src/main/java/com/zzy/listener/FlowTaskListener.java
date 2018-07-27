package com.zzy.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlowTaskListener implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        String[] experts = (String[]) execution.getVariable("experts");
        String approve = execution.getVariable("approve") == null ? "" :  execution.getVariable("approve").toString();
        List<String> list = new ArrayList<String>();
        Assert.isTrue((1==1),"传参出错");
        if(approve.equals("yes")){
        	Assert.isTrue(experts.length>0,"请选择评审专家");
        	list = Arrays.asList(experts);
        } else if(approve.equals("no")){
        	list.add("0");
        }
        execution.setVariable("pers", list);  
    }  
}  
