package com.zzy.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.zzy.dao.impl.SqlDaoUtil;
import com.zzy.model.MsgModel;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zzy.service.UtilService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping(value = "/utilController")
public class UtilController {

	@Autowired
	private UtilService utilService;


	@RequestMapping(value = "gettable",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response) {

		List list = utilService.getListBySql(" SELECT * FROM a_test ");

        String json = JSON.toJSONString(list,true);
		return json;
	}
	/**
	 * hibernate 的 criteria 查询 [Query] 查询 
	 *@Autowired
	 * in  的 解决办法
	 */
	public void testIN查询(){
		utilService.executeSql("");
		/*Session session = utilService.gethibernatesession();
		String hql="from District where id in (:idlist)"; 
		Query query = session.createQuery(hql);
		Object a[] ={1,2,3};
		query.setParameterList("idlist", a); 
		List<District> list = new ArrayList<District>();
		list = query.list();
		session.flush();
		session.close();*/
	}
}
