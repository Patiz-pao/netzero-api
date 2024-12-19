package com.netzero.version.demo.Repository;

import com.netzero.version.demo.Entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepo extends JpaRepository<LocationEntity, String> {

    LocationEntity findByResponseId(String responseId);
}
