package com.cp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @RequestMapping("/hello")
    public Object hello(Model model){
        model.addAttribute("msg","welcome!");
        return("hello");
    }

    @RequestMapping("/hello1")
    public Object hello2(Model model){
        model.addAttribute("msg","welcome to my home!");
        return("hello");
    }

}
