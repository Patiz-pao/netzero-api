package com.netzero.version.demo.Controller;

import com.netzero.version.demo.Services.CalculateServices;
import com.netzero.version.demo.Util.GenericResponse;
import com.netzero.version.demo.Util.ResponseHelper;
import com.netzero.version.demo.constants.ErrorMessage;
import com.netzero.version.demo.domain.Request.CalculationDebugReq;
import com.netzero.version.demo.domain.Request.CalculationReq;
import com.netzero.version.demo.domain.Response.ResultRes;
import io.swagger.models.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class CalculateController {

    private CalculateServices calculateServices;

    @PostMapping("/calculate-rice")
    public ResponseEntity<GenericResponse<ResultRes>> calculateRice(@RequestBody CalculationReq req){
        try{
            ResultRes result = calculateServices.calculateRice(req).getData();
            return ResponseHelper.responseWithData(HttpStatus.OK, "Calculation success", result);
        }
        catch (Exception e){
            return ResponseHelper.responseWithMessage(HttpStatus.INTERNAL_SERVER_ERROR,"Failed" + e.getMessage());
        }
    }

    @PostMapping("/calculate-rice-debug")
    public GenericResponse<ResultRes> calculateRiceDebug(@RequestBody CalculationDebugReq req){
        GenericResponse<ResultRes> response = calculateServices.calculateRiceDebug(req);
        log.info("success");
        return response;
    }
}
