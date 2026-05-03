package com.rabbiter.bms.interceptor;

import com.rabbiter.bms.model.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Reader interceptor: Intercepts requests that readers are not allowed to access
public class ReaderInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("userObj");

        // According to the chain of interception relationships, it can be concluded that the user must have logged in to reach this interceptor.
        if(user.getIsadmin() == 0) {    //If it is a user, then block.
            System.out.println("Readers are not allowed to access the administrator interface!");
            // Redirect to the login interface
            response.sendRedirect(request.getContextPath() + "/index.html");
            return false;
        }
        return true;    //Release
    }
}
