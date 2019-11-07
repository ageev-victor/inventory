package ru.ageevvictor.inventory.model.hardware;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.ageevvictor.inventory.model.service.CheckSubnet;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
@Entity(name = "subnet")
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Subnet implements Comparable<Subnet> {

    @Transient
    private ApplicationContext applicationContext;
    @Transient
    private ExecutorService executorService;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int fromAddress;
    private int toAddress;
    private String fullAddress;
    private String serverName;
    private int countComputers;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastScanDate;
    private boolean isOnline;
    private int checkPercent;
    private int percentPerPC;
    private int maxPercent;
    private long uptime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subnet")
    private List<Computer> computersList;

    public List<String> getReachableDevices() {
        CheckSubnet checkSubnet;
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        for (int i = getFromAddress(); i <= getToAddress(); i++) {
            checkSubnet = applicationContext.getBean(CheckSubnet.class);
            checkSubnet.setSubnet(this);
            checkSubnet.setIndex(i);
            executorService.submit(checkSubnet);
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CheckSubnet.reachebleDevices;
    }

    @Override
    public int compareTo(Subnet subnet) {
        return this.getName().compareTo(subnet.getName());
    }
}
