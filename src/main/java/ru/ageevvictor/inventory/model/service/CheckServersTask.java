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

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CheckServersTask extends TimerTask {


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

    @Override
    public void run() {
        List<Subnet> listSubnets = subnetService.listAll();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (Subnet subnet : listSubnets) {
            CollectUptime collectUptime = applicationContext.getBean(CollectUptime.class);
            collectUptime.setSubnet(subnet);
            executorService.submit(collectUptime);
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
