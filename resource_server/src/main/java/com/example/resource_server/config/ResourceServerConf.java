package com.example.resource_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

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

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // TODO 验证这里设置的资源服务id是否需要和授权服务器配置的资源服务id对应上
        resources.resourceId("test") // 代表这个资源服务的id，是可选的
                .tokenServices(tokenServices()) // 配置如何验证令牌
                .stateless(true); // 无状态模式，不启用session
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /***
     * 配置token远程验证策略
     */
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices services = new RemoteTokenServices();
        services.setCheckTokenEndpointUrl("http://localhost:9001/oauth/check_token");
        services.setClientId("c1");
        services.setClientSecret("secret");
        return services;
    }
}
