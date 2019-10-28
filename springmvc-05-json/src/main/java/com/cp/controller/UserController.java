package com.cp.controller;

import com.cp.pojo.User;
import com.cp.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
//@Controller
public class UserController {
//    @RequestMapping(value = "/j1", produces = "application/json;charset=utf-8")//解决乱码
    @RequestMapping("/j1")//在配置文件中的annotation-driven配置解决乱码
//    @ResponseBody //加了这个注解或者使用RestController就不会走视图解析器，会直接返回一个字符串
    public String json1() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        User user = new User("陈鹏",3,"男");

        String string = mapper.writeValueAsString(user);
        return(string);
    }
    @RequestMapping("/j2")
    public String json2() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        List<User> userList = new ArrayList<User>();
        User user1 = new User("陈鹏2",3,"男");
        User user2 = new User("陈鹏3",3,"男");
        User user3 = new User("陈鹏4",3,"男");
        User user4 = new User("陈鹏5",3,"男");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        String string = mapper.writeValueAsString(userList);
        return(string);
    }

    @RequestMapping("/j3")
    public String json3() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(sdf);

//        String string = mapper.writeValueAsString(sdf.format(date));
        String string = mapper.writeValueAsString(date);
        return(string);
    }

    @RequestMapping("/j4")
    public String json4()  {

        User cp = new User("cpasp",1,"nan");
        Date date = new Date();
        return JsonUtil.getJson(date);

    }



}
