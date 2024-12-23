package com.netzero.version.demo.Services;

import com.netzero.version.demo.Entity.*;
import com.netzero.version.demo.Repository.*;
import com.netzero.version.demo.domain.DataRes;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentServices {

    @Autowired
    private final AreaDataRepo areaDataRepo;
    private final DataRepo dataRepo;
    private final ElectricityDataRepo electricityDataRepo;
    private final LocationRepo locationRepo;
    private final SolarEnergyRepo solarEnergyRepo;

    private List<String[]> loadCSV() {
        try (CSVReader reader = new CSVReader(new FileReader("src/main/java/com/netzero/version/demo/Util/Data/data.csv"))) {
            return reader.readAll();
        } catch (IOException | CsvException e) {
            log.error("Error loading CSV data: ", e);
            return null;
        }
    }

    //TODO: 19/12/67 getAll Service
    @Cacheable(value = "getAllData")
    public List<DataRes.FullResponse> getAllData(){
        log.info("Fetching data from database...Krubfuiiiii");
        //ดึงข้อมูลมาจากฐานข้อมูล
        List<LocationEntity> locationEntities = locationRepo.findAll();
        //สร้าง List ไว้ Response
        List<DataRes.FullResponse> dataResponseList = new ArrayList<>();

        for (LocationEntity entity: locationEntities){
            DataRes.FullResponse fullResponse = new DataRes.FullResponse();
            fullResponse.setResponseId(entity.getResponseId().toString());

            //nz_location
                DataRes.LocationResponse locationResponse = new DataRes.LocationResponse();
                locationResponse.setProvince(entity.getProvince());
                locationResponse.setAmphoe(entity.getAmphoe());
                locationResponse.setTumbol(entity.getTumbol());
                fullResponse.setLocation(locationResponse);

            //nz_area_data
            AreaDataEntity areaDataEntity = areaDataRepo.findByResponseId(entity.getResponseId());
            if (areaDataEntity != null){
                DataRes.AreaDataResponse areaDataResponse = new DataRes.AreaDataResponse();
                areaDataResponse.setTotalArea(areaDataEntity.getTotalArea());
                areaDataResponse.setUsedArea(areaDataEntity.getUsedArea());
                areaDataResponse.setRemainingArea(areaDataEntity.getRemainingArea());
                fullResponse.setAreaData(areaDataResponse);
            }

            //nz_data
            DataEntity dataEntity = dataRepo.findByResponseId(entity.getResponseId());
            if (dataEntity != null){
                DataRes.DataResponse dataResponse = new DataRes.DataResponse();
                dataResponse.setCropType(dataEntity.getCropType());
                dataResponse.setAreaSize(dataEntity.getAreaSize());
                dataResponse.setSolarPanelCount(dataEntity.getSolarPanelCount());
                fullResponse.setData(dataResponse);
            }

            //nz_electricity
            ElectricityDataEntity electricEntity = electricityDataRepo.findByResponseId(entity.getResponseId());
            if (electricEntity != null){
                DataRes.ElectricityDataResponse electricityDataResponse = new DataRes.ElectricityDataResponse();
                electricityDataResponse.setElectricityRequired(electricEntity.getElectricityRequired());
                electricityDataResponse.setElectricityProduced(electricEntity.getElectricityProduced());
                electricityDataResponse.setElectricitySurplus(electricEntity.getElectricityProduced());
                fullResponse.setElectricityData(electricityDataResponse);
            }

            //nz_solar_energy
            SolarEnergyEntity solarEntity = solarEnergyRepo.findByResponseId(entity.getResponseId());
            if (solarEntity != null){
                DataRes.SolarEnergyResponse solarRes = new DataRes.SolarEnergyResponse();
                solarRes.setSolarIntensity(solarEntity.getSolarIntensity());
                solarRes.setNumberPanelCount(solarEntity.getSolarPanelCount());
                fullResponse.setSolarEnergy(solarRes);
            }

            dataResponseList.add(fullResponse);

        }

        return dataResponseList;
    }


}
