package com.netzero.version.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ResultRes {
    private String area;
    private double solarEnergyIntensity;        // ความเข้มพลังงานแสงอาทิตย์
    private int numberOfPanels;                 // จำนวนแผงโซล่าเซลล์ที่ต้องการ
    private double requiredElectricity;         // จำนวนไฟฟ้าที่ต้องการ
    private double usableElectricity;           // พลังงานที่ผลิตได้(นับเฉพาะวันที่มีการใช้ไฟฟ้า)
    private double surplusElectricityUsable;    // จำนวนไฟฟ้าที่ผลิตมาเกิน (นับเฉพาะวันที่มีการใช้ไฟฟ้า)
    private double producedElectricity;         // จำนวนไฟฟ้าที่ผลิตได้ทั้งหมด (นับทุกวัน)
    private double surplusElectricity;          // จำนวนไฟฟ้าที่ผลิตมาเกิน (ของแบบนับทุกวัน)
    private double areaUsed;                    // พื้นที่ที่ใช้ในการติดตั้ง
    private double areaRemaining;               // พื้นที่ที่เหลือ
    private int resultRice;
    private List<Map<String, Object>> monthlyDetail;
    private List<ActivityRes> activities;
}
