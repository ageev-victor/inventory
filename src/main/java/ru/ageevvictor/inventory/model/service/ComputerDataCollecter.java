package ru.ageevvictor.inventory.model.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.ageevvictor.inventory.model.hardware.Computer;
import ru.ageevvictor.inventory.model.hardware.Monitor;
import ru.ageevvictor.inventory.model.hardware.Subnet;
import ru.ageevvictor.inventory.service.ComputerService;
import ru.ageevvictor.inventory.service.SubnetService;

import java.util.Date;

@Setter
@Getter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ComputerDataCollecter implements Runnable {

    private ApplicationContext applicationContext;
    private SubnetService subnetService;
    private ComputerService computerService;
    private String computerIp;
    private Subnet subnet;
    private ComputerDataParser computerDataParser;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setSubnetService(SubnetService subnetService) {
        this.subnetService = subnetService;
    }

    @Autowired
    public void setComputerService(ComputerService computerService) {
        this.computerService = computerService;
    }

    @Autowired
    public void setComputerDataParser(ComputerDataParser computerDataParser) {
        this.computerDataParser = computerDataParser;
    }

    @Override
    public void run() {
        subnet.setCheckPercent(subnet.getCheckPercent() + subnet.getPercentPerPC());
        boolean isNewComputer = false;
        long dayInMilisecond = 86400000;
        computerDataParser.setComputerIP(computerIp);
        if (!computerDataParser.isPC(computerIp, subnet.getServerName())) {
            Thread.currentThread().interrupt();
        }
        Computer computer = computerService.getComputerByIpAddress(computerIp);
        Date today = new Date();
        if (computer == null) {
            computer = applicationContext.getBean(Computer.class);
            computer.setDate(today);
            isNewComputer = true;
        }
        computer.setComputerDataParser(computerDataParser);
        if (today.getTime() - computer.getDate().getTime() > dayInMilisecond | isNewComputer) {
            computer.setScaned(false);
        } else {
            computer.setScaned(true);
        }
        if (!computer.isScaned()) {
            computer.setIpAddress(computerIp);
            computer.setName(computerDataParser.getPCName());
            computer.setHardDrives(computerDataParser.getListHdd(computer));
            computer.setMonitors(computerDataParser.getParsedMonitors());
            for (Monitor monitor : computer.getMonitors()) {
                monitor.setComputer(computer);
            }
            computer.setMacAddress(computerDataParser.getMacAddress(computer));
            computer.setModel(computerDataParser.getPCModel());
            computer.setSystemName(computerDataParser.getSystemName());
            computer.setTotalMemory(computerDataParser.getTotalMemory());
            computer.setSerialNumber(computerDataParser.getPCSerialNumber());
            computer.setSubnet(subnet);
            computer.setDate(today);
            computer.setOnline(true);
        }
        if (subnet.getLastScanDate().getTime() < today.getTime()) subnet.setLastScanDate(today);
        if (!computerService.existsByName(computer.getName())) subnet.setCountComputers(subnet.getCountComputers() + 1);
        computerService.updateOrSave(computer);
        subnetService.save(subnet);
    }
}


