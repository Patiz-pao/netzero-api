package com.netzero.version.demo.domain;

import lombok.Data;

import java.util.List;

@Data
public class DataRes {

    @Data
    public static class LocationResponse {
        private String province;
        private String amphoe;
        private String tumbol;
    }

    @Data
    public static class AreaDataResponse {
        private String totalArea;
        private String usedArea;
        private String remainingArea;
    }

    @Data
    public static class DataResponse {
        private String cropType;
        private String areaSize;
        private Integer solarPanelCount;
    }

    @Data
    public static class ElectricityDataResponse {
        private String electricityRequired;
        private String electricityProduced;
        private String electricitySurplus;
    }

    @Data
    public static class SolarEnergyResponse {
        private String solarIntensity;
        private Integer numberPanelCount;
    }

    @Data
    public static class FullResponse {
        private String responseId;
        private LocationResponse location;
        private AreaDataResponse areaData;
        private DataResponse data;
        private ElectricityDataResponse electricityData;
        private SolarEnergyResponse solarEnergy;
    }

    @Data
    public static class DataResponseList {
        private static final long serialVersionUID = 1L;
        private List<DataResponseList> responses;
    }
}
