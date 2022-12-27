package com.example.authorization_server.config;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Map;

/**
 * 自定义JWT令牌增强器
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/23 17:33
 */
public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map map = super.convertAccessToken(token, authentication);
        if (authentication.getPrincipal() != null) {
            map.put("email", "123456@qq.com");
            map.put("mobile", "13414497654");
        }
        return map;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//        OAuth2AccessToken token = super.enhance(accessToken, authentication);
//        if (token instanceof DefaultOAuth2AccessToken) {
//            DefaultOAuth2AccessToken defaultToken = (DefaultOAuth2AccessToken) token;
//            Map<String, ?> m = this.convertAccessToken(accessToken, authentication);
//            defaultToken.getAdditionalInformation().putAll(m);
//            // defaultToken.getAdditionalInformation().remove("scope");
//            return defaultToken;
//        }
//        return token;

        if (accessToken instanceof DefaultOAuth2AccessToken) {
            Map<String, ?> m = this.convertAccessToken(accessToken, authentication);
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation((Map<String, Object>)m);
            OAuth2AccessToken resultToken = super.enhance(accessToken, authentication);
            return resultToken;
        }
        return accessToken;
    }
}
