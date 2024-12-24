package com.netzero.version.demo.domain.Enums;

import com.netzero.version.demo.domain.Enums.interfaceClass.RiceActivityType;
import lombok.Getter;

@Getter
public enum Rice_RD61_ActivityType implements RiceActivityType {

    //TODO: 24/12/67 ในส่วนงานของวันนี้ทำ Activity ของ RD61 แล้วยิงเข้า handleRiceCalculation

    TEST("ทดสอบ rd-61","ทดสอบ", 0, 7),;

    private final String name;
    private final String description;
    private final double baseElectricityRequired;
    private final int duration;

    Rice_RD61_ActivityType(String name, String description, double baseElectricityRequired, int duration) {
        this.name = name;
        this.description = description;
        this.baseElectricityRequired = baseElectricityRequired;
        this.duration = duration;
    }

    public double getElectricityRequired(double areaInRai) {
        return this.baseElectricityRequired * areaInRai;
    }

}
