package com.cp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("t1")
    public String test(){
        System.out.println("执行了test请求");
        return "hhh";
    }
}
