package com.rzw.web2.controller;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web2")
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello web2";
    }
}
