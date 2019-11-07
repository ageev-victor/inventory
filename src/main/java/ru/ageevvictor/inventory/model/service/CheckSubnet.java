package ru.ageevvictor.inventory.model.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.ageevvictor.inventory.model.hardware.Computer;
import ru.ageevvictor.inventory.model.hardware.Subnet;
import ru.ageevvictor.inventory.service.ComputerService;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CheckSubnet implements Runnable {

    private int index;
    private Subnet subnet;
    public static List<String> reachebleDevices = new ArrayList<>();

    @Override
    public void run() {
        try {
            String host = subnet.getFullAddress() + "." + index;
            if (Inet4Address.getByName(host).isReachable(1000)) {
                reachebleDevices.add(host);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshStatus(List<Computer> computerList, ComputerService computerService) {
        computerList.forEach(computer -> {
            try {
                if (Inet4Address.getByName(computer.getName()).isReachable((1000))) {
                    computer.setOnline(true);
                } else computer.setOnline(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        computerList.forEach(computerService::updateOrSave);
    }
}


