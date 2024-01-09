package com.spring.flowcontrol.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import jakarta.annotation.Resource;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 单机熔断处理机制
 *
 * @auth 十三先生
 * @date 2024/1/9
 * @desc
 */
@Service
public class SingleResourceService {

    @Resource
    private RedissonClient redissonClient;
    /**
     * 硬编码方式进行熔断
     *
     * @return
     */
    public String hardCodeTest() {

        RRateLimiter limiter = redissonClient.getRateLimiter("xx");
        try(Entry entry = SphU.entry("HelloWorld")){
            boolean acquire = limiter.tryAcquire();
        }catch (Throwable t){
            if (!BlockException.isBlockException(t)) {
                Tracer.trace(t);
            }
            return "error";
        }

        return "normal";
    }
    private static void initDegradeRule() {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule("HelloWorld");
        rule.setGrade(CircuitBreakerStrategy.ERROR_COUNT.getType())
                .setCount(3)
                .setMinRequestAmount(3)
                .setStatIntervalMs(60000) // 30s 统计时长
                .setTimeWindow(30); //熔断时长，单位为 s
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
    }
}
