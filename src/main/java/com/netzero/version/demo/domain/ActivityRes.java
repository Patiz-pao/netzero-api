package com.netzero.version.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRes {
    private String activityName;
    private LocalDate startDate;
    private LocalDate endDate;
    private double electricityGenerated; // พลังงานที่ผลิตได้ kWh
    private double electricityUsed; // พลังงานที่ใช้ในในขั้นตอนนั้น kWh
    private double remainingElectricity; // พลังงานที่ผลิตได้ทั้งหมด
    private String description; //รายละเอียดแต่ละขั้นตอน
    private int panelsAdded; // จำนวนแผงที่เพิ่ม
}
