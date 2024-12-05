package com.netzero.version.demo.Controller;

import com.netzero.version.demo.Services.DocumentServices;
import com.netzero.version.demo.Util.GenericResponse;
import com.netzero.version.demo.domain.CalculationDebugReq;
import com.netzero.version.demo.domain.CalculationReq;
import com.netzero.version.demo.domain.ResultRes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class DocumentController {

    private DocumentServices documentServices;

    @PostMapping("/calculate-rice")
    public GenericResponse<ResultRes> calculateRice(@RequestBody CalculationReq req){
        GenericResponse<ResultRes> response = documentServices.calculateRice(req);
        log.info("success");
        return response;
    }

    @PostMapping("/calculate-rice-debug")
    public GenericResponse<ResultRes> calculateRiceDebug(@RequestBody CalculationDebugReq req){
        GenericResponse<ResultRes> response = documentServices.calculateRiceDebug(req);
        log.info("success");
        return response;
    }
}

