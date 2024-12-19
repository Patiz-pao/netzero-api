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
<<<<<<< HEAD
    private String month_start;
    private String month_end;

    //Month จะเปลี่ยนเป็น LocalDatetime
=======
    private LocalDate month_start;
>>>>>>> 5b29345ffdc05b3f08d6f643deea30bc7e768c20
}
