package com.mindaces.mindaces.service;


import org.springframework.stereotype.Service;

@Service
public class ErrorService
{
    public String getErrorMsg(String msg)
    {
        if(msg.equals("galleryMiss"))
        {
            return "존재하지 않는 갤러리입니다";
        }
        return msg;
    }

}
