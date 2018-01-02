package com.zzy.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 本类 解决 ajax 调用 web service 跨域 问题
 */
public class Util_JSON {


    public void a(HttpServletResponse response){
        JSONObject json = new JSONObject();
        json.put("num", 1);
        try {
            response.getWriter().print("successCallbackRunningNum("+json.toString()+")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeJSON(JSONObject json,HttpServletResponse response){
        //JSONObject json= new JSONObject();
        try {
            response.getWriter().print(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
