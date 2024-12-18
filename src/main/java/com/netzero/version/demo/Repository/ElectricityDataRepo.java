package com.netzero.version.demo.Repository;

import com.netzero.version.demo.Entity.ElectricityDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectricityDataRepo extends JpaRepository<ElectricityDataEntity, String> {
}
