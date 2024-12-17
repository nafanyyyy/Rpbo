package ru.mtuci.demo.Request;

import lombok.Data;
@Data
public class ProductRequest {
    private String name;
    private Boolean isBlocked;
}
