package com.netzero.version.demo.Services.Activity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.netzero.version.demo.Util.Constants.*;

@RequiredArgsConstructor
@Service
public class SolarEnergyCalculator {
    private final double panelEfficiency;
    private final double hoursOfSunlight;
    private final double solarWattage;

    public SolarEnergyCalculator() {
        this.panelEfficiency = PANEL_EFFICIENCY;
        this.hoursOfSunlight = HOURS_OF_SUNLIGHT;
        this.solarWattage = SOLAR_W;
    }

    public double calculateDailyEnergy(double solarIntensity, int numberOfPanels) {
        return (solarIntensity / 3.6) * panelEfficiency * hoursOfSunlight * solarWattage * numberOfPanels;
    }
}
