package com.netzero.version.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("/redis/get")
    public String getCacheData(){
        String cacheData = redisTemplate.opsForValue().get("API_URL");

        if (cacheData == null){
            cacheData = "https://script.google.com/macros/s/AKfycby1h6jkUFLSyCyvKH2GWcffr7DnN-IpqgghAnqMfBF5eMCNpp6a-oHi6wxMRa5EyEjY/exec";
            redisTemplate.opsForValue().set("API_URL", cacheData);
        }

        return cacheData;
    }
}
