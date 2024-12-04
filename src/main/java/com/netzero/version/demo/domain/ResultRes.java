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
    private double excessElectricity;     // จำนวนไฟฟ้าที่ผลิตมาเกิน
    private double areaUsed;              // พื้นที่ที่ใช้ในการติดตั้ง
    private double areaRemaining;         // พื้นที่ที่เหลือ
    private double GHG;                   // ก๊าซเรือนกระจก
    private double Sum_GHG;               // ก๊าซเรือนกระจกคงเหลือ
    private double requiredTreeCount;     // ต้นไม้ที่ต้องปลูก
    private double sum;                   // ผลรวมของก๊าซเรือนกระจกคงเหลือ(หักลบจากก๊าซธรรมชาติแล้ว)
}
