package com.example.userservice.security;

import com.example.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { //권한관련
        http.csrf().disable();

        //http 요청이 users로 들어오는 모든것들은 허용한다.
        //http.authorizeHttpRequests().antMatchers("/uesrs/**").permitAll();

        //actuator 로 호출되는 모든 정보에 대해서는 인증이나 권한 없이 진행 가능
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();

        //지정된 IP만 요청 가능
        http.authorizeRequests().antMatchers("/**")
                .hasIpAddress("192.168.219.102")
                .and()
                .addFilter(getAuthenticationFilter());

        //h2-console 사용시 프레임뜨지 않는 오류 해결
        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception{
        AuthenticationFilter filter = new AuthenticationFilter(authenticationManager(), this.userService, this.env);
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
}
