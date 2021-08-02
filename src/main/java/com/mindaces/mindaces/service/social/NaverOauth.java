package com.mindaces.mindaces.service.social;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class NaverOauth implements SocialOauth {
    @Override
    public String getOauthRedirectURL()
    {
        return "";
    }

    @Override
    public UserDetails requestAccessToken(String code)
    {
        return null;
    }
}