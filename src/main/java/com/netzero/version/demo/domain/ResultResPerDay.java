package com.netzero.version.demo.domain;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor // Constructor แบบไม่มีพารามิเตอร์
@AllArgsConstructor
@Builder
public class ResultResPerDay {
    private String area;
    private Double param1;
    private Double param2;
    private Double requiredElectricity;
    private Double totalElectricityProduced;
    private Double param3;
    private Double param4;
    private Double areaSize;
    private Double param5;
    private Double param6;
    private Double param7;
    private Double param8;

    // ฟิลด์ใหม่สำหรับ breakdown รายเดือน
    private List<Map<String, Object>> monthlyDetail;
}
