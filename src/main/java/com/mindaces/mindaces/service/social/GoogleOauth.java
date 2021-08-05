package com.mindaces.mindaces.service.social;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GoogleOauth implements SocialOauth
{
    @Value("${sns.google.url}")
    private String GOOGLE_SNS_BASE_URL;
    @Value("${sns.google.client.id}")
    private String GOOGLE_SNS_CLIENT_ID;
    @Value("${sns.google.callback.url}")
    private String GOOGLE_SNS_CALLBACK_URL;
    @Value("${sns.google.client.secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;
    private final String GOOGLE_SNS_TOKEN_BASE_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_SNS_REVOKE_TOKEN_URL = "https://oauth2.googleapis.com/revoke?token=";

    @Override
    public String getOauthRedirectURL()
    {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", "profile");
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return GOOGLE_SNS_BASE_URL + "?" + parameterString;
    }

    @Override
    public String requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, params, String.class);
        String accessCode = "false";
        if (responseEntity.getStatusCode() == HttpStatus.OK)
        {
            String[] bodyArray = responseEntity
                    .getBody()
                    .split("\n");

            for(int i=0;i<bodyArray.length;i++)
            {
                if(bodyArray[i].contains("\"access_token\""))
                {
                    accessCode = bodyArray[i].split("\"access_token\":")[1];
                    accessCode = accessCode.split("\"")[1];
                    accessCode = accessCode.split("\"")[0];
                    break;
                }
            }
        }
        Map<String, Object> revokeParams = new HashMap<>();
        revokeParams.put("header","Content-type:application/x-www-form-urlencoded");
        String revokeUrl = "https://oauth2.googleapis.com/revoke?token=" + accessCode;
        responseEntity =
                restTemplate.postForEntity(revokeUrl,revokeParams,String.class);
        System.out.println("result::" + responseEntity.getStatusCode());

        return accessCode;
    }

}