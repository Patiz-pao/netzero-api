package com.netzero.version.demo.Repository;

import com.netzero.version.demo.Entity.EnergyReqEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnergyReqRepo extends JpaRepository<EnergyReqEntity, String> {

    EnergyReqEntity findByTypeId(String TypeId);
}
