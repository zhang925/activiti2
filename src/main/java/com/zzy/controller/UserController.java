package com.zzy.controller;

import com.alibaba.fastjson.JSON;
import com.zzy.model.User;
import com.zzy.service.UserService;
import com.zzy.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/userController")
public class UserController {


    @Autowired
    private UtilService utilService;

    @Autowired
    private UserService userService;


    @RequestMapping(value = "login",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) {
        Object obj[] = new Object[]{request.getParameter("username"),request.getParameter("psd")};
        User u = userService.getModelByParam("from User where username = ? and psd = ? " ,obj);

        String json = "";//JSON.toJSONString(u,true);
        if(u!=null){
            request.getSession().setAttribute("user",u);
            json = "{\"result\":\"success\"}";
        }else{
            json = "{\"result\":\"fail\"}";
        }
        return json;
    }
}
