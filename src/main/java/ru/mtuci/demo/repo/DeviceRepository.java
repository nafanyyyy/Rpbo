package ru.mtuci.demo.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mtuci.demo.model.Device;
import ru.mtuci.demo.model.License;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    Optional<Device> findByMac(String mac);
}
