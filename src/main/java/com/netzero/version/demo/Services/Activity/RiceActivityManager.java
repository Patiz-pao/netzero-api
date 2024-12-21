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
            List<Map<String, Object>> monthlyDetail,
            double areaInRai
    ) {
        List<ActivityRes> activities = new ArrayList<>();
        LocalDate currentDate = startDate;
        double currentElectricity = 0;
        int currentPanels = initialPanels;

        for (RiceActivityType activityType : RiceActivityType.values()) {

            double requiredElectricity = activityType.getElectricityRequired(areaInRai);
            // ปรับจำนวนแผงถ้าจำเป็น
            if (requiredElectricity > 0 && currentElectricity < requiredElectricity) {
                currentPanels = adjustPanels(
                        currentElectricity,
                        requiredElectricity,
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
                    monthlyDetail,areaInRai
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
            List<Map<String, Object>> monthlyDetail,
            double areaInRai
    ) {

        double solarEnergyMonth = getSolarEnergyForMonth(monthlyDetail, currentDate);
        double dailyEnergyForActivity = solarCalculator.calculateDailyEnergy(solarEnergyMonth, panels);

        double electricity = initialElectricity;

        // คำนวณไฟฟ้าที่ผลิตได้ในช่วงเวลาของกิจกรรม
        for (int i = 0; i < activityType.getDuration(); i++) {
            electricity += dailyEnergy * panels;
        }

        double requiredElectricity = activityType.getElectricityRequired(areaInRai);

        return new ActivityRes(
                activityType.getName(),
                currentDate,
                currentDate.plusDays(activityType.getDuration() - 1),
                electricity,
                requiredElectricity,
                electricity - requiredElectricity,
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
