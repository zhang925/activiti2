package com.zzy.controller;


import com.alibaba.fastjson.JSONObject;
import com.zzy.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/OpenRestController")
public class OpenRestController {
    @Autowired
    private UtilService utilService;

    @RequestMapping(value = "test",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public void test(HttpServletRequest request, HttpServletResponse response){
        JSONObject json = new JSONObject();
        json.put("num", 1);
        writeJson(json,response);

    }

    public  void writeJson(JSONObject json, HttpServletResponse response){
        try {
            response.getWriter().print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
