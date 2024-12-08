package ru.mtuci.demo.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.mtuci.demo.exception.ActivationException;
import ru.mtuci.demo.exception.DeviceNotFoundException;
import ru.mtuci.demo.exception.LicenseNotFoundException;
import ru.mtuci.demo.model.Device;
import ru.mtuci.demo.model.DeviceLicense;
import ru.mtuci.demo.model.License;
import ru.mtuci.demo.model.User;
import ru.mtuci.demo.repo.DeviceLicenseRepository;
import ru.mtuci.demo.repo.LicenseRepository;
import ru.mtuci.demo.services.*;
import ru.mtuci.demo.ticket.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LicenseServiceImpl implements LicenseService {

    private final LicenseRepository licenseRepository;
    private final ProductService productService;
    private final UserService userService;
    private final LicenseTypeService licenseTypeService;
    private final LicenseHistoryService licenseHistoryService;
    private final DeviceService deviceService;
    private final DeviceLicenseRepository deviceLicenseRepository;



    @Override
    public void add(License license) {
        licenseRepository.save(license);
    }

    @Override
    public List<License> getAll() {
        return licenseRepository.findAll();
    }

    @Override
    public License getById(Long id) {
        return licenseRepository.findById(id).orElse(null);
    }

    @Override
    public License getByKey(String key) {
        return licenseRepository.findByKey(key).orElse(null);
    }

    @Override
    public ResponseEntity<LicenseResponse> createLicense(Long productId, Long ownerId, Long licenseTypeId, License licenseParameters) {
        var product = productService.getProductById(productId);
        if (product == null) {
            throw new RuntimeException("Продукт не найден");
        }

        var user = userService.getById(ownerId);
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }

        var licenseType = licenseTypeService.getLicenseTypeById(licenseTypeId);
        if (licenseType == null) {
            throw new RuntimeException("Тип лицензии не найден");
        }

        License license = new License();
        license.setProduct(product);
        license.setOwner(user);
        license.setLicenseType(licenseType);
        license.setKey(generateActivationCode());
        license.setDescription(licenseType.getDescription());
        license.setBlocked(false);
        license.setDevice_count(0);
        license.setDuration(licenseType.getDefaultDuration());


        licenseRepository.save(license);

        licenseHistoryService.recordLicenseChange(license, user, "Создана", "Лицензия успешно создана");

        LicenseResponse response = new LicenseResponse(
            license.getLicense_id(), license.getKey(),
                licenseTypeId, license.getBlocked(),
                license.getDevice_count(), ownerId,
                license.getDuration(), license.getDescription(),
                productId
        );

        return ResponseEntity.ok(response);
    }

    @Override
    public Ticket activateLicense(ActivationRequest request, User user) {
        License license = licenseRepository.findByKey(request.getActivationCode())
                .orElseThrow(() -> new LicenseNotFoundException("Лицензия не найдена"));

        if (license.getBlocked()) {
            throw new ActivationException("Активация невозможна");
        }

        verifyLicenseOwnership(license, user.getId());

        Device device = deviceService.registerOrUpdateDevice(request.getDeviceInfo(),user,request.getDeviceName());

        license.setUser(user);
        license.setDevice_count(license.getDevice_count()+1);
        license.setFirst_date_activate(LocalDate.now());
        license.setEnding_date(license.getFirst_date_activate().plusDays(license.getDuration()));

        DeviceLicense deviceLicense = new DeviceLicense(license, device, license.getFirst_date_activate());

        deviceLicenseRepository.save(deviceLicense);
        licenseRepository.save(license);


        licenseHistoryService.recordLicenseChange(license, user,"Активировано", "Лицензия успешно активирована");
        Ticket ticket = new Ticket();
        ticket = ticket.generateTicket(license, device, user.getId());

        return ticket;
    }
    public License getActiveLicensesForDevice(Device device, User user){
        DeviceLicense deviceLicense = deviceLicenseRepository.findByDevice_id(device.getId())
                .orElseThrow(() -> new DeviceNotFoundException("Для данного Устройства не найдена лицензия"));
        return licenseRepository.findById(deviceLicense.getDevice().getId())
                .orElseThrow(() -> new LicenseNotFoundException("Лицензия не найдена"));
    }

    private void verifyLicenseOwnership(License license, Long newUserId) {

        if (license.getUser() != null) {
            Long currentUserId = license.getUser().getId();

            if (!currentUserId.equals(newUserId)) {
                throw new ActivationException("Лицензия принадлежит другому пользователю.");
            }
        }
    }

    private String generateActivationCode() {
        return UUID.randomUUID().toString();
    }

}
