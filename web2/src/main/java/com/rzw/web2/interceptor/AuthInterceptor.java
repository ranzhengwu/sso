package com.rzw.web2.interceptor;

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
        String token = (String) httpServletRequest.getSession().getAttribute("token");
        boolean auth = false;
        if (StringUtils.isEmpty(token)){
            Cookie[] cookies = httpServletRequest.getCookies();
            for(Cookie cookie : cookies){
                if ("token".equals(cookie.getName())){
                    auth = check(token);
                }
            }
        }else{
            auth = check(token);
        }
        if(auth){
            return true;
        }else{
            httpServletResponse.sendRedirect("http://localhost:8080/loginPage?"+"url="+httpServletRequest.getRequestURL().toString());
            return false;
        }
    }
    
    private boolean check(String token){
        return new RestTemplate().getForObject("http://localhost:8080/check", boolean.class, token);
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
