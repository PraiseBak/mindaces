package com.mindaces.mindaces.service;

import com.mindaces.mindaces.helper.constants.SocialLoginType;
import com.mindaces.mindaces.service.social.GoogleOauth;
import com.mindaces.mindaces.service.social.SocialOauth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OauthService extends DefaultOAuth2UserService
{
    private final List<SocialOauth> socialOauthList;
    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;

    public void request(SocialLoginType socialLoginType) {
        String redirectURL;
        if (socialLoginType == SocialLoginType.GOOGLE)
        {
            redirectURL = googleOauth.getOauthRedirectURL();
        }
        else
        {
            throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
        }

        System.out.println("REDIRECT 요청 -> " + redirectURL);
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String requestAccessToken(SocialLoginType socialLoginType, String code) {
        if (socialLoginType == SocialLoginType.GOOGLE)
        {
            return googleOauth.requestAccessToken(code);
        }
        throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
    }

    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }





}