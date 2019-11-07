package ru.ageevvictor.inventory.model.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.ageevvictor.inventory.model.hardware.Subnet;
import ru.ageevvictor.inventory.service.SubnetService;

@Getter
@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CollectUptime implements Runnable {

    private SubnetService subnetService;
    private ApplicationContext applicationContext;

    @Autowired
    public void setSubnetService(SubnetService subnetService) {
        this.subnetService = subnetService;
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private Subnet subnet;

    @Override
    public void run() {
        ComputerDataParser computerDataParser = applicationContext.getBean(ComputerDataParser.class);
        Long uptime = computerDataParser.getServerUptime(subnet);
        if (uptime > 0) {
            subnet.setOnline(true);
        } else subnet.setOnline(false);
        subnet.setUptime(uptime);
        subnetService.save(subnet);
    }
}


