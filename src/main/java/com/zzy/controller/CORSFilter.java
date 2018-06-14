package com.zzy.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 本拦截器只对webservice 有效 即 wwww.aaa.com/api/**的访问拦截
 * 本来打算做个需要拦截就加注解的方式
 * 但是发现几乎所有的请求都需要拦截，
 * 因此这里采用系统白名单的方式，进行免拦截
 * @Time 2018年6月
 * @Author zzy
 */
@Controller
public class CORSFilter implements Filter {


//    private SystemService systemService;
//    @Autowired
//    public SystemService getSystemService() {
//        return systemService;
//    }
//
//    public void setSystemService(SystemService systemService) {
//        this.systemService = systemService;
//    }

    public void destroy() {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        HttpServletResponse response = (HttpServletResponse) resp;
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE, PUT");
        response.setHeader("Access-Control-Max-Age","3600");
        response.setHeader("Access-Control-Allow-Headers","Authorization,Origin, X-Requested-With, Content-Type, Accept,Access-Token");
        response.setHeader("Access-Control-Expose-Headers", "*");

        chain.doFilter(req, resp);

//       if(isAuthorization(response)){
//            chain.doFilter(req, resp);
//        }else{
//            response.setCharacterEncoding("utf8");
//           JSONObject obj = new JSONObject();
//           obj.put("responsecode","403");
//           obj.put("msg", "no_login");//应前后端要求，这里统一采用 msg no_login 为没有登录，或者服务器session消失。
//            response.getWriter().println(obj.toJSONString());
//        }


    }

    public void init(FilterConfig config) throws ServletException {

    }


    /**
     * 在前端的时候有时候不需要登陆就要拿到一些数据
     *  true 表示不需要拦截，false 需要拦截
     * @return
     */
    public boolean isHandle(HttpServletRequest request){
        String requestUrl = request.getRequestURI();//获取当前请求的url
        requestUrl = requestUrl.replace("/","");
        boolean flag = false;

        //解决 filter 中注入  systemService 失败
     /*  ServletContext sc = request.getSession().getServletContext();
        XmlWebApplicationContext cxt = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sc);
        if(cxt != null && cxt.getBean("systemService") != null){
            systemService = (SystemService) cxt.getBean("systemService");
        }
        if(systemService!=null){
            //从数据库读取白名单
            List<Map<String, Object>> list = systemService.findForJdbc(" select authority_uri from t_s_authority_white ");
            if(list!=null && list.size()>0){
                for(Map<String, Object> map : list){
                    Object  temp =  map.get("authority_uri");
                    if(temp!=null){//linux 对 / 和 \ 很排斥啊，这里 去掉后变成小写比较
                        String white = temp.toString().trim();
                        white = white.toString().replace("/","");
                        requestUrl = requestUrl.toLowerCase();
                        white = white.toLowerCase();
                        if(requestUrl.contains(white)){//和白名单匹配
                            flag = true;
                            break;
                        }
                    }
                }
            }
        }*/
        //系统免过滤的白名单，在单点登录的sso.properties的white.list配置
      /* String whiteList = UtilSht.getPripertyPath("sso.properties",null,"white.list");

        if(whiteList!=null && !"".equals(whiteList)){
            whiteList = whiteList.trim();
            String str [] = whiteList.split(";");
            if(str!=null && str.length>0){
                for(String temp:str){
                    if( requestUrl.indexOf(temp)!=-1){//找到该地址
                        flag = true;
                        break;
                    }
                }
            }

        }
        */
        return flag;//不需要拦截
    }



    public boolean isAuthorization( HttpServletResponse response){


        /*   HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(isHandle( request)){//是白名单
            return true;
        }
        String authorization = request.getHeader("Authorization");//token
        //"SESSIONID=947CDE0762299E1241430790C588A7F3; aaa=aaa"
        String SESSIONID  = "";
        if (authorization != null && !"".equals(authorization)) { // cookie 里面是不允许 ; 存在的，所以这里可以放心截取
            String cookies[] = authorization.split(";");
            for (String cookie : cookies){
                String cooki[] = cookie.split("=");
                if(cooki!=null && cooki.length>1){//说明是正常的cookie
                    if(cooki[0].trim().equals("SESSIONID")){
                        SESSIONID = cooki[1];
                    }
                }
            }
        }
        if("".equals(SESSIONID)){
            return false;
        }
        MySessionContext mySessionContext = MySessionContext.getSingleInstance();
        HttpSession session = mySessionContext.getSession(SESSIONID);
        if(session!=null){//说明sesssion没有注销
            Object obj = session.getAttribute("restuser");//这个是webservice用户
            Object obj2 = session.getAttribute("LOCAL_CLINET_USER");//这个是系统用户，两者是同一个用户
            if(obj==null){
                return false;
            }
            TSUser tsUser = (TSUser)obj;
            if(tsUser==null || tsUser.getId()==null){
                return false;
            }else{
                return true;//从以前的登陆信息中获取登陆后的用户
            }
        }*/
        return false;
    }
}
