package com.zzy.test;

import java.util.List;
import java.util.Map;

import com.zzy.model.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



/**
 * SpringMVC webservice 客户端测试
 * @author Administrator
 *
 */
public class Test2 {

    public static void main(String[] args) {
        //test01();
        test02();
        //test03();
        //test04();
        //test05();
    }
    /**
     * 查询list
     */
    public static void test01(){
        RestTemplate client=new RestTemplate();
        String url="http://localhost:8888/Sunny/userWebService/getuserbyid/5211314";
        //String result = client.getForObject(url, String.class);
        //System.out.println(result);
        User user = client.getForObject(url, User.class);
        System.out.println(user.getName());
    }
    /**
     * 查询单个实体
     */
    public static void test02(){
        RestTemplate client=new RestTemplate();
        //String url="http://localhost:8888/Sunny/userWebService/getuserbyid2?uid={uid}";
        //String result = client.getForObject(url, String.class);
        //System.out.println(result);
        //User user = client.getForObject(url, User.class,"5211314");
        //String url="http://localhost:8888/Sunny/userWebService/getuserbyid2?uid=5211314";
        String url= "https://docs.heidianer.com/api/catalogue/product/1/";
        /* 把参数放到header中
         User user = new User();
        user.setUid("5211314");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> entity = new HttpEntity<User>(user,headers);*/

        ResponseEntity<User> resp = client.exchange(url, HttpMethod.GET, null, User.class);
        System.out.println("状态码："+resp.getStatusCode() +" 返回实体的name："+ resp.getBody().getName());
    }
    /**
     * 添加
     */
    public static void test03(){
        RestTemplate client=new RestTemplate();
        String url="http://localhost:8888/Sunny/userWebService/saveuser";
        User user = new User();
        user.setName("这是名字！！");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<User>(user,headers);

        ResponseEntity<User> resp = client.exchange(url, HttpMethod.POST, entity, User.class);
        User u = resp.getBody();
        System.out.println("状态码："+resp.getStatusCode()+"返回的："+u.getName());

    }

    /**
     * 修改
     */
    public static void test04(){
        RestTemplate client=new RestTemplate();
        String url="http://localhost:8888/Sunny/userWebService/uptuser";

        HttpHeaders headers = new HttpHeaders();
        //headers.add("username", "newusername"); //参数放到header里面 后台 用 request.getHeader("username")
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 可以是 java实体，String int 等类型的数据
        User user = new User();
        user.setName("这是名字！！");
        HttpEntity<User> entity = new HttpEntity<User>(user,headers);
        ResponseEntity<User> resp = client.exchange(url, HttpMethod.PUT, entity, User.class);
        User u = resp.getBody();
        System.out.println("状态码："+resp.getStatusCode()+" 返回的新名字："+u.getName());
    }
    /**
     * 删除
     */
    public static void test05(){
        RestTemplate client=new RestTemplate();
        String url="http://localhost:8888/Sunny/userWebService/deluser/bbb";
        ResponseEntity<String> resp = client.exchange(url, HttpMethod.DELETE, null, String.class);
        System.out.println("状态码："+resp.getStatusCode()+resp.getBody());
    }

}
