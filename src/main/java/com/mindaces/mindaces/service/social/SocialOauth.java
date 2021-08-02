package com.mindaces.mindaces.service.social;

import com.mindaces.mindaces.helper.constants.SocialLoginType;
import org.springframework.security.core.userdetails.UserDetails;

public interface SocialOauth
{
    //각 Social Login 페이지로 redirect 할 URL 리턴
    String getOauthRedirectURL();


    UserDetails requestAccessToken(String code);

    default SocialLoginType type() {
        if (this instanceof GoogleOauth) {
            return SocialLoginType.GOOGLE;
        } else if (this instanceof NaverOauth) {
            return SocialLoginType.NAVER;
        } else if (this instanceof KakaoOauth) {
            return SocialLoginType.KAKAO;
        } else {
            return null;
        }
    }

}
