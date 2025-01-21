package com.netzero.version.demo.Services;

import com.netzero.version.demo.Entity.*;
import com.netzero.version.demo.Repository.*;
import com.netzero.version.demo.Services.Activity.RiceActivityManager;
import com.netzero.version.demo.Services.Activity.SolarEnergyCalculator;
import com.netzero.version.demo.Util.GenericResponse;
import com.netzero.version.demo.domain.ActivityRes;
import com.netzero.version.demo.domain.CalculationReq;
import com.netzero.version.demo.domain.ResultRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.netzero.version.demo.Util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalculateServices {
    private final SolarEnergyCalculator solarCalculator;
    private final RiceActivityManager activityManager;

    private final RiceTypeRepo riceTypeRepo;
    private final EnergyReqRepo energyReqRepo;
    private final CentralRepo centralRepo;
    private final SolarIntRepo solarIntRepo;

    private final AreaDataRepo areaDataRepo;
    private final DataRepo dataRepo;
    private final ElectricityDataRepo electricityDataRepo;
    private final LocationRepo locationRepo;

    public GenericResponse<ResultRes> checkValue(CalculationReq req) {
        if (req.getCrop_type().equals("rice-rd47") || req.getCrop_type().equals("rice-rd61") ||
                req.getCrop_type().equals("rice-rd57") || req.getCrop_type().equals("rice-pathum-thani-1") ||
                req.getCrop_type().equals("rice-phitsanulok-2")) {
            return calculateProcess(req);
        } else {
            return new GenericResponse<>(HttpStatus.BAD_REQUEST, "Invalid data");
        }
    }

    private double formatDouble(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }

    private Double parseEnergy(String energy) {
        try {
            return energy != null ? Double.parseDouble(energy) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private List<String> generateMonthInRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        List<String> result = new ArrayList<>();
        LocalDate current = startDate;
        int monthCount = 0;

        while (!current.isAfter(endDate) && monthCount < 4) {
            result.add(current.getMonth().toString().substring(0, 3).toUpperCase());
            current = current.plusMonths(1);
            monthCount++;
        }

        return result;
    }

    private Map<String, Double> getSolarEnergyEachMonth(String regionId, List<SolarIntEntity> data, List<String> selectMonths) {
        Map<String, Double> energyPerMonth = new LinkedHashMap<>();
        for (String month : selectMonths) {
            Integer index = MONTH_INDEX.get(month);
            if (index == null) {
                throw new IllegalArgumentException("Invalid month: " + month);
            }
            for (SolarIntEntity entity : data) {
                if (regionId.equals(entity.getRegionId())) {
                    switch (month.toLowerCase()) {
                        case "jan":
                            energyPerMonth.put(month, parseEnergy(entity.getJan()));
                            break;
                        case "feb":
                            energyPerMonth.put(month, parseEnergy(entity.getFeb()));
                            break;
                        case "mar":
                            energyPerMonth.put(month, parseEnergy(entity.getMar()));
                            break;
                        case "apr":
                            energyPerMonth.put(month, parseEnergy(entity.getApr()));
                            break;
                        case "may":
                            energyPerMonth.put(month, parseEnergy(entity.getMay()));
                            break;
                        case "jun":
                            energyPerMonth.put(month, parseEnergy(entity.getJun()));
                            break;
                        case "jul":
                            energyPerMonth.put(month, parseEnergy(entity.getJul()));
                            break;
                        case "aug":
                            energyPerMonth.put(month, parseEnergy(entity.getAug()));
                            break;
                        case "sep":
                            energyPerMonth.put(month, parseEnergy(entity.getSep()));
                            break;
                        case "oct":
                            energyPerMonth.put(month, parseEnergy(entity.getOct()));
                            break;
                        case "nov":
                            energyPerMonth.put(month, parseEnergy(entity.getNov()));
                            break;
                        case "dec":
                            energyPerMonth.put(month, parseEnergy(entity.getDec()));
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid month: " + month);
                    }
                    break;
                }
            }
        }
        return energyPerMonth;
    }

    private String formatDoubleToString(double value) {
        DecimalFormat df = new DecimalFormat("#.##"); // รูปแบบที่ต้องการ เช่น ทศนิยม 2 ตำแหน่ง
        return df.format(value);
    }

    private double getSolarEnergyForMonth(Map<String, Double> monthlyDetail, LocalDate date) {
        String monthStr = date.getMonth().toString().substring(0, 3).toUpperCase();
        return monthlyDetail.getOrDefault(monthStr, 0.0); // ถ้าไม่เจอค่าให้คืนค่า 0.0
    }

    private void saveData(String responseId, CalculationReq req, double area, double areaUsed, double areaRemaining, double requiredElectricity, double totalElectricity, double surplusElectricity) {
        // Save Location Data
        LocationEntity locationDetail = new LocationEntity();
        locationDetail.setResponseId(responseId);
        locationDetail.setProvince(req.getProvince());
        locationDetail.setAmphoe(req.getAmphoe());
        locationDetail.setTumbol(req.getTumbol());
        locationRepo.save(locationDetail);

        // Save Area Data
        AreaDataEntity areaData = new AreaDataEntity();
        areaData.setResponseId(responseId);
        areaData.setTotalArea(formatDoubleToString(area));
        areaData.setUsedArea(formatDoubleToString(areaUsed));
        areaData.setRemainingArea(formatDoubleToString(areaRemaining));
        areaDataRepo.save(areaData);

        // Save Electricity Data
        ElectricityDataEntity electricityData = new ElectricityDataEntity();
        electricityData.setResponseId(responseId);
        electricityData.setElectricityRequired(formatDoubleToString(requiredElectricity));
        electricityData.setElectricityProduced(formatDoubleToString(totalElectricity));
        electricityData.setElectricitySurplus(formatDoubleToString(surplusElectricity));
        electricityDataRepo.save(electricityData);

        // Save Data Entity
        DataEntity dataEntity = new DataEntity();
        dataEntity.setResponseId(responseId);
        dataEntity.setCropType(req.getCrop_type());
        dataEntity.setAreaSize(req.getArea());
        dataEntity.setSolarPanelCount(req.getSolarCell());
        dataRepo.save(dataEntity);
    }

    private GenericResponse<ResultRes> calculateProcess(CalculationReq req) {

        RiceTypeEntity riceType = riceTypeRepo.findByRiceName(req.getCrop_type());
        EnergyReqEntity energyReq = energyReqRepo.findByTypeId(riceType.getTypeId());
        List<SolarIntEntity> data = solarIntRepo.findAll();
        CentralEntity region = centralRepo.findByTumbol(req.getTumbol());

        double area = Double.parseDouble(req.getArea()) * 1600;
        double requiredElectricity = energyReq.getTotalEnergy() * (area / 1600);

        LocalDate startDate = req.getMonth_start();
        LocalDate endDate = startDate.plusMonths(4);

        List<String> selectMonths = generateMonthInRange(startDate, endDate);
        Map<String, Double> monthlySolarEnergy = getSolarEnergyEachMonth(region.getRegionId(), data, selectMonths);

        double totalSolarEnergy = 0;
        int monthCount = 0;
        int numberOfPanels = (req.getSolarCell() == null) ? 1 : req.getSolarCell();

        double solarEnergyMonth = getSolarEnergyForMonth(monthlySolarEnergy, startDate);
        double dailyEnergy = solarCalculator.calculateDailyEnergy(solarEnergyMonth, 1); // ต่อ 1 แผง

        List<ActivityRes> activities;

        boolean isValidDuration;
        int additionalPanels = 0;
        int loopCounter = 0;

        do {
            if (loopCounter > 1000) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ไม่สามารถคำนวณได้เพราะว่าจำนวนแผงโซล่าเซลล์ไม่สามารถผลิตไฟฟ้าได้ทันภายในระยะเวลาที่ปลูกของพันธุ์นั้นๆ");
            }
            int currentPanels;
            if (req.getSolarCell() != null){
                // รีเซ็ตค่าต่างๆ ก่อนคำนวณใหม่
                currentPanels = req.getSolarCell();
            }else {
                currentPanels = numberOfPanels + additionalPanels;
            }

            activities = activityManager.calculateActivities(
                    req,
                    startDate,
                    dailyEnergy,
                    currentPanels,
                    monthlySolarEnergy,
                    Double.parseDouble(req.getArea())
            );

            LocalDate firstStartDate = activities.get(0).getStartDate();
            LocalDate lastEndDate = activities.get(activities.size() - 1).getEndDate();

            long totalDuration = ChronoUnit.DAYS.between(firstStartDate, lastEndDate);

            long DayLimit = switch (req.getCrop_type()) {
                case "rice-rd47" -> 124;
                case "rice-rd61" -> 118;
                case "rice-rd57" -> 127;
                case "rice-pathum-thani-1" -> 125;
                case "rice-phitsanulok-2" -> 141;
                default -> 0;
            };

            if (totalDuration > DayLimit) {
                additionalPanels++;
                isValidDuration = false;
            } else {
                isValidDuration = true;
            }

            numberOfPanels = currentPanels;
            loopCounter++;

        } while (!isValidDuration);

        List<Map<String, Object>> monthlyDetail = new ArrayList<>();
        for (Map.Entry<String, Double> entry : monthlySolarEnergy.entrySet()) {
            String month = entry.getKey();
            double solarEnergy = entry.getValue();

            totalSolarEnergy += solarEnergy;
            monthCount++;

            int daysInMonth = DAYS_IN_MONTH.getOrDefault(month.toUpperCase(), 0);

            double energyPerDayPerPanel = solarEnergy / 3.6 * PANEL_EFFICIENCY * HOURS_OF_SUNLIGHT * SOLAR_W * numberOfPanels;

            double totalKwhMonthly = energyPerDayPerPanel * daysInMonth;

            Map<String, Object> monthlyResult = Map.of(
                    "month", month,
                    "solarEnergy", formatDoubleToString(solarEnergy),
                    "totalkWh", formatDouble(totalKwhMonthly)
            );

            monthlyDetail.add(monthlyResult);
        }

        double averageSolarEnergy = totalSolarEnergy / monthCount;

        double totalElectricity = monthlyDetail.stream()
                .mapToDouble(month -> (double) month.get("totalkWh"))
                .sum();


        double totalUsed = activities.stream()
                .filter(activity -> activity.getElectricityUsed() > 0)
                .mapToDouble(ActivityRes::getElectricityGenerated)
                .sum();

        double surplusElectricity =  totalElectricity - requiredElectricity;
        double areaUsed = numberOfPanels * PANEL_AREA;
        double areaRemaining = area - areaUsed;

        int areaRai = Integer.parseInt(req.getArea());

        int ResultRice = switch (req.getCrop_type()) {
            case "rice-rd47" -> 793 * areaRai;
            case "rice-rd61" -> 1004 * areaRai;
            case "rice-rd57" -> 714 * areaRai;
            case "rice-pathum-thani-1" -> 712 * areaRai;
            case "rice-phitsanulok-2" -> 807 * areaRai;
            default -> 0;
        };

        ResultRes result = new ResultRes(
                req.getArea(),
                formatDouble(averageSolarEnergy),
                numberOfPanels,
                formatDouble(requiredElectricity),
                formatDouble(totalUsed),
                formatDouble(totalUsed - requiredElectricity),
                totalElectricity,
                formatDouble(surplusElectricity),
                areaUsed,
                areaRemaining,
                ResultRice,
                monthlyDetail,
                activities
        );

        // Save ALL DATA
        String responseId = UUID.randomUUID().toString();
        saveData(
                responseId,
                req,
                area,
                areaUsed,
                areaRemaining,
                requiredElectricity,
                totalElectricity,
                surplusElectricity
        );

        return new GenericResponse<>(HttpStatus.OK, "Success", result);
    }
}
