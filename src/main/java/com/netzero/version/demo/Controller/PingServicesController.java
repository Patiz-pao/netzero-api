package com.netzero.version.demo.Controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class PingServicesController {

    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        log.info("ping success");
        return ResponseEntity.ok(Collections.singletonMap("status", "ok"));
    }
}
