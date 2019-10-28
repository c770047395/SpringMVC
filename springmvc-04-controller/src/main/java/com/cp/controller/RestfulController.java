package com.cp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RestfulController {

    //Restful：简洁、高效、安全
    //原来的： http://localhost:8080/add?a=1&b=3
    //Restful： http://localhost:8080/add/a/b

    @RequestMapping("/add")
    public String test(int a,int b, Model model){
        int res = a+b;
        model.addAttribute("msg","结果为"+res);
        return "hello";
    }

    //@RequestMapping(value="/add/{a}/{b}",method = RequestMethod.GET)
    @GetMapping("/add/{a}/{b}")
    public String test1(@PathVariable int a, @PathVariable int b, Model model){
        int res = a+b;
        model.addAttribute("msg","结果为"+res);
        return "hello";
    }
}
