package ru.mtuci.demo.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.mtuci.demo.model.LicenseHistory;

import java.util.Optional;

@Repository
public interface LicenseHistoryRepository extends JpaRepository<LicenseHistory, Long> {
    Optional<LicenseHistory> findById(Long id);
}