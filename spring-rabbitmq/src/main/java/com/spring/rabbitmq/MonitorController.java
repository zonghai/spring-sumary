package com.spring.rabbitmq;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auth 十三先生
 * @date 2023/11/12
 * @desc
 */
@RefreshScope
@RestController
public class MonitorController {

    @GetMapping("monitor")
    public String monitor() {
        return "I'm alive.";
    }

}
