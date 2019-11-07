package ru.ageevvictor.inventory.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.ageevvictor.inventory.model.hardware.Subnet;
import ru.ageevvictor.inventory.model.service.BatRunner;
import ru.ageevvictor.inventory.model.service.ComputerDataCollecter;
import ru.ageevvictor.inventory.service.SubnetService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
@Component
public class StartDataInitializer {

    private ApplicationContext applicationContext;
    private SubnetService subnetService;
    private BatRunner batRunner;
    protected List<Subnet> subnetList;

    @Autowired
    public void setBatRunner(BatRunner batRunner) {
        this.batRunner = batRunner;
    }

    @Autowired
    public void setSubnetService(SubnetService subnetService) {
        this.subnetService = subnetService;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void inventoryOneSubnet(Long subnetId) {
        collectDataFromSubnet(subnetService.get(subnetId));
    }

    public void inventoryAllSubnets() {
        subnetList = subnetService.listAll();
        Collections.sort(subnetList);
        subnetList.forEach(this::collectDataFromSubnet);
    }

    private void collectDataFromSubnet(Subnet subnet) {
        subnet.setApplicationContext(applicationContext);
        subnet.setExecutorService(Executors.newFixedThreadPool(7));
        List<String> devicesList = subnet.getReachableDevices();
        subnet.setPercentPerPC(100 / devicesList.size());
        subnet.setMaxPercent(devicesList.size() * subnet.getPercentPerPC());
        subnet.setCheckPercent(0);
        for (String s : devicesList) {
            ComputerDataCollecter computerDataCollecter = applicationContext.getBean(ComputerDataCollecter.class);
            computerDataCollecter.setSubnet(subnet);
            computerDataCollecter.setComputerIp(s);
            subnet.getExecutorService().submit(computerDataCollecter);
        }
        subnet.getExecutorService().shutdown();
        try {
            devicesList.clear();
            subnet.getExecutorService().awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
