package com.rabbiter.bms.interceptor;

import com.rabbiter.bms.model.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// User interceptor, intercepting requests that cannot be accessed without login.
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("userObj");

        if(user == null) {  //Not logged in
            System.out.println("Not logged in! Cannot access!");
            // Redirect to the login interface
            response.sendRedirect(request.getContextPath() + "/index.html");
            return false;
        }

        return true;    //Release
    }

}
