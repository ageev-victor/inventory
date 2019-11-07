package ru.ageevvictor.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ageevvictor.inventory.model.hardware.Subnet;
import ru.ageevvictor.inventory.repository.SubnetRepository;

import java.util.List;

@Service
public class SubnetService {

    private SubnetRepository subnetRepo;

    @Autowired
    public void setSubnetRepo(SubnetRepository subnetRepo) {
        this.subnetRepo = subnetRepo;
    }

    public List<Subnet> listAll() {
        return subnetRepo.findAll();
    }

    public void save(Subnet subnet) {
        Subnet existSubnet = subnetRepo.findByName(subnet.getName());
        if (existSubnet != null) subnet.setId(existSubnet.getId());
        subnetRepo.save(subnet);
    }

    public Subnet get(long id) {
        return subnetRepo.findById(id).get();
    }

    public void delete(long id) {
        subnetRepo.deleteById(id);
    }


}

