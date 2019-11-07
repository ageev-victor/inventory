package ru.ageevvictor.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ageevvictor.inventory.model.hardware.Monitor;
import ru.ageevvictor.inventory.repository.MonitorRepository;
import java.util.List;

@Service
public class MonitorService {


    private MonitorRepository monRepo;

    @Autowired
    public void setMonRepo(MonitorRepository monRepo) {
        this.monRepo = monRepo;
    }

    public List<Monitor> listAll() {
        return monRepo.findAll();
    }

    public List<Monitor> getListMonitorsByComputer(Long id) {
        return monRepo.findByComputer_Id(id);
    }


    public void save(Monitor monitor) {
        Monitor existMonitor = monRepo.findBySerial(monitor.getSerial());
        if (existMonitor != null) monitor.setId(existMonitor.getId());
        monRepo.save(monitor);
    }

    public Monitor get(long id) {
        return monRepo.findById(id).get();
    }

    public void delete(long id) {
        getListMonitorsByComputer(id).forEach(monitor -> monRepo.deleteById(monitor.getId()));
    }
}
