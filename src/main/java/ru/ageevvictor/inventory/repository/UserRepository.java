package ru.ageevvictor.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ageevvictor.inventory.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
}
