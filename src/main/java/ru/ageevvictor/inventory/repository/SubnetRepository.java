package ru.ageevvictor.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ageevvictor.inventory.model.hardware.Subnet;

public interface SubnetRepository extends JpaRepository<Subnet, Long> {

    Subnet findByName(String name);


}
