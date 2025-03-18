package com.netzero.version.demo.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netzero.version.demo.Entity.CentralEntity;
import com.netzero.version.demo.Entity.EnergyReqEntity;
import com.netzero.version.demo.Entity.HistoryEntity.HistoryActivitiesEntity;
import com.netzero.version.demo.Entity.HistoryEntity.HistoryDataEntity;
import com.netzero.version.demo.Entity.HistoryEntity.HistoryMonthlyEntity;
import com.netzero.version.demo.Entity.RiceTypeEntity;
import com.netzero.version.demo.Entity.SolarIntEntity;
import com.netzero.version.demo.Exception.NetZeroException;
import com.netzero.version.demo.Repository.CentralRepo;
import com.netzero.version.demo.Repository.EnergyReqRepo;
import com.netzero.version.demo.Repository.HistoryRepo.HistoryActivitiesRepo;
import com.netzero.version.demo.Repository.HistoryRepo.HistoryDataRepo;
import com.netzero.version.demo.Repository.HistoryRepo.HistoryMonthlyRepo;
import com.netzero.version.demo.Repository.RiceTypeRepo;
import com.netzero.version.demo.Repository.SolarIntRepo;
import com.netzero.version.demo.Services.ActivityService.RiceActivityManager;
import com.netzero.version.demo.Services.ActivityService.SolarEnergyCalculator;
import com.netzero.version.demo.Util.CalculationUtils;
import com.netzero.version.demo.Util.GenericResponse;
import com.netzero.version.demo.domain.ActivityRes;
import com.netzero.version.demo.domain.CalculationReq;
import com.netzero.version.demo.domain.ResultRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.netzero.version.demo.Util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalculateServices {
    private final SolarEnergyCalculator solarCalculator;
    private final RiceActivityManager activityManager;

    private final CalculationUtils calculationUtils;

    private final RiceTypeRepo riceTypeRepo;
    private final EnergyReqRepo energyReqRepo;
    private final CentralRepo centralRepo;
    private final SolarIntRepo solarIntRepo;

    private final HistoryDataRepo historyDataRepo;
    private final HistoryMonthlyRepo historyMonthlyRepo;
    private final HistoryActivitiesRepo historyActivitiesRepo;

    public GenericResponse<ResultRes> checkValue(CalculationReq req) throws NetZeroException {
        if (req.getCrop_type().equals(RICE_RD47) || req.getCrop_type().equals(RICE_RD61) ||
                req.getCrop_type().equals(RICE_RD57) || req.getCrop_type().equals(RICE_PATHUM_THANI_1) ||
                req.getCrop_type().equals(RICE_PHITSANULOK_2)) {
            return calculateProcess(req);
        } else {
            return new GenericResponse<>(HttpStatus.BAD_REQUEST, "Invalid data");
        }
    }


    private GenericResponse<ResultRes> calculateProcess(CalculationReq req) throws NetZeroException {

        RiceTypeEntity riceType = riceTypeRepo.findByRiceName(req.getCrop_type());
        EnergyReqEntity energyReq = energyReqRepo.findByTypeId(riceType.getTypeId());
        List<SolarIntEntity> data = solarIntRepo.findAll();
        CentralEntity region = centralRepo.findByTumbol(req.getTumbol());

        double area = Double.parseDouble(req.getArea()) * 1600;
        double requiredElectricity = energyReq.getTotalEnergy() * (area / 1600);

        LocalDate startDate = req.getMonth_start();
        LocalDate endDate = startDate.plusMonths(5);

        List<String> selectMonths = calculationUtils.generateMonthInRange(startDate, endDate);
        Map<String, Double> monthlySolarEnergy = calculationUtils.getSolarEnergyEachMonth(region.getRegionId(), data, selectMonths);

        double totalSolarEnergy = 0;
        int monthCount = 0;
        int numberOfPanels = (req.getSolarCell() == null) ? 1 : req.getSolarCell();

        double solarEnergyMonth = calculationUtils.getSolarEnergyForMonth(monthlySolarEnergy, startDate);
        double dailyEnergy = solarCalculator.calculateDailyEnergy(solarEnergyMonth, 1); // ต่อ 1 แผง

        List<ActivityRes> activities;

        boolean isValidDuration;
        int additionalPanels = 0;
        int loopCounter = 0;

        LocalDate lastEndDate;
        do {
            if (loopCounter > 1000) {
                throw new NetZeroException(HttpStatus.BAD_REQUEST, "ไม่สามารถคำนวณได้เพราะว่าจำนวนแผงโซล่าเซลล์ไม่สามารถผลิตไฟฟ้าได้ทันภายในระยะเวลาที่ปลูกของพันธุ์นั้นๆ");
            }
            int currentPanels;
            if (req.getSolarCell() != null) {
                // รีเซ็ตค่าต่างๆ ก่อนคำนวณใหม่
                currentPanels = req.getSolarCell();
            } else {
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
            lastEndDate = activities.get(activities.size() - 1).getEndDate();

            long totalDuration = ChronoUnit.DAYS.between(firstStartDate, lastEndDate);

            long DayLimit = switch (req.getCrop_type()) {
                case RICE_RD47 -> RICE_RD47_DAY;
                case RICE_RD61 -> RICE_RD61_DAY;
                case RICE_RD57 -> RICE_RD57_DAY;
                case RICE_PATHUM_THANI_1 -> RICE_PATHUM_THANI_1_DAY;
                case RICE_PHITSANULOK_2 -> RICE_PHITSANULOK_2_DAY;
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
                    "solarEnergy", calculationUtils.formatDoubleToString(solarEnergy),
                    "totalkWh", calculationUtils.formatDouble(totalKwhMonthly)
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

        Map<String, Object> energyUsageDetails = calculationUtils.calculateEnergyToolsUsage(req, Double.parseDouble(req.getArea()));

        double totalEnergyTractors = (double) energyUsageDetails.get("totalEnergyTractors");
        double totalEnergyWaterPump = (double) energyUsageDetails.get("totalEnergyWaterPump");
        double totalEnergyDrone = (double) energyUsageDetails.get("totalEnergyDrone");

        double surplusElectricity = totalElectricity - requiredElectricity;
        double areaUsed = numberOfPanels * PANEL_AREA;
        double areaRemaining = area - areaUsed;

        int areaRai = Integer.parseInt(req.getArea());

        int ResultRice = switch (req.getCrop_type()) {
            case RICE_RD47 -> RICE_RD47_RESULT * areaRai;
            case RICE_RD61 -> RICE_RD61_RESULT * areaRai;
            case RICE_RD57 -> RICE_RD57_RESULT * areaRai;
            case RICE_PATHUM_THANI_1 -> RICE_PATHUM_THANI_1_RESULT * areaRai;
            case RICE_PHITSANULOK_2 -> RICE_PHITSANULOK_2_RESULT * areaRai;
            default -> 0;
        };

        ResultRes result = new ResultRes(
                req.getArea(),
                calculationUtils.formatDouble(averageSolarEnergy),
                numberOfPanels,
                calculationUtils.formatDouble(requiredElectricity),
                calculationUtils.formatDouble(totalUsed),
                calculationUtils.formatDouble(totalUsed - requiredElectricity),
                calculationUtils.formatDouble(totalElectricity),
                calculationUtils.formatDouble(surplusElectricity),
                areaUsed,
                areaRemaining,
                ResultRice,
                monthlyDetail,
                activities
        );

        saveData(
                req,
                totalUsed,
                averageSolarEnergy,
                totalEnergyTractors,
                totalEnergyWaterPump,
                totalEnergyDrone,
                numberOfPanels,
                areaUsed,
                areaRemaining,
                requiredElectricity,
                totalElectricity,
                surplusElectricity,
                ResultRice,
                activities,
                monthlyDetail,
                region.getRegionId(),
                riceType.getTypeId(),
                lastEndDate
        );

        return new GenericResponse<>(HttpStatus.OK, "Success", result);
    }

    private void saveData(CalculationReq req,double totalUsed, double averageSolarEnergy, double totalEnergyTractors, double totalEnergyWaterPump, double totalEnergyDrone, int numberOfPanels, double areaUsed, double areaRemaining, double requiredElectricity, double totalElectricity, double surplusElectricity, int ResultRice, List<ActivityRes> activities, List<Map<String, Object>> monthlyDetail, String regionId, String typeId, LocalDate lastEndDate) throws NetZeroException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        String historyId = UUID.randomUUID().toString();

        try {
            HistoryDataEntity historyData = new HistoryDataEntity();
            historyData.setHistoryId(historyId);
            historyData.setRegionId(regionId);
            historyData.setTypeId(typeId);
            historyData.setCreatedDate(LocalDateTime.now());
            historyData.setSolarEnergyIntensity(calculationUtils.formatDouble(averageSolarEnergy));
            historyData.setNumberOfPanels(numberOfPanels);
            historyData.setRequiredElectricity(calculationUtils.formatDouble(requiredElectricity));
            historyData.setProducedElectricity(calculationUtils.formatDouble(totalElectricity));
            historyData.setSurplusElectricity(calculationUtils.formatDouble(surplusElectricity));
            historyData.setUsableElectricity(calculationUtils.formatDouble(totalUsed));
            historyData.setSurplusElectricityUsable(calculationUtils.formatDouble(totalUsed - requiredElectricity));
            historyData.setAreaUsed(areaUsed);
            historyData.setAreaRemaining(areaRemaining);
            historyData.setResultRice(ResultRice);
            historyData.setEnergyTractor(totalEnergyTractors);
            historyData.setEnergyDrone(totalEnergyDrone);
            historyData.setEnergyWaterPumping(totalEnergyWaterPump);
            historyData.setStartDate(req.getMonth_start());
            historyData.setEndDate(lastEndDate);
            historyDataRepo.save(historyData);

            String activitiesJsonNode = objectMapper.writeValueAsString(activities);
            HistoryActivitiesEntity activityEntity = new HistoryActivitiesEntity();
            activityEntity.setActivityId(UUID.randomUUID().toString());
            activityEntity.setHistoryId(historyId);
            activityEntity.setActivities(activitiesJsonNode);
            historyActivitiesRepo.save(activityEntity);

            String monthlyDetailsJsonNode = objectMapper.writeValueAsString(monthlyDetail);
            HistoryMonthlyEntity monthlyEntity = new HistoryMonthlyEntity();
            monthlyEntity.setMonthlyDetailId(UUID.randomUUID().toString());
            monthlyEntity.setHistoryId(historyId);
            monthlyEntity.setMonthlyData(monthlyDetailsJsonNode);
            historyMonthlyRepo.save(monthlyEntity);
        }catch (JsonProcessingException ex){
            throw new NetZeroException(HttpStatus.INTERNAL_SERVER_ERROR, "ไม่สามารถแปลงข้อมูลเป็น JSON ได้: " + ex.getMessage());
        }

    }
}
