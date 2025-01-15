package com.netzero.version.demo.Repository;

import com.netzero.version.demo.Entity.CentralEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CentralRepo extends JpaRepository<CentralEntity, String> {
    CentralEntity findByTumbol(String regionId);
}
