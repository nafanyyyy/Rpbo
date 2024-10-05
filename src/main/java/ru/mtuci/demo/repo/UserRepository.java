package ru.mtuci.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.mtuci.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
