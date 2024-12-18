package com.netzero.version.demo.Repository;

import com.netzero.version.demo.Entity.SolarEnergyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolarEnergyRepo extends JpaRepository<SolarEnergyEntity, String> {
}
