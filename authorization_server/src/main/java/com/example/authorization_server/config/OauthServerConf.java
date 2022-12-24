package com.example.authorization_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;


/**
 * 授权服务器配置类
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/18 00:16
 */

@Configuration
@EnableAuthorizationServer // 开启授权服务器
public class OauthServerConf extends AuthorizationServerConfigurerAdapter {

    /**
     * 认证管理器，调用providerManager实现认证，默认providerManager为用户名密码认证
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 用于查找用户详情，认证管理器使用
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /***
     * 配置令牌端点的安全约束
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //.tokenKeyAccess("isAuthenticated()") // 用于资源服务器远程获取JWT公钥的端点：/oauth/token_key
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()") // 配置验证令牌端点：/oauth/check_token允许访问
                .allowFormAuthenticationForClients().passwordEncoder(NoOpPasswordEncoder.getInstance()); // 允许以表单认证的方式申请令牌，而不仅仅是 Basic Auth 方式
    }

    /***
     * 配置客户端详情服务：ClientDetailsService
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() // 内存方式
                .withClient("c1") // client_id
                // TODO 加密后的secret有问题
                // .secret(passwordEncoder.encode("secret")) // client_secret
                .secret("secret")
                // 代表客户端可以访问的资源服务器列表，当使用该客户端申请到的令牌访问资源服务器时
                // 资源服务器会进行校验，如果该客户端可以访问的资源服务器列表不包含当前资源服务器，则拒绝访问
                // 该配置是可选的，不进行配置时，默认该客户端可以访问所有资源服务器
                .resourceIds("resource-server")
                .authorizedGrantTypes("authorization_code", "password",
                        "client_credentials", "implicit", "refresh_token") // 该客户端允许的授权类型
                .scopes("all") // 允许的授权范围
                .autoApprove(true) // 是否自动授权
                .redirectUris("http://www.baidu.com"); // 回调地址
    }

    /***
     * 配置令牌的访问端点和令牌服务
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .userDetailsService(userDetailsService);
    }
}
