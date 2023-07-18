package com.lucarlosmelo.catalog.components;

import com.lucarlosmelo.catalog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class JwtTokenEnhancer implements TokenEnhancer {

    @Autowired
    private UserService userService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        var user = userService.findByEmail(oAuth2Authentication.getName());

        var map = new HashMap<String, Object>();
        map.put("userFirstName ", user.getFirstName());
        map.put("userId", user.getId());

        var token = (DefaultOAuth2AccessToken) oAuth2AccessToken;
        token.setAdditionalInformation(map);

        return oAuth2AccessToken;
    }
}
