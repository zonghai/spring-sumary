package com.spring.rabbitmq.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;


/**
 * @auth 十三先生
 * @date 2024/1/15
 * @desc
 */
@Component
public class AppHealthIndicator implements HealthIndicator {
    @Override
    public Health getHealth(boolean includeDetails) {
        return HealthIndicator.super.getHealth(includeDetails);
    }

    @Override
    public Health health() {
        Health.Builder up = Health.up().withDetail("url", "localhost:8080").withDetail("application","test");
        return up.build();
    }
}
