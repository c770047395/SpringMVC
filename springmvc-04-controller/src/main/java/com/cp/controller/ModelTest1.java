package com.cp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class ModelTest1 {

    //使用Servlet原生API进行转发&重定向
    @RequestMapping("/m1/t1")
    public void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        System.out.println(session.getId());
        session.setAttribute("msg","helloooooooo");
//        return("hello");
        request.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(request,response);
    }

    //SpringMVC不使用视图解析器时，默认调用原生ServletAPI进行转发与重定向
    //return ("forward:/xxx/xxx/")转发
    //return ("redirect:/XXX/XXX/")重定向
    @RequestMapping("/m1/t2")
    public String test1(Model model){
        model.addAttribute("msg","helllllll");
        return("redirect:/WEB-INF/jsp/hello.jsp");
    }

    //SpringMVC使用视图解析器时
    //return ("xxx")转发
    //return ("redirect:/XXX.jsp")重定向
    @RequestMapping("/m1/t3")
    public String test2(Model model){
        model.addAttribute("msg","xxxxxxxx");
//        return("redirect:/index.jsp");
        return("redirect:/m1/t4");
    }

    @RequestMapping("/m1/t4")
    public String test3(Model model){
        model.addAttribute("msg","重定向页面");
        return("hello");
    }

}
