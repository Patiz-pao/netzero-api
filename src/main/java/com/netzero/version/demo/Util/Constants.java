package com.netzero.version.demo.Util;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Constants {
    public static final int HOURS_OF_SUNLIGHT = 5; // ชั่วโมงแสงแดด
    public static final double SOLAR_W = 0.71; // ประสิทธิภาพการผลิตพลังงาน
    public static final double PANEL_EFFICIENCY = 0.2286; // ประสิทธิภาพแผง
    public static final double PANEL_AREA = 3; // ขนาดแผง (m^2)

    public static final double USE_TRACTOR = 31.5;
    public static final double USE_WATER_PUMP = 29.7;
    public static final double USE_DRONE = 7.2;

    public static final String RICE_RD47 = "rice-rd47";
    public static final String RICE_RD61 = "rice-rd61";
    public static final String RICE_RD57 = "rice-rd57";
    public static final String RICE_PATHUM_THANI_1 = "rice-pathum-thani-1";
    public static final String RICE_PHITSANULOK_2 = "rice-phitsanulok-2";

    public static final int RICE_RD47_DAY = 140;
    public static final int RICE_RD61_DAY = 134;
    public static final int RICE_RD57_DAY = 143;
    public static final int RICE_PATHUM_THANI_1_DAY = 141;
    public static final int RICE_PHITSANULOK_2_DAY = 157;

    public static final int RICE_RD47_RESULT = 793;
    public static final int RICE_RD61_RESULT = 1004;
    public static final int RICE_RD57_RESULT = 714;
    public static final int RICE_PATHUM_THANI_1_RESULT = 712;
    public static final int RICE_PHITSANULOK_2_RESULT = 807;

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

    public static final Map<String, Integer> DAYS_IN_MONTH = new HashMap<>() {{
        put("JAN", 31);
        put("FEB", 28);
        put("MAR", 31);
        put("APR", 30);
        put("MAY", 31);
        put("JUN", 30);
        put("JUL", 31);
        put("AUG", 31);
        put("SEP", 30);
        put("OCT", 31);
        put("NOV", 30);
        put("DEC", 31);
    }};
}
