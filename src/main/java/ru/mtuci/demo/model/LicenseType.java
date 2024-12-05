package ru.mtuci.demo.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "license_types")
@Entity
public class LicenseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация ID
    private Long id;

    @Column(unique = true, nullable = false) // Уникальное имя типа лицензии
    private String name;

    @Column(name = "default_duration", nullable = false) // Длительность лицензии
    private Integer defaultDuration = 30;

    @Column(name = "description") // Описание лицензии
    private String description;

    @OneToMany(mappedBy = "licenseType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("licenseType")
    private List<License> license;

}
