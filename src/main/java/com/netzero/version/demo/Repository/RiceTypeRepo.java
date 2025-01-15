package com.netzero.version.demo.Repository;

import com.netzero.version.demo.Entity.RiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiceTypeRepo extends JpaRepository<RiceTypeEntity, String> {
    RiceTypeEntity findByRiceName(String typeId);
}
