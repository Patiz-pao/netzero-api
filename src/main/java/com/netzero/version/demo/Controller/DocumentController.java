package com.netzero.version.demo.Controller;

import com.netzero.version.demo.Services.DocumentServices;
import com.netzero.version.demo.Util.GenericResponse;
import com.netzero.version.demo.domain.DataRes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class DocumentController {

    @Autowired
    private final DocumentServices documentServices;

    @GetMapping("/getAllData")
    public List<DataRes.FullResponse> getAllData(){
        List<DataRes.FullResponse> responses = documentServices.getAllData();
        log.info("success");
        return responses;
    }

}

