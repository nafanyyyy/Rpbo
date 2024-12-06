package ru.mtuci.demo.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.demo.model.License;
import ru.mtuci.demo.repo.LicenseRepository;
import ru.mtuci.demo.services.*;

import java.time.LocalDate;
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
        return licenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("License not found"));
    }

    @Override
    public License getByKey(String key) {
        return licenseRepository.findByKey(key)
                .orElseThrow(() -> new RuntimeException("License not found"));
    }

    @Override
    public License createLicense(Long productId, Long ownerId, Long licenseTypeId, License licenseParameters) {
        // Проверка существования продукта
        var product = productService.getProductById(productId);
        if (product == null) {
            throw new RuntimeException("Продукт не найден");
        }

        // Проверка существования пользователя
        var user = userService.getById(ownerId);
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }

        // Проверка существования типа лицензии
        var licenseType = licenseTypeService.getLicenseTypeById(licenseTypeId);
        if (licenseType == null) {
            throw new RuntimeException("Тип лицензии не найден");
        }

        // Создание новой лицензии
        License license = new License();
        license.setProduct(product);
        license.setOwner(user);
        license.setLicenseType(licenseType);
        license.setKey(generateActivationCode());
        license.setDescription(licenseParameters.getDescription());

        // Расчет даты окончания лицензии
        license.setEnding_date(calculateExpirationDate(licenseType.getDefaultDuration()));

        // Сохранение лицензии в базу данных
        licenseRepository.save(license);

        // Запись в историю лицензий
        licenseHistoryService.recordLicenseChange(license, user, "Создана", "Лицензия успешно создана");

        return license;
    }

    private String generateActivationCode() {
        return UUID.randomUUID().toString(); // Генерация уникального кода активации
    }

    private LocalDate calculateExpirationDate(int durationDays) {
        return LocalDate.now().plusDays(durationDays); // Расчет даты окончания
    }
}
