package com.netzero.version.demo.Services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentServices {
    private List<String[]> loadCSV() {
        try (CSVReader reader = new CSVReader(new FileReader("src/main/java/com/netzero/version/demo/Util/Data/data.csv"))) {
            return reader.readAll();
        } catch (IOException | CsvException e) {
            log.error("Error loading CSV data: ", e);
            return null;
        }
    }
}
