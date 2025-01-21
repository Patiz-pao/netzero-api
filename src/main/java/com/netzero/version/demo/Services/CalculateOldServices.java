package com.netzero.version.demo.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.netzero.version.demo.Util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalculateOldServices {

    private final AreaDataRepo areaDataRepo;
    private final DataRepo dataRepo;
    private final ElectricityDataRepo electricityDataRepo;
    private final LocationRepo locationRepo;
    private final SolarEnergyRepo solarEnergyRepo;

    private final SolarEnergyCalculator solarCalculator;
    private final RiceActivityManager activityManager;

    // Load CSV from API
    @Cacheable("apiDataCache")
    private List<String[]> loadCSV() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String jsonResponse = restTemplate.getForObject(API_URL, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);

            if (responseMap != null && "success".equals(responseMap.get("status"))) {
                List<Map<String, Object>> apiData = (List<Map<String, Object>>) responseMap.get("data");

                List<String[]> processedData = new ArrayList<>();

                processedData.add(new String[]{
                        "Province", "Amphoe", "Tumbol", "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                        "JUL", "AUG", "SEP", "OCT", "NOV", "DEC",
                        "Rice"
                });

                for (Map<String, Object> row : apiData) {
                    String[] dataRow = new String[20];
                    dataRow[0] = (String) row.get("Province");
                    dataRow[1] = (String) row.get("Amphoe");
                    dataRow[2] = (String) row.get("Tumbol");
                    dataRow[3] = String.valueOf(row.get("JAN"));
                    dataRow[4] = String.valueOf(row.get("FEB"));
                    dataRow[5] = String.valueOf(row.get("MAR"));
                    dataRow[6] = String.valueOf(row.get("APR"));
                    dataRow[7] = String.valueOf(row.get("MAY"));
                    dataRow[8] = String.valueOf(row.get("JUN"));
                    dataRow[9] = String.valueOf(row.get("JUL"));
                    dataRow[10] = String.valueOf(row.get("AUG"));
                    dataRow[11] = String.valueOf(row.get("SEP"));
                    dataRow[12] = String.valueOf(row.get("OCT"));
                    dataRow[13] = String.valueOf(row.get("NOV"));
                    dataRow[14] = String.valueOf(row.get("DEC"));
                    dataRow[15] = String.valueOf(row.get("Rice"));

                    processedData.add(dataRow);
                }

                return processedData;
            } else {
                log.error("Failed to retrieve data from API");
                return null;
            }
        } catch (Exception e) {
            log.error("Error loading data from API: ", e);
            return null;
        }
    }

    // Function Constants
    private double formatDouble(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }

    private double getRequiredElectricityRice(String tumbol, List<String[]> data) {
        for (String[] row : data) {
            if (row[2].equals(tumbol)) {
                return Double.parseDouble(row[15]);
            }
        }
        throw new IllegalArgumentException("Invalid tumbol");
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

    private Map<String, Double> getSolarEnergyEachMonth(String tumbol, List<String[]> data, List<String> selectMonths) {
        Map<String, Double> energyPerMonth = new LinkedHashMap<>();
        for (String month : selectMonths) {
            Integer index = MONTH_INDEX.get(month);
            if (index == null) {
                throw new IllegalArgumentException("Invalid month: " + month);
            }
            for (String[] row : data) {
                if (row[2].equals(tumbol)) {
                    energyPerMonth.put(month, Double.parseDouble(row[index]));
                }
            }
        }
        return energyPerMonth;
    }

    private String formatDoubleToString(double value) {
        DecimalFormat df = new DecimalFormat("#.##"); // รูปแบบที่ต้องการ เช่น ทศนิยม 2 ตำแหน่ง
        return df.format(value);
    }

    private double getSolarEnergyForMonth(List<Map<String, Object>> monthlyDetail, LocalDate date) {
        return monthlyDetail.stream()
                .filter(month -> date.getMonth().toString().substring(0, 3)
                        .equals(((String) month.get("month")).substring(0, 3)))
                .mapToDouble(month -> Double.parseDouble((String) month.get("solarEnergy")))
                .sum();
    }

    // Normal Mode
    public GenericResponse<ResultRes> calculateRice(CalculationReq req) {
        List<String[]> data = loadCSV();

        if (data != null) {
            return switch (req.getCrop_type()) {
                case "rice-rd47", "rice-rd61", "rice-rd57", "rice-pathum-thani-1", "rice-phitsanulok-2" ->
                        handleRiceCalculation(req, data);
                default -> new GenericResponse<>(HttpStatus.BAD_REQUEST, "Invalid data");
            };
        }

        return new GenericResponse<>(HttpStatus.BAD_REQUEST, "Invalid data");
    }

    private GenericResponse<ResultRes> handleRiceCalculation(CalculationReq req, List<String[]> data) {
        String rai = req.getArea();
        String tumbol = req.getTumbol();
        double area = Double.parseDouble(rai) * 1600;

        double requiredElectricity = getRequiredElectricityRice(tumbol, data);
        double requiredElectricityNew = requiredElectricity * (area / 1600);

        LocalDate startDate = req.getMonth_start();
        LocalDate endDate = startDate.plusMonths(4);

        List<String> selectMonths = generateMonthInRange(startDate, endDate);
        Map<String, Double> monthlySolarEnergy = getSolarEnergyEachMonth(tumbol, data, selectMonths);

        double totalSolarEnergy = 0;
        int monthCount = 0;

        List<Map<String, Object>> monthlyDetailSolar = new ArrayList<>();
        for (Map.Entry<String, Double> entry : monthlySolarEnergy.entrySet()) {
            String month = entry.getKey();
            double solarEnergy = entry.getValue();

            totalSolarEnergy += solarEnergy;
            monthCount++;

            Map<String, Object> monthlyResult = Map.of(
                    "month", month,
                    "solarEnergy", formatDoubleToString(solarEnergy),
                    "totalkWh", formatDouble(0)
            );

            monthlyDetailSolar.add(monthlyResult);
        }

        double averageSolarEnergy = totalSolarEnergy / monthCount;

        int numberOfPanels;
        if (req.getSolarCell() == null) {
            numberOfPanels = 1;
        }else {
            numberOfPanels = req.getSolarCell();
        }

        double solarEnergyMonth = getSolarEnergyForMonth(monthlyDetailSolar, startDate);
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
                    (Map<String, Double>) monthlyDetailSolar,
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

        double totalElectricity = monthlyDetail.stream()
                .mapToDouble(month -> (double) month.get("totalkWh"))
                .sum();


        double totalUsed = activities.stream()
                .filter(activity -> activity.getElectricityUsed() > 0)
                .mapToDouble(ActivityRes::getElectricityGenerated)
                .sum();

        double surplusElectricity =  totalElectricity - requiredElectricityNew;
        double areaUsed = numberOfPanels * PANEL_AREA;
        double areaRemaining = area - areaUsed;

        int areaRai = Integer.parseInt(rai);

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
                requiredElectricityNew,
                formatDouble(totalUsed),
                formatDouble(totalUsed - requiredElectricityNew),
                totalElectricity,
                surplusElectricity,
                areaUsed,
                areaRemaining,
                ResultRice,
                monthlyDetail,
                activities
        );

        // Create response_id
        String responseId = UUID.randomUUID().toString();

        // LocationEntity
        LocationEntity locationDetail = new LocationEntity();
        locationDetail.setResponseId(responseId);
        locationDetail.setProvince(req.getProvince());
        locationDetail.setAmphoe(req.getAmphoe());
        locationDetail.setTumbol(req.getTumbol());
        locationRepo.save(locationDetail);

        // AreaDataEntity
        AreaDataEntity areaData = new AreaDataEntity();
        areaData.setResponseId(responseId);
        areaData.setTotalArea(formatDoubleToString(area));
        areaData.setUsedArea(formatDoubleToString(areaUsed));
        areaData.setRemainingArea(formatDoubleToString(areaRemaining));
        areaDataRepo.save(areaData);

        // ElectricityDataEntity
        ElectricityDataEntity electricityData = new ElectricityDataEntity();
        electricityData.setResponseId(responseId);
        electricityData.setElectricityRequired(formatDoubleToString(requiredElectricityNew));
        electricityData.setElectricityProduced(formatDoubleToString(totalElectricity));
        electricityData.setElectricitySurplus(formatDoubleToString(surplusElectricity));
        electricityDataRepo.save(electricityData);

        // DataEntity
        DataEntity dataEntity = new DataEntity();
        dataEntity.setResponseId(responseId);
        dataEntity.setCropType(req.getCrop_type());
        dataEntity.setAreaSize(req.getArea());
        dataEntity.setSolarPanelCount(req.getSolarCell());
        dataRepo.save(dataEntity);

        // SolarEnergyEntity
        SolarEnergyEntity solarEnergy = new SolarEnergyEntity();
        solarEnergy.setResponseId(responseId);

        double averageSolarIntensity = totalSolarEnergy / 4;
        solarEnergy.setSolarIntensity(formatDoubleToString(averageSolarIntensity));

        Integer solarCell = req.getSolarCell();
        if (solarCell == null) {
            solarCell = numberOfPanels;
        }
        solarEnergy.setSolarPanelCount(solarCell);

        solarEnergyRepo.save(solarEnergy);

        return new GenericResponse<>(HttpStatus.OK, "Success", result);
    }

    // End of Normal Mode
}