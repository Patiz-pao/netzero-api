package com.netzero.version.demo.Entity.HistoryEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter

@Entity
@Table(name = "nz_history_activities")
public class HistoryActivitiesEntity {
    @Id
    @Column(name = "activity_id")
    private String activityId;

    @Column(name = "history_id")
    private String historyId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "activities", columnDefinition = "jsonb")
    private String activities;
}
