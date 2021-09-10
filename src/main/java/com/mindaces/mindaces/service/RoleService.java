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

    public String getUserName(Authentication authentication)
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

}
