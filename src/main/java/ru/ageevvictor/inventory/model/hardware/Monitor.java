package ru.ageevvictor.inventory.model.hardware;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity(name = "monitors")
@Setter
@Getter
@Component
@NoArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Monitor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // autoincrement
    private Long id;
    private String model;
    private String serial;

    @ManyToOne
    @JoinColumn
    private Computer computer;
}
