package com.cp.controller;

import com.cp.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AjaxController {
    @RequestMapping("/t1")
    public String test(){
        return "hello";
    }
    @RequestMapping("/a1")
    public void a1(String name, HttpServletResponse response) throws IOException {
        System.out.println("a1:param=>"+name);
        if("cp".equals(name)){
            response.getWriter().println("true");
        }else{
            response.getWriter().println("false");
        }
    }

    @RequestMapping("/a2")
    public List<User> a2(){
        List<User> userList = new ArrayList<User>();

        userList.add(new User("cp",1,"男"));
        userList.add(new User("cp2",2,"男"));
        userList.add(new User("cp3",3,"男"));

        return userList;
    }

    @RequestMapping("/a3")
    public String a3(String name,String pwd){
        String msg = "";
        if(name!=null){
            if("admin".equals(name)){
                msg = "ok";
            }else{
                msg = "用户名错误";
            }
        }
        if(pwd!=null){
            if("123456".equals(pwd)){
                msg = "ok";
            }else{
                msg = "密码错误";
            }
        }
        return msg;
    }

}
