package com.netzero.version.demo.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netzero.version.demo.Util.GenericResponse;
import com.netzero.version.demo.domain.CalculationDebugReq;
import com.netzero.version.demo.domain.CalculationReq;
import com.netzero.version.demo.domain.ResultRes;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.netzero.version.demo.Util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalculateServices {

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
                        "Province", "Tumbol", "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                        "JUL", "AUG", "SEP", "OCT", "NOV", "DEC",
                        "Rice", "Corn", "Banana", "KaffirLime", "Pomelo", "Coconut"
                });

                for (Map<String, Object> row : apiData) {
                    String[] dataRow = new String[20];
                    dataRow[0] = (String) row.get("Province");
                    dataRow[1] = (String) row.get("Tumbol");
                    dataRow[2] = String.valueOf(row.get("JAN"));
                    dataRow[3] = String.valueOf(row.get("FEB"));
                    dataRow[4] = String.valueOf(row.get("MAR"));
                    dataRow[5] = String.valueOf(row.get("APR"));
                    dataRow[6] = String.valueOf(row.get("MAY"));
                    dataRow[7] = String.valueOf(row.get("JUN"));
                    dataRow[8] = String.valueOf(row.get("JUL"));
                    dataRow[9] = String.valueOf(row.get("AUG"));
                    dataRow[10] = String.valueOf(row.get("SEP"));
                    dataRow[11] = String.valueOf(row.get("OCT"));
                    dataRow[12] = String.valueOf(row.get("NOV"));
                    dataRow[13] = String.valueOf(row.get("DEC"));
                    dataRow[14] = String.valueOf(row.get("Rice"));
                    dataRow[15] = String.valueOf(row.get("Corn"));
                    dataRow[16] = String.valueOf(row.get("Banana"));
                    dataRow[17] = String.valueOf(row.get("KaffirLime"));
                    dataRow[18] = String.valueOf(row.get("Pomelo"));
                    dataRow[19] = String.valueOf(row.get("Coconut"));

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

    private double calculateRequiredTreeCount(double ghg, double solarResult, String treeType) {
        double totalGHG = ghg - solarResult;
        if (totalGHG <= 0) {
            return 0;
        }

        if (treeType.equals("eucalyptus")) {
            return Math.ceil(totalGHG / 15);
        } else if (treeType.equals("mango")) {
            return Math.ceil(totalGHG / 20);
        } else {
            throw new IllegalArgumentException("Invalid tree type. Must be 'eucalyptus' or 'mango'.");

        }
    }

    private double calculateEnergyPerPanel(String tumbol, List<String[]> data) {
        String currentMonth = LocalDate.now().getMonth().toString().substring(0, 3);
        for (String[] row : data) {
            if (row[2].equals(tumbol)) {
                Integer index = MONTH_INDEX.get(currentMonth);
                if (index != null) {
                    return Double.parseDouble(row[index]) / 3.6 * PANEL_EFFICIENCY * HOURS_OF_SUNLIGHT * SOLAR_W;
                }
            }
        }
        throw new IllegalArgumentException("Invalid tumbol");
    }

    private int calculatePanels(double requiredElectricityNew, double energyPerPanelPerDay, int day) {
        int numberOfPanels = 1;
        double totalKwh = energyPerPanelPerDay * numberOfPanels * day;

        while (totalKwh < requiredElectricityNew) {
            numberOfPanels++;
            totalKwh = energyPerPanelPerDay * numberOfPanels * day;
        }

        return numberOfPanels;
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

    private int calculateNumberOfPanelsNormal(CalculationReq req, double requiredElectricityNew, double energyPerPanelPerDay) {
        if (req.getSolarCell() == null) {
            if (req.getType().equals("rice")) {
                return calculatePanels(requiredElectricityNew, energyPerPanelPerDay, DAYS_RICE);
            }
            if (req.getType().equals("corn")) {
                return calculatePanels(requiredElectricityNew, energyPerPanelPerDay, DAYS_CORN);
            }
            if (req.getType().equals("banana")) {
                return calculatePanels(requiredElectricityNew, energyPerPanelPerDay, DAYS_BANANA);
            }
        }
        return req.getSolarCell();
    }

    private double getRequiredElectricityRice(String tumbol, List<String[]> data) {
        for (String[] row : data) {
            if (row[2].equals(tumbol)) {
                return Double.parseDouble(row[15]);
            }
        }
        throw new IllegalArgumentException("Invalid tumbol");
    }

    private double getRequiredElectricityCorn(String tumbol, List<String[]> data) {
        for (String[] row : data) {
            if (row[2].equals(tumbol)) {
                return Double.parseDouble(row[16]);
            }
        }
        throw new IllegalArgumentException("Invalid tumbol");
    }

    private double getRequiredElectricityBanana(String tumbol, List<String[]> data) {
        for (String[] row : data) {
            if (row[2].equals(tumbol)) {
                return Double.parseDouble(row[17]);
            }
        }
        throw new IllegalArgumentException("Invalid tumbol");
    }

    // Normal Mode
    public GenericResponse<ResultRes> calculateRice(CalculationReq req) {
        List<String[]> data = loadCSV();

        if (data != null) {
            if (req.getType().equals("rice")) {
                return handleRiceCalculation(req, data);
            }
            if (req.getType().equals("corn")) {
                return handleCornCalculation(req, data);
            }
            if (req.getType().equals("banana")) {
                return handleBananaCalculation(req, data);
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

        double energyPerPanelPerDay = calculateEnergyPerPanel(tumbol, data);
        int numberOfPanels = calculateNumberOfPanelsNormal(req, requiredElectricityNew, energyPerPanelPerDay);

        double totalKwh = energyPerPanelPerDay * numberOfPanels * DAYS_RICE;
        double areaUsed = numberOfPanels * PANEL_AREA;
        double areaRemaining = area - areaUsed;

        double excessElectricity = totalKwh - requiredElectricityNew;

        double riceResult = (area / 1600) * RICE_KG;

        double solarResult = totalKwh * GHG_SOLAR;
        double ghg = riceResult * GHG_RICE;

        double treeGHG = 0;
        double requiredTreeCount = calculateRequiredTreeCount(ghg, solarResult, req.getTreeType());
        if (req.getTreeType().equals("eucalyptus")) {
            treeGHG = requiredTreeCount * 15;

            areaUsed = areaUsed + (requiredTreeCount * 6);
            areaRemaining = area - areaUsed;
        }
        if (req.getTreeType().equals("mango")) {
            treeGHG = requiredTreeCount * 20;

            areaUsed = areaUsed + (requiredTreeCount * 12);
            areaRemaining = area - areaUsed;
        }

        double newGHG = ghg - treeGHG - solarResult;

        ResultRes result = new ResultRes(req.getArea(),
                getSolarEnergy(tumbol, data),
                numberOfPanels,
                requiredElectricityNew,
                formatDouble(totalKwh),
                formatDouble(excessElectricity),
                areaUsed,
                areaRemaining,
                formatDouble(ghg),
                formatDouble(newGHG),
                requiredTreeCount,
                formatDouble(ghg - solarResult));

        return new GenericResponse<>(HttpStatus.OK, "Success", result);
    }

    private GenericResponse<ResultRes> handleCornCalculation(CalculationReq req, List<String[]> data) {
        String rai = req.getArea();
        String tumbol = req.getTumbol();
        double area = Double.parseDouble(rai) * 1600;

        double requiredElectricity = getRequiredElectricityCorn(tumbol, data);
        double requiredElectricityNew = requiredElectricity * (area / 1600);

        double energyPerPanelPerDay = calculateEnergyPerPanel(tumbol, data);
        int numberOfPanels = calculateNumberOfPanelsNormal(req, requiredElectricityNew, energyPerPanelPerDay);

        double totalKwh = energyPerPanelPerDay * numberOfPanels * DAYS_CORN;
        double areaUsed = numberOfPanels * PANEL_AREA;
        double areaRemaining = area - areaUsed;

        double excessElectricity = totalKwh - requiredElectricityNew;

        double riceResult = (area / 1600) * CORN_KG;

        double solarResult = totalKwh * GHG_SOLAR;
        double ghg = riceResult * GHG_CORN;

        double treeGHG = 0;
        double requiredTreeCount = calculateRequiredTreeCount(ghg, solarResult, req.getTreeType());
        if (req.getTreeType().equals("eucalyptus")) {
            treeGHG = requiredTreeCount * 15;

            areaUsed = areaUsed + (requiredTreeCount * 6);
            areaRemaining = area - areaUsed;
        }
        if (req.getTreeType().equals("mango")) {
            treeGHG = requiredTreeCount * 20;

            areaUsed = areaUsed + (requiredTreeCount * 12);
            areaRemaining = area - areaUsed;
        }

        double newGHG = ghg - treeGHG - solarResult;

        ResultRes result = new ResultRes(req.getArea(),
                getSolarEnergy(tumbol, data),
                numberOfPanels,
                requiredElectricityNew,
                formatDouble(totalKwh),
                formatDouble(excessElectricity),
                areaUsed,
                areaRemaining,
                formatDouble(ghg),
                formatDouble(newGHG),
                requiredTreeCount,
                formatDouble(ghg - solarResult));

        return new GenericResponse<>(HttpStatus.OK, "Success", result);
    }

    private GenericResponse<ResultRes> handleBananaCalculation(CalculationReq req, List<String[]> data) {
        String rai = req.getArea();
        String tumbol = req.getTumbol();
        double area = Double.parseDouble(rai) * 1600;

        double requiredElectricity = getRequiredElectricityBanana(tumbol, data);
        double requiredElectricityNew = requiredElectricity * (area / 1600);

        double energyPerPanelPerDay = calculateEnergyPerPanel(tumbol, data);
        int numberOfPanels = calculateNumberOfPanelsNormal(req, requiredElectricityNew, energyPerPanelPerDay);

        double totalKwh = energyPerPanelPerDay * numberOfPanels * DAYS_BANANA;
        double areaUsed = numberOfPanels * PANEL_AREA;
        double areaRemaining = area - areaUsed;

        double excessElectricity = totalKwh - requiredElectricityNew;

        double riceResult = (area / 1600) * BANANA_KG;

        double solarResult = totalKwh * GHG_SOLAR;
        double ghg = riceResult * GHG_BANANA;

        double treeGHG = 0;
        double requiredTreeCount = calculateRequiredTreeCount(ghg, solarResult, req.getTreeType());
        if (req.getTreeType().equals("eucalyptus")) {
            treeGHG = requiredTreeCount * 15;

            areaUsed = areaUsed + (requiredTreeCount * 6);
            areaRemaining = area - areaUsed;
        }
        if (req.getTreeType().equals("mango")) {
            treeGHG = requiredTreeCount * 20;

            areaUsed = areaUsed + (requiredTreeCount * 12);
            areaRemaining = area - areaUsed;
        }

        double newGHG = ghg - treeGHG - solarResult;

        ResultRes result = new ResultRes(req.getArea(),
                getSolarEnergy(tumbol, data),
                numberOfPanels,
                requiredElectricityNew,
                formatDouble(totalKwh),
                formatDouble(excessElectricity),
                areaUsed,
                areaRemaining,
                formatDouble(ghg),
                formatDouble(newGHG),
                requiredTreeCount,
                formatDouble(ghg - solarResult));

        return new GenericResponse<>(HttpStatus.OK, "Success", result);
    }
    //End of Normal Mode
















    // Debug Mode
    public GenericResponse<ResultRes> calculateRiceDebug(CalculationDebugReq req) {
        if (req.getType().equals("rice")) {
            return handleRiceCalculationDebug(req);
        }
        return new GenericResponse<>(HttpStatus.BAD_REQUEST, "Invalid data");
    }

    private GenericResponse<ResultRes> handleRiceCalculationDebug(CalculationDebugReq req) {
        // แปลงไร่เป็นตารางเมตร
        double area = Double.parseDouble(req.getArea()) * 1600;
        // คำนวณไฟฟ้าที่ต้องการตามพื้นที่
        double requiredElectricity = calculateRequiredElectricityDemand(req.getElectric(), area);
        // คำนวณพลังงานที่ผลิตได้จากแผงโซล่าร์หนึ่งแผงต่อวัน
        double energyPerPanelPerDay = calculateEnergyPerPanelPerDay(req.getSolarEnergyIntensity());
        // คำนวณจำนวนแผงโซล่าร์ที่ต้องการ
        int numberOfPanels = calculateNumberOfPanels(req, requiredElectricity, energyPerPanelPerDay);
        // คำนวณพลังงานรวมที่ผลิตได้ในจำนวนวันที่กำหนด
        double totalKwh = calculateTotalKwh(energyPerPanelPerDay, numberOfPanels, Double.parseDouble(req.getDay()));
        // คำนวณพื้นที่ที่ใช้โดยแผงโซล่าร์
        double areaUsed = calculateAreaUsed(numberOfPanels);
        // คำนวณไฟฟ้าที่ผลิตได้เกินจากที่ต้องการ
        double excessElectricity = calculateExcessElectricity(totalKwh, requiredElectricity);
        // คำนวณผลผลิตข้าวตามพื้นที่
        double riceResult = (area / 1600) * RICE_KG;
        // คำนวณการลดก๊าซเรือนกระจกจากการใช้โซล่าร์
        double solarResult = totalKwh * GHG_SOLAR;
        // คำนวณการปล่อยก๊าซเรือนกระจกจากการปลูกข้าว
        double ghg = riceResult * GHG_RICE;
        // คำนวณจำนวนต้นไม้ที่ต้องการเพื่อลดก๊าซเรือนกระจก
        double requiredTreeCount = calculateRequiredTreeCount(ghg, solarResult, req.getTreeType());
        // คำนวณการลดก๊าซเรือนกระจกจากต้นไม้
        double treeGHG = calculateTreeGHG(req.getTreeType(), requiredTreeCount);
        // อัปเดตพื้นที่ที่ใช้รวมถึงพื้นที่ที่ใช้โดยต้นไม้
        areaUsed = updateAreaUsed(areaUsed, req.getTreeType(), requiredTreeCount);
        // คำนวณพื้นที่ที่เหลือหลังจากใช้โดยแผงโซล่าร์และต้นไม้
        double areaRemaining = area - areaUsed;
        // คำนวณก๊าซเรือนกระจกที่เหลือหลังจากการลดจากโซล่าร์และต้นไม้
        double RemainingGHG = ghg - treeGHG - solarResult;

        ResultRes result = new ResultRes(req.getArea(),
                req.getSolarEnergyIntensity(),
                numberOfPanels,
                requiredElectricity,
                formatDouble(totalKwh),
                formatDouble(excessElectricity),
                areaUsed,
                areaRemaining,
                formatDouble(ghg),
                formatDouble(RemainingGHG),
                requiredTreeCount,
                formatDouble(ghg - solarResult));

        return new GenericResponse<>(HttpStatus.OK, "Success", result);
    }

    private double calculateRequiredElectricityDemand(double requiredElectricity, double area) {
        return requiredElectricity * (area / 1600);
    }

    private double calculateEnergyPerPanelPerDay(double solarEnergyIntensity) {
        return solarEnergyIntensity / 3.6 * PANEL_EFFICIENCY * HOURS_OF_SUNLIGHT * SOLAR_W;
    }

    private int calculateNumberOfPanels(CalculationDebugReq req, double requiredElectricityNew, double energyPerPanelPerDay) {
        if (req.getSolarCell() == null) {
            return calculatePanels(requiredElectricityNew, energyPerPanelPerDay, Integer.parseInt(req.getDay()));
        } else {
            return req.getSolarCell();
        }
    }

    private double calculateTotalKwh(double energyPerPanelPerDay, int numberOfPanels, double day) {
        return energyPerPanelPerDay * numberOfPanels * day;
    }

    private double calculateAreaUsed(int numberOfPanels) {
        return numberOfPanels * PANEL_AREA;
    }

    private double calculateExcessElectricity(double totalKwh, double requiredElectricityNew) {
        double excessElectricity = totalKwh - requiredElectricityNew;
        return Math.max(excessElectricity, 0);
    }

    private double calculateTreeGHG(String treeType, double requiredTreeCount) {
        if (treeType.equals("eucalyptus")) {
            return requiredTreeCount * 15;
        } else if (treeType.equals("mango")) {
            return requiredTreeCount * 20;
        } else {
            throw new IllegalArgumentException("Invalid tree type. Must be 'eucalyptus' or 'mango'.");
        }
    }

    private double updateAreaUsed(double areaUsed, String treeType, double requiredTreeCount) {
        if (treeType.equals("eucalyptus")) {
            return areaUsed + (requiredTreeCount * 6);
        } else if (treeType.equals("mango")) {
            return areaUsed + (requiredTreeCount * 12);
        } else {
            throw new IllegalArgumentException("Invalid tree type. Must be 'eucalyptus' or 'mango'.");
        }
    }
    //End of Debug Mode
}
