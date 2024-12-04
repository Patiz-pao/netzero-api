package com.netzero.version.demo.Services;

import com.netzero.version.demo.Util.GenericResponse;
import com.netzero.version.demo.domain.CalculationDebugReq;
import com.netzero.version.demo.domain.CalculationReq;
import com.netzero.version.demo.domain.ResultRes;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.netzero.version.demo.Util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentServices {
    public GenericResponse<ResultRes> calculationDataDebug(CalculationDebugReq req) {

            if (req.getType().equals("rice")){
                String rai = req.getArea();
                double area = convertArea(rai);

                double requiredElectricity = req.getElectric();
                double requiredElectricityNew = requiredElectricity * (area / 1600);

                double energyPerPanelPerDay = req.getSolarEnergyIntensity() / 3.6 * PANEL_EFFICIENCY * HOURS_OF_SUNLIGHT * SOLAR_W;
                int numberOfPanels;
                if (req.getSolarCell() == null){
                    numberOfPanels = calculatePanels(requiredElectricityNew, energyPerPanelPerDay);
                }else {
                    numberOfPanels = req.getSolarCell();
                }

                double totalKwh = energyPerPanelPerDay * numberOfPanels * DAYS;
                double areaUsed = numberOfPanels * PANEL_AREA;
                double areaRemaining = area - areaUsed;

                double excessElectricity = totalKwh - requiredElectricityNew;
                if (excessElectricity < totalKwh){
                    excessElectricity = 0;
                }

                double riceResult = calculateRiceResult(area);
                double solarResult = totalKwh * GHG_SOLAR;
                double ghg = calculateGHG(riceResult);

                double treeGHG = 0;
                double requiredTreeCount = calculateRequiredTreeCount(ghg, solarResult, req.getTreeType());

                if (req.getTreeType().equals("eucalyptus")){
                    treeGHG = requiredTreeCount * 15;

                    areaUsed = areaUsed + (requiredTreeCount * 6);
                    areaRemaining = area - areaUsed;
                }
                if (req.getTreeType().equals("mango")){
                    treeGHG = requiredTreeCount * 20;

                    areaUsed = areaUsed + (requiredTreeCount * 12);
                    areaRemaining = area - areaUsed;
                }

                double newGHG = ghg - treeGHG - solarResult;


                ResultRes result = new ResultRes(req.getArea(),
                        req.getSolarEnergyIntensity(),
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


        return new GenericResponse<>(HttpStatus.BAD_REQUEST, "Invalid data");
    }

    public GenericResponse<ResultRes> calculationData(CalculationReq req) {
        List<String[]> data = loadCSV();

        if (data != null) {
            if (req.getType().equals("rice")){
                String rai = req.getArea();
                String tumbol = req.getTumbol();
                double area = convertArea(rai);

                double requiredElectricity = getRequiredElectricity(tumbol, data);
                double requiredElectricityNew = requiredElectricity * (area / 1600);

                double energyPerPanelPerDay = calculateEnergyPerPanel(tumbol, data);
                int numberOfPanels = calculatePanels(requiredElectricityNew, energyPerPanelPerDay);

                double totalKwh = energyPerPanelPerDay * numberOfPanels * DAYS;
                double areaUsed = numberOfPanels * PANEL_AREA;
                double areaRemaining = area - areaUsed;

                double excessElectricity = totalKwh - requiredElectricityNew;

                double riceResult = calculateRiceResult(area);
                double solarResult = totalKwh * GHG_SOLAR;
                double ghg = calculateGHG(riceResult);

                double treeGHG = 0;
                double requiredTreeCount = calculateRequiredTreeCount(ghg, solarResult, req.getTreeType());
                if (req.getTreeType().equals("eucalyptus")){
                    treeGHG = requiredTreeCount * 15;

                    areaUsed = areaUsed + (requiredTreeCount * 6);
                    areaRemaining = area - areaUsed;
                }
                if (req.getTreeType().equals("mango")){
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
        }

        return new GenericResponse<>(HttpStatus.BAD_REQUEST, "Invalid data");
    }

    private List<String[]> loadCSV() {
        try (CSVReader reader = new CSVReader(new FileReader("src/main/java/com/netzero/version/demo/Util/Data/data.csv"))) {
            return reader.readAll();
        } catch (IOException | CsvException e) {
            log.error("Error loading CSV data: ", e);
            return null;
        }
    }

    private double getRequiredElectricity(String tumbol, List<String[]> data) {
        for (String[] row : data) {
            if (row[1].equals(tumbol)) {
                return Double.parseDouble(row[3]);
            }
        }
        return 0.0;
    }

    private double getSolarEnergy(String tumbol, List<String[]> data) {
        for (String[] row : data) {
            if (row[1].equals(tumbol)) {
                String currentMonth = LocalDate.now().getMonth().toString().substring(0, 3);
                Integer index = MONTH_INDEX.get(currentMonth);
                if (index != null) {
                    return Double.parseDouble(row[index]);
                }
            }
        }
        return 0.0;
    }

    private double calculateEnergyPerPanel(String tumbol, List<String[]> data) {
        String currentMonth = LocalDate.now().getMonth().toString().substring(0, 3);
        for (String[] row : data) {
            if (row[1].equals(tumbol)) {
                Integer index = MONTH_INDEX.get(currentMonth);
                if (index != null) {
                    return Double.parseDouble(row[index]) / 3.6 * PANEL_EFFICIENCY * HOURS_OF_SUNLIGHT * SOLAR_W;
                }
            }
        }
        return 0.0;
    }

    private double convertArea(String areaString) {
        return Double.parseDouble(areaString) * 1600;
    }

    private int calculatePanels(double requiredElectricityNew, double energyPerPanelPerDay) {
        int numberOfPanels = 1;
        double totalKwh = energyPerPanelPerDay * numberOfPanels * DAYS;

        while (totalKwh < requiredElectricityNew) {
            numberOfPanels++;
            totalKwh = energyPerPanelPerDay * numberOfPanels * DAYS;
        }

        return numberOfPanels;
    }

    private double calculateRiceResult(double area) {
        return (area / 1600) * RICE_KG;
    }

    private double calculateGHG(double riceResult) {
        return riceResult * GHG_RICE;
    }

    private double calculateRequiredTreeCount(double ghg, double solarResult, String treeType) {
        double totalGHG = ghg - solarResult;
        if (totalGHG <= 0){
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

    private double formatDouble(double value) {
        return Double.parseDouble(String.format("%.2f", value));
    }
}
