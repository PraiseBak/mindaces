package com.mindaces.mindaces.service.social;

import com.mindaces.mindaces.helper.constants.SocialLoginType;

public interface SocialOauth
{
    String getOauthRedirectURL();


    String requestAccessToken(String code);



    default SocialLoginType type() {

       if (this instanceof GoogleOauth) {
            return SocialLoginType.GOOGLE;
        } else {
            return null;
        }
    }

}
