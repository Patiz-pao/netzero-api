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

    @PostMapping("/calculation")
    public GenericResponse<ResultRes> calculationData(@RequestBody CalculationReq req){
        GenericResponse<ResultRes> response = documentServices.calculationData(req);
        log.info("success");
        return response;
    }

    @PostMapping("/calculation_debug")
    public GenericResponse<ResultRes> calculationData_debug(@RequestBody CalculationDebugReq req){
        GenericResponse<ResultRes> response = documentServices.calculationDataDebug(req);
        log.info("success");
        return response;
    }
}

