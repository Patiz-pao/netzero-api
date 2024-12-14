package com.netzero.version.demo.domain.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class CalculationReq {
    private String province;
    private String tumbol;
    private String area;
    private String type;
    private String treeType;
    private int solarCell;
}
