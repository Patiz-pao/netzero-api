package com.netzero.version.demo.domain.Enums;

import static com.netzero.version.demo.Util.Constants.*;

public enum RiceActivityType {
    STORE_ELECTRICITY("กักเก็บไฟฟ้า", 0, 7),
    PLOWING_FIRST("ไถดะ (รอบที่ 1)", USE_TRACTOR, 1),
    REST_SOIL("พักดิน 5 วัน", 0, 5),
    PLOWING_SECOND("ไถแปร (รอบที่ 2)", USE_TRACTOR, 1),
    REST_SOIL_SECOND("พักดิน 1 วัน", 0, 1),
    HARROW_FIRST_AND_PUMPING_WATER_FIRST("คราด (ครั้งที่ 1), สูบน้ำ (ครั้งที่ 1)", USE_TRACTOR + USE_WATER_PUMP, 1),
    DRONE_FIRST("ใช้โดรนหว่านเมล็ดพันธุ์ (ครั้งที่1)", USE_DRONE, 1),
    STORE_ELECTRICITY_SECOND("กักเก็บไฟฟ้า", 0, 6),
    PUMPING_WATER_SECOND("สูบน้ำ (ครั้งที่ 2)", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_THREE("กักเก็บไฟฟ้า", 0, 7),
    DRONE_SECOND("ใช้โดรนพ่นปุ๋ย (ครั้งที่1) อายุข้าว 15 วัน", USE_DRONE, 1),
    STORE_ELECTRICITY_FOUR("กักเก็บไฟฟ้า", 0, 1),
    PUMPING_WATER_THREE("สูบน้ำ (ครั้งที่ 3)", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_FIVE("กักเก็บไฟฟ้า", 0, 9),
    PUMPING_WATER_FOUR("สูบน้ำ (ครั้งที่ 4)", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_SIX("กักเก็บไฟฟ้า", 0, 9),
    PUMPING_WATER_FIVE("สูบน้ำ (ครั้งที่ 5)", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_SEVEN("กักเก็บไฟฟ้า", 0, 8),
    DRONE_THREE("ใช้โดรนพ่นปุ๋ย (ครั้งที่ 2) อายุข้าว 45 วัน ( บำรุงต้นและใบ )", USE_DRONE, 1),
    PUMPING_WATER_SIX("สูบน้ำ (ครั้งที่ 6)", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_EIGHT("กักเก็บไฟฟ้า", 0, 9),
    PUMPING_WATER_SEVEN("สูบน้ำ (ครั้งที่ 7)", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_NINE("กักเก็บไฟฟ้า", 0, 9),
    PUMPING_WATER_EIGHT("สูบน้ำ (ครั้งที่ 8)", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_TEN("กักเก็บไฟฟ้า", 0, 2),
    DRONE_FOUR("ใช้โดรนพ่นปุ๋ย (ครั้งที่ 3) อายุข้าว 70 วัน ( ช่วยเร่งการสร้างเมล็ด )", USE_DRONE, 1),
    STORE_ELECTRICITY_ELEVEN("กักเก็บไฟฟ้า", 0, 6),
    PUMPING_WATER_NINE("สูบน้ำ (ครั้งที่ 9)", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_TWELVE("กักเก็บไฟฟ้า", 0, 9),
    PUMPING_WATER_TEN("สูบน้ำ (ครั้งที่ 10)", USE_WATER_PUMP, 1),
    STORE_ELECTRICITY_THIRTEEN("ช่วงรอระบายน้ำออกจากนาประมาณ 16 วัน", 0, 16),
    WAITING_TO_HARVEST("ระบายน้ำออกจากนาประมาณ 6 วัน", 0, 6),
    HARVEST("เก็บเกี่ยวข้าว", 0, 4);



    private final String name;
    private final double electricityRequired;
    private final int duration;

    RiceActivityType(String name, double electricityRequired, int duration) {
        this.name = name;
        this.electricityRequired = electricityRequired;
        this.duration = duration;
    }

    public String getName() { return name; }
    public double getElectricityRequired() { return electricityRequired; }
    public int getDuration() { return duration; }
}
