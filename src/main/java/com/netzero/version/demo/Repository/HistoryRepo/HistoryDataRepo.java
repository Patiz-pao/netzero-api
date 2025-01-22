package com.netzero.version.demo.Repository.HistoryRepo;

import com.netzero.version.demo.Entity.HistoryEntity.HistoryDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryDataRepo extends JpaRepository<HistoryDataEntity, String> {
}
