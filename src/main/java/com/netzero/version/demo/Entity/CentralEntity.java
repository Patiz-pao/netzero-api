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
@Table(name = "nz_central")
public class CentralEntity {
    @Id
    @Column(name = "region_id")
    private String regionId;

    @Column(name = "province")
    private String province;

    @Column(name = "amphoe")
    private String amphoe;

    @Column(name = "tumbol")
    private String tumbol;
}
