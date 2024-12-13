package com.netzero.version.demo.Util;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Constants {
    public static final int HOURS_OF_SUNLIGHT = 5; // ชั่วโมงแสงแดด
    public static final double SOLAR_W = 0.45; // ประสิทธิภาพการผลิตพลังงาน
    public static final double PANEL_EFFICIENCY = 0.2; // ประสิทธิภาพแผง
    public static final double PANEL_AREA = 2; // ขนาดแผง (m^2)
    public static final double GHG_SOLAR = 0.42; // ก๊าซเรือนกระจกที่ช่วยลด/kWh (solar)

    public static final int RICE_KG = 678; // ผลผลิตที่ได้เฉลี่ย 1 ไร่/กิโลกรัม
    public static final double GHG_RICE = 1.5; // ก๊าซเรือนกระจกที่ปล่อยออกมา/kg (rice)
    public static final int DAYS_RICE = 120; // จำนวนวัน

    public static final int BANANA_KG = 2035; // ผลผลิตที่ได้เฉลี่ย 1 ไร่/กิโลกรัม
    public static final double GHG_BANANA = 0.6; // ก๊าซเรือนกระจกที่ปล่อยออกมา/kg (banana)
    public static final int DAYS_BANANA = 300; // จำนวนวัน

    public static final Map<String, Integer> MONTH_INDEX = new HashMap<>();
    static {
        MONTH_INDEX.put("JAN", 3);
        MONTH_INDEX.put("FEB", 4);
        MONTH_INDEX.put("MAR", 5);
        MONTH_INDEX.put("APR", 6);
        MONTH_INDEX.put("MAY", 7);
        MONTH_INDEX.put("JUN", 8);
        MONTH_INDEX.put("JUL", 9);
        MONTH_INDEX.put("AUG", 10);
        MONTH_INDEX.put("SEP", 11);
        MONTH_INDEX.put("OCT", 12);
        MONTH_INDEX.put("NOV", 13);
        MONTH_INDEX.put("DEC", 14);
    }
}
