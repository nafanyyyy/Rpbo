package ru.mtuci.demo.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mtuci.demo.model.DeviceLicense;

import java.util.Optional;

@Repository
public interface DeviceLicenseRepository extends JpaRepository<DeviceLicense, Long> {
    Optional<DeviceLicense> findById(Long id);
    Optional<DeviceLicense> findByDevice_id(Long device_id);
}
