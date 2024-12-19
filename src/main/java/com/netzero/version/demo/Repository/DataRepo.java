package com.netzero.version.demo.Repository;

import com.netzero.version.demo.Entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepo extends JpaRepository<DataEntity, String> {

    DataEntity findByResponseId(String responseId);
}
