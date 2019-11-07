package ru.ageevvictor.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ageevvictor.inventory.model.hardware.Computer;
import ru.ageevvictor.inventory.model.hardware.HardDrive;
import ru.ageevvictor.inventory.repository.ComputerRepository;
import ru.ageevvictor.inventory.repository.HardDiskRepository;

import java.util.List;

@Service
public class ComputerService {

    private ComputerRepository compRepo;
    private HardDiskRepository hddRepo;

    @Autowired
    public void setCompRepo(ComputerRepository compRepo) {
        this.compRepo = compRepo;
    }

    @Autowired
    public void setHddRepo(HardDiskRepository hddRepo) {
        this.hddRepo = hddRepo;
    }

    public List<Computer> listAll() {
        return compRepo.findAll();
    }

    public void updateOrSave(Computer computer) {
        Computer existComputer = compRepo.getComputerByName(computer.getName());
        if (existComputer != null) {
            compRepo.delete(existComputer);
        }
        compRepo.save(computer);
    }

    public Computer getComputerByName(String name) {
        return compRepo.getComputerByName(name);
    }

    public Computer getComputerByIpAddress(String ip) {
        return compRepo.getComputerByIpAddress(ip);
    }

    public Computer get(long id) {
        return compRepo.findById(id).get();
    }


    public List<HardDrive> getListHardDrivesByComputer(Long id) {
        return hddRepo.findByComputer_Id(id);
    }


    public List<Computer> findBySubnetID(Long id) {
        return compRepo.findBySubnetId(id);
    }

    public boolean existsByName(String name) {
        return compRepo.existsByName(name);
    }

}
