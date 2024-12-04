package com.netzero.version.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CalculationReq {
    private String province;
    private String tumbol;
    private String area;
    private String type;
    private String treeType;
}
