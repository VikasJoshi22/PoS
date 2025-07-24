package com.increff.pos.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .antMatchers(HttpMethod.GET, "/api/products/get-by**").hasAuthority("operator")
                .antMatchers( "/api/clients").hasAuthority("supervisor")
                .antMatchers("/api/products").hasAuthority("supervisor")
                .antMatchers("/api/orders/create").hasAuthority("supervisor")
                .antMatchers("/api/**").hasAnyAuthority("supervisor", "operator")//
                // Ignore CSRF and CORS
                .and().csrf().disable().cors().disable();
    }
}