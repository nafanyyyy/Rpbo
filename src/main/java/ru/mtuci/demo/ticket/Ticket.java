package ru.mtuci.demo.ticket;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import ru.mtuci.demo.model.Device;
import ru.mtuci.demo.model.License;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

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
    private Long licenseTypeId;
    public Ticket generateTicket(License license, Device device, Long userId) {
        Ticket ticket = new Ticket();

        ticket.setServerDate(new Date());
        ticket.setTicketLifetime(license.getDuration() * 24L * 60 * 60 * 1000);
        ticket.setActivationDate(java.sql.Date.valueOf(license.getFirst_date_activate()));
        ticket.setExpirationDate(java.sql.Date.valueOf(license.getEnding_date()));
        ticket.setUserId(UUID.nameUUIDFromBytes(userId.toString().getBytes()));
        ticket.setDeviceId(device.getId().toString());
        ticket.setLicenseBlocked(license.getBlocked());
        ticket.setDigitalSignature(generateDigitalSignature(license, device, userId));
        ticket.setLicenseTypeId(license.getLicenseType().getId());
        return ticket;
    }
    private String generateDigitalSignature(License license, Device device, Long userId) {
        try {
            String rawData = license.getKey() + device.getId() + userId + license.getEnding_date();

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            byte[] hash = messageDigest.digest(rawData.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при генерации цифровой подписи", e);
        }
    }
}