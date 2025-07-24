package com.increff.pos;

import com.increff.pos.spring.SpringConfig;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableScheduling
@ComponentScan(
        basePackages = { "com.increff.pos" }, //
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })
)
@PropertySources({
        @PropertySource(value = "classpath:test.properties", ignoreResourceNotFound = true)
})
public class QaConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
