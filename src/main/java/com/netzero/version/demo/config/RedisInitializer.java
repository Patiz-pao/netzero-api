package com.netzero.version.demo.config;

import com.netzero.version.demo.Util.Constants;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisInitializer {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void initializeConstants(){
        Constants.setRedisTemplate(redisTemplate);
    }
}
