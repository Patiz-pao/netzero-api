package com.netzero.version.demo.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netzero.version.demo.Entity.*;
import com.netzero.version.demo.Repository.*;
import com.netzero.version.demo.Util.GenericResponse;
import com.netzero.version.demo.domain.CalculationReq;
import com.netzero.version.demo.domain.ResultRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

import static com.netzero.version.demo.Util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalculateServices {

    private final AreaDataRepo areaDataRepo;
    private final DataRepo dataRepo;
    private final ElectricityDataRepo electricityDataRepo;
    private final LocationRepo locationRepo;
    private final SolarEnergyRepo solarEnergyRepo;

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
    // Prepare data from CSV (อาจจะกลับมาใช้)
//    private List<String[]> loadCSV() {
//        try (CSVReader reader = new CSVReader(new FileReader("src/main/java/com/netzero/version/demo/Util/Data/data.csv"))) {
//            return reader.readAll();
//        } catch (IOException | CsvException e) {
//            log.error("Error loading CSV data: ", e);
//            return null;
//        }
//    }

    // Function Constants
    private double formatDouble(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }

    private int calculatePanels(CalculationReq req, double requiredElectricityNew, double totalElectricity) {
        int numberOfPanels = 1;

        if (req.getSolarCell() == null) {
            while (totalElectricity < requiredElectricityNew) {
                numberOfPanels++;
                totalElectricity += totalElectricity;
            }
            return numberOfPanels;
        }

        return req.getSolarCell();
    }

    private double getSolarEnergy(String tumbol, List<String[]> data) {
        for (String[] row : data) {
            if (row[2].equals(tumbol)) {
                String currentMonth = LocalDate.now().getMonth().toString().substring(0, 3);
                Integer index = MONTH_INDEX.get(currentMonth);
                if (index != null) {
                    return Double.parseDouble(row[index]);
                }
            }
        }
        return 0.0;
    }

    private double getRequiredElectricityRice(String tumbol, List<String[]> data) {
        for (String[] row : data) {
            if (row[2].equals(tumbol)) {
                return Double.parseDouble(row[15]);
            }
        }
        throw new IllegalArgumentException("Invalid tumbol");
    }

    private List<String> generateMonthInRange(String start, String end) {
        List<String> months = Arrays.asList(
                "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");

        int startIndex = months.indexOf(start.toUpperCase());
        int endIndex = months.indexOf(end.toUpperCase());

        if (startIndex == -1 || endIndex == -1) {
            throw new IllegalArgumentException("Invalid Month pprovided.");
        }

        List<String> result = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            result.add(months.get(i % 12));
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

    // Normal Mode
    public GenericResponse<ResultRes> calculateRice(CalculationReq req) {
        List<String[]> data = loadCSV();

        if (data != null) {
            if (req.getCrop_type().equals("rice")) {
                return handleRiceCalculation(req, data);
            }
        }

        return new GenericResponse<>(HttpStatus.BAD_REQUEST, "Invalid data");
    }

    private GenericResponse<ResultRes> handleRiceCalculation(CalculationReq req, List<String[]> data) {
        String rai = req.getArea();
        String tumbol = req.getTumbol();
        double area = Double.parseDouble(rai) * 1600;

        double requiredElectricity = getRequiredElectricityRice(tumbol, data);
        double requiredElectricityNew = requiredElectricity * (area / 1600);

        List<String> selectMonths = generateMonthInRange(req.getMonth_start(), req.getMonth_end());
        Map<String, Double> monthlySolarEnergy = getSolarEnergyEachMonth(tumbol, data, selectMonths);

        double totalSolarEnergy = 0;
        int monthCount = 0;

        List<Map<String, Object>> monthlyDetail = new ArrayList<>();
        for (Map.Entry<String, Double> entry : monthlySolarEnergy.entrySet()) {
            String month = entry.getKey();
            double solarEnergy = entry.getValue();

            totalSolarEnergy += solarEnergy;
            monthCount++;

            double energyPerDayPerPanel = solarEnergy / 3.6 * PANEL_EFFICIENCY * HOURS_OF_SUNLIGHT * SOLAR_W;

            double totalKwhMonthly = energyPerDayPerPanel * 30;

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

        int numberOfPanels = calculatePanels(req, requiredElectricityNew, totalElectricity);
        totalElectricity = totalElectricity * numberOfPanels;
        double surplusElectricity = totalElectricity - requiredElectricityNew;

        double areaUsed = numberOfPanels * PANEL_AREA;
        double areaRemaining = area - areaUsed;

        ResultRes result = new ResultRes(
                req.getArea(),
                formatDouble(averageSolarEnergy),
                numberOfPanels,
                requiredElectricityNew,
                totalElectricity,
                surplusElectricity,
                areaUsed,
                areaRemaining,
                monthlyDetail
        );

        //create response_id
        String responseId = UUID.randomUUID().toString();

        //LocationEntity
        LocationEntity locationDetail = new LocationEntity();
            locationDetail.setResponseId(responseId);
            locationDetail.setProvince(req.getProvince());
            locationDetail.setAmphoe(req.getAmphoe());
            locationDetail.setTumbol(req.getTumbol());
            locationRepo.save(locationDetail);

        //AreaDataEntity
        AreaDataEntity areaData = new AreaDataEntity();
            areaData.setResponseId(responseId);
            areaData.setTotalArea(formatDoubleToString(area));
            areaData.setUsedArea(formatDoubleToString(areaUsed));
            areaData.setRemainingArea(formatDoubleToString(areaRemaining));
            areaDataRepo.save(areaData);

        //ElectricityDataEntity
        ElectricityDataEntity electricityData = new ElectricityDataEntity();
            electricityData.setResponseId(responseId);
            electricityData.setElectricityRequired(formatDoubleToString(requiredElectricityNew));
            electricityData.setElectricityProduced(formatDoubleToString(totalElectricity));
            electricityData.setElectricitySurplus(formatDoubleToString(surplusElectricity));
            electricityDataRepo.save(electricityData);

        //DataEntity
        DataEntity dataEntity = new DataEntity();
            dataEntity.setResponseId(responseId);
            dataEntity.setCropType(req.getCrop_type());
            dataEntity.setAreaSize(req.getArea());
            dataEntity.setSolarPanelCount(req.getSolarCell());
            dataRepo.save(dataEntity);

        //SolarEnergyEntity
        SolarEnergyEntity solarEnergy = new SolarEnergyEntity();
            solarEnergy.setResponseId(responseId);

            double averageSolarIntensity = totalSolarEnergy/4;
            solarEnergy.setSolarIntensity(formatDoubleToString(averageSolarIntensity));

            Integer solarCell = req.getSolarCell();
            if (solarCell == null){
                solarCell = numberOfPanels;
            }
            solarEnergy.setSolarPanelCount(solarCell);

            solarEnergyRepo.save(solarEnergy);

        return new GenericResponse<>(HttpStatus.OK, "Success", result);
        }

        //End of Normal Mode
    }