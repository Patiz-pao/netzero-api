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
                        "Province","Amphoe", "Tumbol", "JAN", "FEB", "MAR", "APR", "MAY", "JUN",
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
            if (req.getCrop_type().equals("rice")) {
                return calculatePanels(requiredElectricityNew, energyPerPanelPerDay, DAYS_RICE);
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

        double energyPerPanelPerDay = calculateEnergyPerPanel(tumbol, data);
        int numberOfPanels = calculateNumberOfPanelsNormal(req, requiredElectricityNew, energyPerPanelPerDay);

        double totalKwh = energyPerPanelPerDay * numberOfPanels * DAYS_RICE;
        double areaUsed = numberOfPanels * PANEL_AREA;
        double areaRemaining = area - areaUsed;

        double surplusElectricity = totalKwh - requiredElectricityNew;

        ResultRes result = new ResultRes(req.getArea(),
                getSolarEnergy(tumbol, data),
                numberOfPanels,
                requiredElectricityNew,
                formatDouble(totalKwh),
                formatDouble(surplusElectricity),
                areaUsed,
                areaRemaining,
                0.0,
                0.0,
                0.0,
                0.0);

        return new GenericResponse<>(HttpStatus.OK, "Success", result);
    }
    //End of Normal Mode
}
