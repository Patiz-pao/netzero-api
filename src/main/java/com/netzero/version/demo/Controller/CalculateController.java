package com.netzero.version.demo.Controller;

import com.netzero.version.demo.Services.CalculateServices;
import com.netzero.version.demo.Util.GenericResponse;
import com.netzero.version.demo.domain.CalculationReq;
import com.netzero.version.demo.domain.ResultRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class CalculateController {

    private CalculateServices calculateServices;

    @PostMapping("/calculate")
    @Operation(
            summary = "Calculate Rice",
            description = "Calculate rice data based on user input",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Default Request",
                                    value = """
                                            {
                                              "province": "Pathum Thani",
                                              "amphoe": "Mueang Pathum Thani",
                                              "tumbol": "Bang Prao",
                                              "area": "1",
                                              "crop_type": "rice-rd47",
                                              "solarCell": null,
                                              "month_start": "2024-01-01"
                                            }"""
                            )
                    )
            )
    )
    public GenericResponse<ResultRes> calculateRice(@RequestBody CalculationReq req){
        GenericResponse<ResultRes> response = calculateServices.calculateRice(req);
        log.info("success");
        return response;
    }
}
