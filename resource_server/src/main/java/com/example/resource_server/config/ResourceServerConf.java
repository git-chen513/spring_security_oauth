package com.example.resource_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 资源服务器配置类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/18 17:11
 */
@Configuration
@EnableResourceServer // 开启资源服务器，启用该注解会自动增加OAuth2AuthenticationProcessingFilter过滤器
public class ResourceServerConf extends ResourceServerConfigurerAdapter {

    @Value("${login.mock.enable}")
    private boolean mockLogin;
    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
    private String introspectionUri;
    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
    private String clientSecret;

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("resource-server") // 代表这个资源服务器的id，是可选的，不配置会有默认值，建议配置上
                .tokenServices(tokenServices()) // 配置如何验证令牌
                .tokenStore(tokenStore) // jwt验证令牌
                .stateless(true); // 无状态模式，不启用session
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests(auth -> {
                    if (mockLogin) {
                        auth.anyRequest().permitAll();
                    } else {
                        auth.anyRequest().authenticated();
                    }
                })
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /***
     * 配置token远程验证策略
     *
     * TODO：使用远程验证token，那么token的解析由授权服务器完成，资源服务器将不再自行解析token
     * TODO：后续应该修改为远程验证token只返回true or false，token的解析由资源服务器完成
     *
     */
    @Bean
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices services = new RemoteTokenServices();
        //CustomRemoteTokenServices services = new CustomRemoteTokenServices();
        services.setCheckTokenEndpointUrl(introspectionUri);
        services.setClientId(clientId);
        services.setClientSecret(clientSecret);

        DefaultUserAuthenticationConverter defaultUserAuthenticationConverter = new DefaultUserAuthenticationConverter();
        //defaultUserAuthenticationConverter.setUserDetailsService(userDetailsServiceImpl);

        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(defaultUserAuthenticationConverter);
        services.setAccessTokenConverter(defaultAccessTokenConverter);

        return services;
    }
}
