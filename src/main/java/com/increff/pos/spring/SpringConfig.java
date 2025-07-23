package com.increff.pos.spring;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;


@Configuration
@EnableScheduling
@ComponentScan("com.increff.pos")
@PropertySources({
        @PropertySource(value = "file:./pos.properties", ignoreResourceNotFound = true)
})
public class SpringConfig {

    @Bean(name = "restTemplate")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "bCryptPasswordEncoder")
    public BCryptPasswordEncoder getBCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
