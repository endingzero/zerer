package com.zjw.zerer.account.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @ClassName: OAuth2Config
 * @Description: OAuth2配置文件
 * @author: jiewei
 * @date: 2019/8/27
 */
@Configuration
public class OAuth2Config {

    private static final String ACCOUNT_RESOURCE_ID = "account";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(ACCOUNT_RESOURCE_ID).stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            //配置访问控制，必须认证过后才可以访问
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .and()
                    .requestMatchers().anyRequest()
                    .and()
                    .anonymous()
                    .and()
                    .authorizeRequests().antMatchers("/account/**").authenticated();
        }
    }


    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        RedisConnectionFactory redisConnectionFactory;

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
            oauthServer.allowFormAuthenticationForClients();
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("client_1")
                    .resourceIds(ACCOUNT_RESOURCE_ID)
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("select")
                    .authorities("client")
                    .secret("123456");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints.tokenStore(new RedisTokenStore(redisConnectionFactory))
                    .authenticationManager(authenticationManager);
        }
    }
}

