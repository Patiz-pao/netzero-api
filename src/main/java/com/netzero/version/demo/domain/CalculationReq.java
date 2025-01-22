package com.netzero.version.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CalculationReq {
    private String province;
    private String amphoe;
    private String tumbol;
    private String area;
    private String crop_type;
    private Integer solarCell;
    private LocalDate month_start;

    public CalculationReq() {}
}
