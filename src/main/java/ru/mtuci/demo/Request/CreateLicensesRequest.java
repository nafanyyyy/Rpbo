package ru.mtuci.demo.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateLicensesRequest {
    Long ownerId;
    Long productId;
    Long licenseTypeId;
}
