package ru.mtuci.demo.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.demo.Request.ActivationRequest;
import ru.mtuci.demo.Request.CreateLicensesRequest;
import ru.mtuci.demo.Request.RenewalRequest;
import ru.mtuci.demo.Response.LicenseResponse;
import ru.mtuci.demo.exception.ActivationException;
import ru.mtuci.demo.exception.DeviceNotFoundException;
import ru.mtuci.demo.exception.LicenseNotFoundException;
import ru.mtuci.demo.exception.LicenseRenewalException;
import ru.mtuci.demo.model.*;
import ru.mtuci.demo.repo.DeviceLicenseRepository;
import ru.mtuci.demo.repo.LicenseRepository;
import ru.mtuci.demo.services.*;
import ru.mtuci.demo.ticket.Ticket;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    public List<LicenseResponse> getAll() {
        return licenseRepository.findAll().stream()
                .map(license -> new LicenseResponse(
                        license.getLicense_id(),
                        license.getKey(),
                        license.getLicenseType() != null ? license.getLicenseType().getId() : null,
                        license.getBlocked(),
                        license.getDevice_count(),
                        license.getOwner() != null ? license.getOwner().getId() : null,
                        license.getDuration(),
                        license.getDescription(),
                        license.getProduct() != null ? license.getProduct().getId() : null
                ))
                .toList();
    }
    @Override
    public LicenseResponse getById(Long id) {
        License license = licenseRepository.findById(id)
                .orElseThrow(() -> new LicenseNotFoundException("Лицензия с ID " + id + " не найдена"));

        return new LicenseResponse(
                license.getLicense_id(), license.getKey(),
                license.getLicenseType().getId(), license.getBlocked(),
                license.getDevice_count(), license.getOwner().getId(),
                license.getDuration(), license.getDescription(),
                license.getProduct().getId()
        );
    }

    @Override
    public LicenseResponse getByKey(String key) {
        License license = licenseRepository.findByKey(key)
                .orElseThrow(() -> new LicenseNotFoundException("Лицензия с ключом " + key + " не найдена"));

        return new LicenseResponse(
                license.getLicense_id(), license.getKey(),
                license.getLicenseType().getId(), license.getBlocked(),
                license.getDevice_count(), license.getOwner().getId(),
                license.getDuration(), license.getDescription(),
                license.getProduct().getId()
        );
    }

    @Override
    public Ticket renewLicense(RenewalRequest request, User user) {
        License license = licenseRepository.findByKey(request.getActivationCode())
                .orElseThrow(() -> new LicenseNotFoundException("Лицензия не найдена"));

        if (license.getUser() == null || !license.getUser().getId().equals(user.getId())) {
            throw new LicenseRenewalException("Лицензия была активирована другим пользователем, продление невозможно");
        }

        Device device = deviceService.getDeviceByDeviceInfo(request.getDeviceInfo());

        List<DeviceLicense> deviceLicenses = deviceLicenseRepository.findAllByDeviceId(device.getId());
        boolean isDeviceLinked = false;
        for (DeviceLicense deviceLicense : deviceLicenses) {
            if (deviceLicense.getLicense().getLicense_id().equals(license.getLicense_id())) {
                isDeviceLinked = true;
                break;
            }
        }

        if (!isDeviceLinked) {
            throw new LicenseRenewalException("Указанное устройство не связано с этой лицензией");
        }

        if (license.getBlocked()) {
            throw new ActivationException("Активация невозможна, лицензия заблокирована");
        }

        if (license.getEnding_date().isBefore(LocalDate.now())) {
            throw new LicenseRenewalException("Лицензия истекла");
        }

        LocalDate newEndingDate = license.getEnding_date().plusDays(license.getDuration());
        license.setEnding_date(newEndingDate);
        licenseRepository.save(license);

        licenseHistoryService.recordLicenseChange(license, user, "Продлена", "Лицензия успешно продлена");

        Ticket ticket = new Ticket();
        ticket = ticket.generateTicket(license, device, user.getId());

        return ticket;
    }

    @Override
    public LicenseResponse createLicense(CreateLicensesRequest request) {
        var product = productService.getProductById(request.getProductId());
        if (product == null) {
            throw new RuntimeException("Продукт не найден");
        }

        if (product.getIsBlocked() != null && product.getIsBlocked()) {
            throw new RuntimeException("Продукт заблокирован");
        }

        var user = userService.getById(request.getOwnerId());
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }

        var licenseType = licenseTypeService.getLicenseTypeById(request.getLicenseTypeId());
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
        return new LicenseResponse(
                license.getLicense_id(), license.getKey(),
                licenseType.getId(), license.getBlocked(),
                license.getDevice_count(), user.getId(),
                license.getDuration(), license.getDescription(),
                product.getId()
        );
    }

    @Override
    public Ticket activateLicense(ActivationRequest request, User user) {
        License license = licenseRepository.findByKey(request.getActivationCode())
                .orElseThrow(() -> new LicenseNotFoundException("Лицензия не найдена"));

        verifyLicenseOwnership(license, user.getId());

        if (license.getBlocked()) {
            throw new ActivationException("Активация невозможна");
        }
        if (license.getEnding_date() != null && license.getEnding_date().isBefore(LocalDate.now())) {
            throw new ActivationException("Срок действия лицензии истёк.");
        }
        if (license.getLicenseType().getActivationLimit() != null &&
                license.getDevice_count() >= license.getLicenseType().getActivationLimit()) {
            throw new ActivationException("Достигнут предел активаций для этой лицензии.");
        }
        Device device = deviceService.registerOrUpdateDevice(request.getDeviceInfo(), user, request.getDeviceName());

        List<DeviceLicense> existingDeviceLicenses = deviceLicenseRepository.findAllByDeviceId(device.getId());
        for (DeviceLicense existingDeviceLicense : existingDeviceLicenses) {
            License linkedLicense = existingDeviceLicense.getLicense();
            if (linkedLicense.getLicenseType().getId().equals(license.getLicenseType().getId()) &&
                    linkedLicense.getProduct().getId().equals(license.getProduct().getId())) {
                if (!linkedLicense.getBlocked()) {
                    throw new ActivationException("Устройство уже связано с лицензией с таким же продуктом и типом лицензии.");
                }
            }
        }

        if (license.getFirst_date_activate() == null) {
            license.setFirst_date_activate(LocalDate.now());
            license.setEnding_date(license.getFirst_date_activate().plusDays(license.getDuration()));
        }

        license.setUser(user);
        DeviceLicense deviceLicense = new DeviceLicense(license, device, license.getFirst_date_activate());
        license.setDevice_count(license.getDevice_count() + 1);

        deviceLicenseRepository.save(deviceLicense);
        licenseRepository.save(license);
        licenseHistoryService.recordLicenseChange(license, user, "Активировано", "Лицензия успешно активирована");
        Ticket ticket = new Ticket();
        ticket = ticket.generateTicket(license, device, user.getId());

        return ticket;
    }

    public List<License> getActiveLicensesForDevice(Device device, User user) {
        List<DeviceLicense> deviceLicenses = deviceLicenseRepository.findAllByDeviceId(device.getId());

        if (deviceLicenses.isEmpty()) {
            throw new DeviceNotFoundException("Для данного устройства не найдены лицензии.");
        }

        List<License> activeLicenses = new ArrayList<>();
        for (DeviceLicense deviceLicense : deviceLicenses) {
            License license = deviceLicense.getLicense();

            if (license.getUser() != null && license.getUser().getId().equals(user.getId()) && !license.getBlocked()) {
                activeLicenses.add(license);
            }
        }

        if (activeLicenses.isEmpty()) {
            throw new LicenseNotFoundException("Активные лицензии для данного устройства не найдены для данного пользователя.");
        }

        return activeLicenses;
    }
    private void verifyLicenseOwnership(License license, Long newUserId) {
        if (license.getOwner() == null || !license.getOwner().getId().equals(newUserId)) {
            throw new ActivationException("Лицензия принадлежит другому пользователю.");
        }

        if (license.getUser() != null) {
            Long currentUserId = license.getUser().getId();
            if (!currentUserId.equals(newUserId)) {
                throw new ActivationException("Лицензия уже активирована другим пользователем.");
            }
        }
    }
    @Override
    public void blockLicense(Long id) {
        License license = licenseRepository.findById(id)
                .orElseThrow(() -> new LicenseNotFoundException("Лицензия с ID " + id + " не найдена"));
        license.setBlocked(true);
        licenseRepository.save(license);
    }
    @Override
    public void unblockLicense(Long id) {
        License license = licenseRepository.findById(id)
                .orElseThrow(() -> new LicenseNotFoundException("Лицензия с ID " + id + " не найдена"));
        license.setBlocked(false);
        licenseRepository.save(license);
    }
    private String generateActivationCode() {
        return UUID.randomUUID().toString();
    }
}
