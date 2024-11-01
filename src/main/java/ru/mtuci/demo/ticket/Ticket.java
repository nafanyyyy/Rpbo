package ru.mtuci.demo.ticket;

import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Ticket {

    private Date serverDate;
    private Long ticketLifetime;
    private Date activationDate;
    private Date expirationDate;
    private UUID userId;
    private String deviceId;
    private Boolean licenseBlocked;
    private String digitalSignature;

}