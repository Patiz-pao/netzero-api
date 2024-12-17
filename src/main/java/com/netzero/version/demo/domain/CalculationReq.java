package com.netzero.version.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CalculationReq {
    private String province;
    private String amphoe;
    private String tumbol;
    private String area;
    private String crop_type;
    private int solarCell;
}
