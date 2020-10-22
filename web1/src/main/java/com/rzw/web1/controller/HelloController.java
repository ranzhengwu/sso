package com.rzw.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/web1")
public class HelloController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello web1";
    }

    @RequestMapping("/index")
    @ResponseBody
    public String index(){
        return "index web1";
    }

    @RequestMapping(value = "/afterLoginToCheckToken")
    public String afterLoginToCheckToken(String token, String url
            , HttpServletResponse response, HttpServletRequest request) throws IOException {

        if(StringUtils.isEmpty(token)){
            return "authlogin";
        }else{
            boolean check = check(token==null?"999":token);
            System.out.println("web3校验token:"+check);
            if(check){
                if(StringUtils.isEmpty(url)){
                    return "/index";
                }else{
                    Cookie[] cookies = request.getCookies();
                    if(cookies!=null){
                        for(Cookie cookie : cookies){
                            System.out.println(cookie.getName()+"<->"+cookie.getValue());
                            if ("token".equals(cookie.getName())){
                                cookie.setMaxAge(0);
                                cookie.setPath("/");
                                response.addCookie(cookie);
                            }
                        }
                    }
                    Cookie cookie = new Cookie("token", token);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    return "redirect:"+url;
                }
            }else{
                response.sendRedirect("http://localhost:8080/loginPage?"+"url="+request.getRequestURL().toString());
                return null;
            }
        }
    }

    private boolean check(String token){
        Boolean result = new RestTemplate().getForObject("http://localhost:8080/check?token="+token, boolean.class);
        return result;
    }
}
