package ru.ageevvictor.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ageevvictor.inventory.model.hardware.HardDrive;
import java.util.List;

public interface HardDiskRepository extends JpaRepository<HardDrive, Long> {

    List<HardDrive> findByComputer_Id(Long id);
}
