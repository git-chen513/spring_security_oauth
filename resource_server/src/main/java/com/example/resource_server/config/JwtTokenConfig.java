package com.example.resource_server.config;

import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * JWT令牌存储配置
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/22 19:16
 */
@Configuration
public class JwtTokenConfig {

    @Bean
    public TokenStore tokenStore() throws IOException {
        // 使用JWT令牌
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() throws IOException{
        // 使用公钥解密JWT令牌
        CustomJwtAccessTokenConverter jwtAccessTokenConverter = new CustomJwtAccessTokenConverter();
        ClassPathResource resource = new ClassPathResource("public.txt");
        String publicKey = IOUtils.toString((InputStream)resource.getInputStream(), (Charset) Charset.defaultCharset());
        jwtAccessTokenConverter.setVerifierKey(publicKey);
        return jwtAccessTokenConverter;
    }
}
