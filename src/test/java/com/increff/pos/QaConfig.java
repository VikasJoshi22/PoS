package com.increff.pos;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@EnableScheduling
@ComponentScan("com.increff.pos")
@PropertySources({
        @PropertySource(value = "classpath:./com/increff/pos/resources/test.properties", ignoreResourceNotFound = true)
})
public class QaConfig {

}
