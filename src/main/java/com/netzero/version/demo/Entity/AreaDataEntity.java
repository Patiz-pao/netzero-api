package com.netzero.version.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "nz_area_data")
public class AreaDataEntity {
    @Id
    @Column(name = "response_id")
    private String responseId;

    @Column(name = "total_area")
    private String totalArea;

    @Column(name = "used_area")
    private String usedArea;

    @Column(name = "remaining_area")
    private String remainingArea;
}
