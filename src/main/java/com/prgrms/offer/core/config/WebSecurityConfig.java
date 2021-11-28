package com.prgrms.offer.core.config;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.GET;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .cors()
            .and()
            // h2 console
            .headers()
            .frameOptions()
            .sameOrigin()
            .and()
            // session
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // url
            .authorizeRequests()
            .antMatchers(POST, "/api/v1/members").permitAll()
            .antMatchers(GET, "/api/v1/members/profiles/**").permitAll()
            .anyRequest().authenticated();
    }
}
