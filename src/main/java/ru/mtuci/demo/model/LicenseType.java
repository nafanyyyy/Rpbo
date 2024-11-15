package ru.mtuci.demo.model;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    private Integer defaultDuration;
    private String description;
    @OneToMany(mappedBy = "licenseType", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("licenseType")
    private List<License> license;
}