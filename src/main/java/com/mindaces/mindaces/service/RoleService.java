package com.mindaces.mindaces.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class RoleService
{

    Boolean isUserRole(Authentication authentication)
    {
        if(authentication != null)
        {
            if(authentication instanceof User)
            {
                return true;
            }
        }
        return false;
    }




}
