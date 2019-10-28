package com.cp.controller;

import com.cp.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    //localhost：8080/user/t1?name=xxxx
    @GetMapping("t1")
    public String test(String name, Model model){
        //1.接收前端参数
        System.out.println("接收到前端的参数为:"+name);
        //2.将返回的结果传递给前端
        model.addAttribute("msg",name);

        return "hello";
    }

    //前端参数与接收参数不同，使用@RequestParam("xx")注解  ps：最好前端接收的参数都使用
    //localhost：8080/user/t1?username=xxxx
    @GetMapping("t2")
    public String test1(@RequestParam("username") String name, Model model){
        //1.接收前端参数
        System.out.println("get param:"+name);
        //2.将返回的结果传递给前端
        model.addAttribute("msg",name);

        return "hello";
    }

    //前端接受的参数是一个对象
    @RequestMapping("/t3")
    public String test2(User user, Model model){
        //1.接收前端参数
        System.out.println(user);
        //2.将返回的结果传递给前端
        model.addAttribute("msg",user);
        return "hello";
    }


}
