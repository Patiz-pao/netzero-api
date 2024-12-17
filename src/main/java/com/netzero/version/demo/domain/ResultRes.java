package com.netzero.version.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResultRes {
    private String area;
    private double solarEnergyIntensity;  // ความเข้มพลังงานแสงอาทิตย์
    private int numberOfPanels;           // จำนวนแผงโซล่าเซลล์ที่ต้องการ
    private double requiredElectricity;   // จำนวนไฟฟ้าที่ต้องการ
    private double producedElectricity;   // จำนวนไฟฟ้าที่ผลิตได้
    private double surplusElectricity;    // จำนวนไฟฟ้าที่ผลิตมาเกิน
    private double areaUsed;              // พื้นที่ที่ใช้ในการติดตั้ง
    private double areaRemaining;         // พื้นที่ที่เหลือ
    private double month_1;
    private double month_2;
    private double month_3;
    private double month_4;
}
