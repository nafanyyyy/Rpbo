package ru.mtuci.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="licenses")
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // Добавлено для автогенерации идентификатора
    @Column(name="license_id")
    private Long license_id;

    @Column(name="key")
    private String key;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private LicenseType licenseType;

    @Column(name="first_date_activate")
    private LocalDate first_date_activate;

    @Column(name="ending_date")
    private LocalDate ending_date;

    @Column(name="blocked")
    private Boolean blocked;

    @Column(name="device_count")
    private Integer device_count;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @Column(name="duration")
    private Integer duration;

    @Column(name="description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Products product;

    @OneToMany(mappedBy = "license", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("license")
    private List<LicenseHistory> licenseHistories;

    @OneToMany(mappedBy = "license", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("license")
    private List<DeviceLicense> deviceLicenses;

}