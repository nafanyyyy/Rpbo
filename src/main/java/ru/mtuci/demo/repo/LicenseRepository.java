package ru.mtuci.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mtuci.demo.model.License;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {
}
