package ru.ageevvictor.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ageevvictor.inventory.model.hardware.Computer;
import java.util.List;

public interface ComputerRepository extends JpaRepository<Computer, Long> {

    boolean existsByName(String name);

    Computer getComputerByName(String name);

    Computer getComputerByIpAddress(String id);

    List<Computer> findBySubnetId(Long id);

}
