package com.netzero.version.demo.domain.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CalculationDebugReq {
    private String province;
    private String tumbol;
    private String area;
    private String type;
    private String treeType;
    private double electric;
    private double solarEnergyIntensity;
    private Integer solarCell;
    private String day;
}
