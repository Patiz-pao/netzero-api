package com.netzero.version.demo.Repository;

import com.netzero.version.demo.Entity.AreaDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaDataRepo extends JpaRepository<AreaDataEntity, String> {

    AreaDataEntity findByResponseId(String responseId);

}
