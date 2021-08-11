package com.mindaces.mindaces.service.social;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
public class GoogleOAuth2User implements OAuth2User {
    //private List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
    private OAuth2User oauth2User;
    /*
    private String id;
    @JsonProperty("google_account")
    private Map<String, Object> googleAccount;
    private Map<String, Object> properties;


     */
    @Override
    public Map<String, Object> getAttributes() {
/*
        Map<String, Object> attributes = new HashMap<>();
        attributes.putAll(googleAccount);
        attributes.putAll(properties);

 */

        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getEmail() {
        return oauth2User.<String>getAttribute("email");
    }


}