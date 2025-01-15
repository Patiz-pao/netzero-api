package com.netzero.version.demo.Repository;

import com.netzero.version.demo.Entity.SolarIntEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolarIntRepo extends JpaRepository<SolarIntEntity, String> {
}
