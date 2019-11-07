package ru.ageevvictor.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ageevvictor.inventory.model.hardware.Monitor;
import java.util.List;

public interface MonitorRepository extends JpaRepository<Monitor, Long> {

    List<Monitor> findByComputer_Id(Long id);

    Monitor findBySerial(String name);
}
