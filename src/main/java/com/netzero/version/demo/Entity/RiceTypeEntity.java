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
@Table(name = "nz_rice_type")
public class RiceTypeEntity {
    @Id
    @Column(name = "type_id")
    private String typeId;

    @Column(name = "rice_name")
    private String riceName;

    @Column(name = "description")
    private String description;

    @Column(name = "yield")
    private int yield;
}
