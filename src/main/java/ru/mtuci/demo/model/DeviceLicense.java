package ru.mtuci.demo.model;
import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "device_licenses")
@Entity
public class DeviceLicense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "license_id", referencedColumnName = "license_id")
    private License license;

    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;

    private LocalDate activationDate;

    public DeviceLicense(License license, Device device, LocalDate activationDate) {
        this.license = license;
        this.device = device;
        this.activationDate = activationDate;
    }
}