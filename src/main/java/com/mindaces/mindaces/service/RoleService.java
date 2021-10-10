package com.mindaces.mindaces.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class RoleService
{
    public Boolean isUser(Authentication authentication)
    {
        if(authentication != null)
        {
            if(authentication.getPrincipal() instanceof User)
            {
                return true;
            }
        }
        return false;
    }

    public String getUsername(Authentication authentication)
    {
        if(authentication != null)
        {
            if(authentication.getPrincipal() instanceof User)
            {
                return authentication.getName();
            }
        }
        return "-";
    }


    public Boolean isSameUser(Authentication authentication, String objUsername)
    {
        String curUsername;
        curUsername = getUsername(authentication);
        if(objUsername.equals(curUsername))
        {
            return true;
        }
        return false;


    }
}
