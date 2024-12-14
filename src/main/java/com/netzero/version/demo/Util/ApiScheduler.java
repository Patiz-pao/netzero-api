package com.netzero.version.demo.Util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ApiScheduler {

    private final RestTemplate restTemplate;

    public ApiScheduler() {
        this.restTemplate = new RestTemplate();
    }

    @Scheduled(fixedRate = 45000)
    public void callPingApi() {
        String url = "https://netzero-api-ncrt.onrender.com/ping";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            log.info("Ping API called successfully: {}", response.getBody());
        } catch (Exception e) {
            log.error("Error calling Ping API: {}", e.getMessage());
        }
    }

    @Scheduled(fixedRate = 45000)
    public void callPingBlogApi() {
        String url = "https://blog-strapi-w691.onrender.com/admin/auth/login";

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            log.info("Blog Ping API called successfully");
        } catch (Exception e) {
            log.error("Error calling Blog API: {}", e.getMessage());
        }
    }
}
