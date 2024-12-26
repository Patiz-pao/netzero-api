package com.netzero.version.demo.domain.Enums.interfaceClass;

public interface RiceActivityType {
    String getName();
    int getDuration();
    void setDuration(int duration);
    double getElectricityRequired(double areaInRai);
    String getDescription();

    void resetDuration();
}
