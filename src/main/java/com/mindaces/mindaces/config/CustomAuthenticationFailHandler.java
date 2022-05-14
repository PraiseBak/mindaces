package com.mindaces.mindaces.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class CustomAuthenticationFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException exception)
            throws IOException, ServletException, AuthenticationException,UsernameNotFoundException,InternalAuthenticationServiceException
    {
        String error = "";

        //res.sendRedirect("/user/login?error="+arg2.getMessage());
        req.removeAttribute("password");

        if(exception instanceof InternalAuthenticationServiceException)
        {
            error = "1";
//            error = "인증이 완료되지 않은 계정입니다 이메일을 확인해주세요";
        }
        else if(exception instanceof BadCredentialsException) {
            error = "2";
            //            error = "아이디나 비밀번호가 잘못되었습니다";
        }else
        {
            error = "3";
//            error = "로그인 할 수 없습니다 관리자에게 문의해주세요";
        }

        res.sendRedirect("/user/login?error=" + error);
    }

}

