package com.cp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EncodingController {

    //过滤器解决乱码
    @RequestMapping("/e/t")
    public String test(@RequestParam("name") String name, Model model){
        System.out.println(name);
        model.addAttribute("msg",name);
        return("hello");
    }
}
