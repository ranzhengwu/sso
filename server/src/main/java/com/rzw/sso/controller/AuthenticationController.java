package com.rzw.sso.controller;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class AuthenticationController {
    public static Map<String,String> tokenMap = new HashMap();

    /**
     * 跳转到首页
     * @return
     */
    @RequestMapping(value = {"/index","/",""})
    public ModelAndView index(){
        return new ModelAndView("authlogin");
    }

    @RequestMapping(value = {"/loginPage"})
    public String loginPage(String url, Model model,HttpServletRequest request){
        model.addAttribute("url",url);
        return "authlogin";
    }

    /**
     * 登录
     * @param request
     * @param response
     * @param name
     * @param password
     * @param url
     * @param model
     * @return
     */
    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response
        , String name, String password, String url, Model model){
        if(name.equals(password)){
            String token = UUID.randomUUID().toString();
            request.getSession().setAttribute("token",token);
            tokenMap.put(token,name);
            System.out.println(tokenMap);
            System.out.println("登录成功生成token:"+token);
            return "redirect:http://localhost:8081/web1/afterLoginToCheckToken?token="+token+"&url="+url;
        }else{
            model.addAttribute("url",url);
            return "authlogin";
        }
    }

    /**
     * 检验token
     * @param token
     * @return
     */
    @RequestMapping("/check")
    @ResponseBody
    public boolean check(String token){
        boolean b = tokenMap.containsKey(token);
        System.out.println("校验token是否在map中存在:"+token+":"+b+tokenMap);
        return b;
    }
}
