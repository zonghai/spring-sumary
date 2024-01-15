package com.spring.rabbitmq.health;

/**
 * @auth 十三先生
 * @date 2024/1/15
 * @desc
 */

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnEnabledHealthIndicator("app")
public class AppHealthIndicatorAutoConfiguration  {
    @Bean
    @ConditionalOnMissingBean(name = "appHealthIndicator")
    public HealthIndicator redisHealthIndicator() {
        return new AppHealthIndicator();
    }
}
