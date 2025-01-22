package com.netzero.version.demo.Repository;

import com.netzero.version.demo.Entity.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepo extends JpaRepository<FeedbackEntity,Long> {

}
