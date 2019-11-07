package ru.ageevvictor.inventory.model.hardware;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.ageevvictor.inventory.model.service.ComputerDataParser;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "computers")
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Computer implements Comparable<Computer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "computer")
    private List<Monitor> monitors;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "computer")
    private List<HardDrive> hardDrives;

    @Column(name = "ip_address")
    private String ipAddress;

    private String macAddress;

    @Column(name = "system_name")
    private String systemName;

    @Column(name = "total_memory")
    private int totalMemory;

    @Column(name = "serial_number")
    private String serialNumber;

    private boolean isOnline;

    private boolean isScaned;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String model;

    @Transient
    private ComputerDataParser computerDataParser;

    @ManyToOne
    @JoinColumn
    private Subnet subnet;

    @Autowired
    public void setComputerDataParser(ComputerDataParser computerDataParser) {
        this.computerDataParser = computerDataParser;
    }

    @Override
    public int compareTo(Computer computer) {
        return this.getName().compareTo(computer.getName());
    }
}