package ru.mtuci.demo.model;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "license_id", referencedColumnName = "license_id")
    private License license;
    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private Device device;
    private Date activationDate;
}