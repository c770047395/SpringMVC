package com.cp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @RequestMapping("/main/main")
    public String goMain(){
        return "main";
    }

    @RequestMapping("/user/goLogin")
    public String goLogin(){
        return "login";
    }
    @RequestMapping("/user/login")
    public String login(HttpSession session, String username, String password){
        System.out.println(username+";"+password);
        if(username.equals("admin")&&password.equals("123456")){
            session.setAttribute("user",username);
            return "main";
        }else{
            return "redirect:/index.jsp";
        }
    }
}
