package com.netzero.version.demo.Services.Activity;

import com.netzero.version.demo.domain.ActivityRes;
import com.netzero.version.demo.domain.Enums.RiceActivityType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class RiceActivityManager {

    private final SolarEnergyCalculator solarCalculator;

    public List<ActivityRes> calculateActivities(
            LocalDate startDate,
            double dailyEnergy,
            int initialPanels,
            List<Map<String, Object>> monthlyDetail
    ) {
        List<ActivityRes> activities = new ArrayList<>();
        LocalDate currentDate = startDate;
        double currentElectricity = 0;
        int currentPanels = initialPanels;

        for (RiceActivityType activityType : RiceActivityType.values()) {
            // ปรับจำนวนแผงถ้าจำเป็น
            if (activityType.getElectricityRequired() > 0 &&
                    currentElectricity < activityType.getElectricityRequired()) {
                currentPanels = adjustPanels(
                        currentElectricity,
                        activityType.getElectricityRequired(),
                        dailyEnergy
                );
            }
            ActivityRes activity = processActivity(
                    activityType,
                    currentDate,
                    currentElectricity,
                    dailyEnergy,
                    currentPanels,
                    currentPanels,
                    monthlyDetail
            );
            activities.add(activity);
            currentDate = activity.getEndDate().plusDays(1);
            currentElectricity = activity.getRemainingElectricity();
        }

        return activities;
    }

    private ActivityRes processActivity(
            RiceActivityType activityType,
            LocalDate currentDate,
            double initialElectricity,
            double dailyEnergy,
            int panels,
            int panelsAdded,
            List<Map<String, Object>> monthlyDetail
    ) {

        double solarEnergyMonth = getSolarEnergyForMonth(monthlyDetail, currentDate);
        double dailyEnergyForActivity = solarCalculator.calculateDailyEnergy(solarEnergyMonth, panels);

        double electricity = initialElectricity;

        // คำนวณไฟฟ้าที่ผลิตได้ในช่วงเวลาของกิจกรรม
        for (int i = 0; i < activityType.getDuration(); i++) {
            electricity += dailyEnergy * panels;
        }

        return new ActivityRes(
                activityType.getName(),
                currentDate,
                currentDate.plusDays(activityType.getDuration() - 1),
                electricity,
                activityType.getElectricityRequired(),
                electricity - activityType.getElectricityRequired(),
                activityType.getDescription(),
                panelsAdded
        );
    }

    private int adjustPanels(double currentElectricity, double required, double dailyEnergy) {
        int additionalPanels = (int) Math.ceil((required - currentElectricity) / dailyEnergy);
        return Math.max(1, additionalPanels);
    }

    private double getSolarEnergyForMonth(List<Map<String, Object>> monthlyDetail, LocalDate date) {
        return monthlyDetail.stream()
                .filter(month -> date.getMonth().toString().substring(0, 3)
                        .equals(((String) month.get("month")).substring(0, 3)))
                .mapToDouble(month -> Double.parseDouble((String) month.get("solarEnergy")))
                .sum();
    }
}
