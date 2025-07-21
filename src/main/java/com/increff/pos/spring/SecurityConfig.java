package com.increff.pos.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http//
                // Match only these URLs
                .requestMatchers()//
                .antMatchers("/api/**")//
                .and().authorizeRequests()//
                .antMatchers("/api/**/supervisor/**").hasAuthority("supervisor")//
                .antMatchers("/api/**").hasAnyAuthority("supervisor", "operator")//
                // Ignore CSRF and CORS
                .and().csrf().disable().cors().disable();
    }
}