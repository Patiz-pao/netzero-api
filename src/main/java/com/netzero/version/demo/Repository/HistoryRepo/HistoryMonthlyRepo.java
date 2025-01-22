package com.netzero.version.demo.Repository.HistoryRepo;

import com.netzero.version.demo.Entity.HistoryEntity.HistoryMonthlyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryMonthlyRepo extends JpaRepository<HistoryMonthlyEntity, String> {
}
