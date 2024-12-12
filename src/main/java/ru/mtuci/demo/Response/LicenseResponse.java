package ru.mtuci.demo.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LicenseResponse {
    private Long license_id;

    private String key;

    private Long licenseType_id;

    private Boolean blocked;

    private Integer device_count;

    private Long owner_id;

    private Integer duration;

    private String description;

    private Long product_id;

}
