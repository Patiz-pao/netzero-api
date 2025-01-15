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
@Table(name = "nz_energy_req")
public class EnergyReqEntity {
    @Id
    @Column(name = "energy_id")
    private String energyId;

    @Column(name = "type_id")
    private String typeId;

    @Column(name = "total_energy")
    private float totalEnergy;

    @Column(name = "plowing")
    private float plowing;

    @Column(name = "fertilizing")
    private float fertilizing;

    @Column(name = "seeding")
    private float seeding;

    @Column(name = "water_pumping")
    private float waterPumping;
}
