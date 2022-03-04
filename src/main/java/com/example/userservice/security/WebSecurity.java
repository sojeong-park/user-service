package com.example.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception { //권한관련
        http.csrf().disable();

        //http 요청이 users로 들어오는 모든것들은 허용한다.
        http.authorizeHttpRequests().antMatchers("/uesrs/**").permitAll();

        //h2-console 사용시 프레임뜨지 않는 오류 해결
        http.headers().frameOptions().disable();
    }
}
