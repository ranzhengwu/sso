package com.rzw.web1.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("拦截"+httpServletRequest.getRequestURL().toString());
        String token = (String) httpServletRequest.getParameter("token");
        boolean auth = false;
        if (StringUtils.isEmpty(token)){
            Cookie[] cookies = httpServletRequest.getCookies();
           if(cookies!=null){
               for(Cookie cookie : cookies){
                   System.out.println(cookie.getName()+"<->"+cookie.getValue());
                   if ("token".equals(cookie.getName())){
                       auth = check(cookie.getValue());
                       System.out.println("1校验token:"+auth);
                   }
               }
           }
        }else{
            auth = check(token);
            System.out.println("2校验token:"+auth);
        }
        System.out.println("auth:"+auth);
        if(auth){
            System.out.println("通过校验");
            return true;
        }else{
            Cookie[] cookies = httpServletRequest.getCookies();
            if(cookies!=null){
                for(Cookie cookie : cookies){
                    System.out.println(cookie.getName()+"<+>"+cookie.getValue());
                    if ("token".equals(cookie.getName())){
                        cookie.setMaxAge(0);
                        cookie.setPath("/");
                        httpServletResponse.addCookie(cookie);
                    }
                }
            }
            httpServletResponse.sendRedirect("http://localhost:8080/loginPage?"+"url="+httpServletRequest.getRequestURL().toString());
            return false;
        }
    }
    
    private boolean check(String token){
        Boolean result = new RestTemplate().getForObject("http://localhost:8080/check?token="+token, boolean.class);
        return result;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
