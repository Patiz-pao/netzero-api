package com.netzero.version.demo.Util;

import com.netzero.version.demo.Entity.SolarIntEntity;
import com.netzero.version.demo.domain.CalculationReq;
import com.netzero.version.demo.domain.Enums.interfaceClass.RiceActivityType;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

import static com.netzero.version.demo.Services.ActivityService.RiceActivityManager.getActivityTypes;
import static com.netzero.version.demo.Util.Constants.*;

@Component
public class CalculationUtils {

    private Double parseEnergy(String energy) {
        try {
            return energy != null ? Double.parseDouble(energy) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private Double getMonthlyEnergy(SolarIntEntity entity, String month) {
        return switch (month) {
            case "jan" -> parseEnergy(entity.getJan());
            case "feb" -> parseEnergy(entity.getFeb());
            case "mar" -> parseEnergy(entity.getMar());
            case "apr" -> parseEnergy(entity.getApr());
            case "may" -> parseEnergy(entity.getMay());
            case "jun" -> parseEnergy(entity.getJun());
            case "jul" -> parseEnergy(entity.getJul());
            case "aug" -> parseEnergy(entity.getAug());
            case "sep" -> parseEnergy(entity.getSep());
            case "oct" -> parseEnergy(entity.getOct());
            case "nov" -> parseEnergy(entity.getNov());
            case "dec" -> parseEnergy(entity.getDec());
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
    }

    public double formatDouble(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }

    public List<String> generateMonthInRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        List<String> result = new ArrayList<>();
        LocalDate current = startDate;
        int monthCount = 0;

        while (!current.isAfter(endDate) && monthCount < 5) {
            result.add(current.getMonth().toString().substring(0, 3).toUpperCase());
            current = current.plusMonths(1);
            monthCount++;
        }

        return result;
    }

    public Map<String, Double> getSolarEnergyEachMonth(String regionId, List<SolarIntEntity> data, List<String> selectMonths) {
        Map<String, Double> energyPerMonth = new LinkedHashMap<>();

        SolarIntEntity targetEntity = data.stream()
                .filter(entity -> regionId.equals(entity.getRegionId()))
                .findFirst()
                .orElse(null);

        if (targetEntity != null) {
            for (String month : selectMonths) {
                if (!MONTH_INDEX.containsKey(month.toUpperCase())) {
                    throw new IllegalArgumentException("Invalid month: " + month);
                }

                String monthKey = month.toLowerCase();
                Double energy = getMonthlyEnergy(targetEntity, monthKey);
                energyPerMonth.put(month, energy);
            }
        }

        return energyPerMonth;
    }

    public String formatDoubleToString(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
    }

    public double getSolarEnergyForMonth(Map<String, Double> monthlyDetail, LocalDate date) {
        String monthStr = date.getMonth().toString().substring(0, 3).toUpperCase();
        return monthlyDetail.getOrDefault(monthStr, 0.0);
    }

    public Map<String, Object> calculateEnergyToolsUsage(CalculationReq req, double areaInRai) {

        Enum<?>[] activityTypes = getActivityTypes(req);

        double totalEnergyTractors = 0.0;
        double totalEnergyWaterPump = 0.0;
        double totalEnergyDrone = 0.0;

        for (Enum<?> activityEnum : activityTypes) {
            RiceActivityType activity = (RiceActivityType) activityEnum;

            if (activity.getBaseElectricityRequired() > 0) {
                double energyForThisActivity = activity.getElectricityRequired(areaInRai);

                if (activity.getBaseElectricityRequired() == USE_TRACTOR) {
                    totalEnergyTractors += energyForThisActivity;
                } else if (activity.getBaseElectricityRequired() == USE_WATER_PUMP) {
                    totalEnergyWaterPump += energyForThisActivity;
                } else if (activity.getBaseElectricityRequired() == USE_DRONE) {
                    totalEnergyDrone += energyForThisActivity;
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalEnergyTractors", totalEnergyTractors);
        result.put("totalEnergyWaterPump", totalEnergyWaterPump);
        result.put("totalEnergyDrone", totalEnergyDrone);

        return result;
    }
}
