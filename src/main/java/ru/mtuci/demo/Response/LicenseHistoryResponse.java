package ru.mtuci.demo.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LicenseHistoryResponse {
    private Long id;
    private Long licenseId;
    private Long userId;
    private String status;
    private Date changeDate;
    private String description;
}