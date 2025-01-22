package com.netzero.version.demo.Repository.HistoryRepo;

import com.netzero.version.demo.Entity.HistoryEntity.HistoryActivitiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryActivitiesRepo extends JpaRepository<HistoryActivitiesEntity, String> {
}
