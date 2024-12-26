package com.netzero.version.demo.Services.Activity;

import com.netzero.version.demo.domain.ActivityRes;
import com.netzero.version.demo.domain.CalculationReq;
import com.netzero.version.demo.domain.Enums.*;
import com.netzero.version.demo.domain.Enums.interfaceClass.RiceActivityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class RiceActivityManager {

    private final SolarEnergyCalculator solarCalculator;

    public List<ActivityRes> calculateActivities(
            CalculationReq req,
            LocalDate startDate,
            double dailyEnergy,
            int numberOfPanels,
            List<Map<String, Object>> monthlyDetail,
            double areaInRai
    ) {
        List<ActivityRes> activities = new ArrayList<>();
        LocalDate currentDate = startDate;
        double currentElectricity = 0;
        int currentPanels = numberOfPanels;

        Enum<?>[] activityTypes = getActivityTypes(req);

        for (Enum<?> activityTypeEnum : activityTypes) {
            RiceActivityType activityType = (RiceActivityType) activityTypeEnum;

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
        double dailyEnergyForActivity = solarCalculator.calculateDailyEnergy(solarEnergyMonth, panels); // พลังงานที่ผลิตได้ต่อวัน เอาไว้ใช้ Debug ดูเฉยๆ ไม่ต้องลบออก

        double electricity = 0;
        double requiredElectricity;

        activityType.resetDuration();

        do {
            // คำนวณไฟฟ้าที่ผลิตได้ในช่วงเวลาของกิจกรรม
            for (int i = 0; i < activityType.getDuration(); i++) {
                electricity += dailyEnergy * panels;
            }

            requiredElectricity = activityType.getElectricityRequired(areaInRai);

            if (electricity < requiredElectricity) {
                electricity = 0;
                activityType.setDuration(activityType.getDuration() + 1);
            }

        }while (electricity < requiredElectricity);


        return new ActivityRes(
                activityType.getName(),
                currentDate,
                currentDate.plusDays(activityType.getDuration() - 1),
                electricity,
                requiredElectricity,
                electricity - requiredElectricity,
                activityType.getDescription(),
                panels
        );
    }

    private double getSolarEnergyForMonth(List<Map<String, Object>> monthlyDetail, LocalDate date) {
        return monthlyDetail.stream()
                .filter(month -> date.getMonth().toString().substring(0, 3)
                        .equals(((String) month.get("month")).substring(0, 3)))
                .mapToDouble(month -> Double.parseDouble((String) month.get("solarEnergy")))
                .sum();
    }

    private static Enum<?>[] getActivityTypes(CalculationReq req) {
        Enum<?>[] activityTypes;

        if ("rice-rd47".equals(req.getCrop_type())) {
            activityTypes = Rice_RD47_ActivityType.values();
        } else if ("rice-rd61".equals(req.getCrop_type())) {
            activityTypes = Rice_RD61_ActivityType.values();
        } else if ("rice-rd57".equals(req.getCrop_type())) {
            activityTypes = Rice_RD57_ActivityType.values();
        } else if ("rice-pathum-thani-1".equals(req.getCrop_type())) {
            activityTypes = Rice_Pathum_1_ActivityType.values();
        }else if ("rice-phitsanulok-2".equals(req.getCrop_type())) {
            activityTypes = Rice_Phitsanulok_2_ActivityType.values();
        }else {
            throw new IllegalArgumentException("Unsupported crop type: " + req.getCrop_type());
        }
        return activityTypes;
    }
}
