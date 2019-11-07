package ru.ageevvictor.inventory.model.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.ageevvictor.inventory.model.hardware.Computer;
import ru.ageevvictor.inventory.model.hardware.HardDrive;
import ru.ageevvictor.inventory.model.hardware.Monitor;
import ru.ageevvictor.inventory.model.hardware.Subnet;

import javax.persistence.Transient;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ComputerDataParser {

    @Transient
    private ApplicationContext applicationContext;
    private BatRunner batRunner;
    private String computerIP;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setBatRunner(BatRunner batRunner) {
        this.batRunner = batRunner;
    }

    private HashMap<String, String> getEdidMonitors(String text) {
        HashMap<String, String> monitors = new HashMap<>();
        Pattern pattern = Pattern.compile("confidential");
        Matcher matcher = pattern.matcher(text);
        /*while (matcher.find()) {
            String serialNum = text.substring(
                    confidential
                    );
            String modelNum;
            if (serialNum.equals("confidential")) {
                modelNum = text.substring(confidential);
            } else modelNum = text.substring(confidential);

            monitors.put(modelNum, serialNum);
        }*/
        return monitors;
    }


    private String hexToString(String hex) {
        String result;
        if (hex.equals("confidential")) {
            result = "Отсутствуют данные";
        } else {
            result = new String(new BigInteger(hex, 16).toByteArray());
        }
        return result;
    }

    String getPCName() {
        String result;
        String command = "confidential";
        result = batRunner.getBatResult(command);
        if (result.contains("confidential")) {
            result = result.replaceAll("confidential", "");
        } else result = "its not a computer";
        return result;
    }

    boolean isPC(String computerIP, String serverName) {
        boolean result = false;
        String command = "confidential";
        String text = batRunner.getBatResult(command);
        //confidential
        return result;
    }


    String getSystemName() {
        String result;
        String command = "confidential";
        result = batRunner.getBatResult(command);
        result = result.replaceAll("confidential", "");
        result = result.trim();
        return result;
    }


    private String getMonitorDataFromReg() {
        String command = "confidential";
        return batRunner.getBatResult(command);
    }


    ArrayList<Monitor> getParsedMonitors() {
        ArrayList<Monitor> monitors = new ArrayList<>();
        String text = getMonitorDataFromReg();
        for (Map.Entry<String, String> entry : getEdidMonitors(text).entrySet()) {
            String model = hexToString(entry.getKey());
            String serial = hexToString(entry.getValue());
            Monitor monitor = applicationContext.getBean(Monitor.class);
            monitor.setModel(model);
            monitor.setSerial(serial);
            monitors.add(monitor);
        }
        return monitors;
    }


    int getTotalMemory() {
        String result;
        int totalMemory;
        String command = "confidential";
        result = batRunner.getBatResult(command);
        result = result.replaceAll("confidential", "");
        result = result.trim();
        totalMemory = (int) (Double.parseDouble(result) / 1024 / 1024);
        return totalMemory;
    }

    String getMacAddress(Computer computer) {
        String result;
        String command = "confidential";
        result = batRunner.getBatResult(command);
        result = result.replaceAll("confidential", "");
        result = result.substring(0, 0);
        return result;
    }

    String getPCSerialNumber() {
        String result;
        String command = "confidential";
        result = batRunner.getBatResult(command);
        result = result.replaceAll("confidential", "");
        return result;
    }

    String getPCModel() {
        String result;
        String command = "confidential";
        result = batRunner.getBatResult(command);
        result = result.substring(0);
        result = result.trim();
        return result;
    }

    public String getWhoLoggedOn(String pcName) {
        String userLogin;
        String userFio;
        String[] command = new String[]{
                //confidential
        };
        userLogin = batRunner.getBatResultFromArray(command);
        userLogin = userLogin.substring(userLogin.indexOf('\\') + 1);
        command = new String[]{
                //confidential
        };
        userFio = batRunner.getBatResultFromArray(command);
        userFio = userFio.replaceAll("confidential", "");
        userFio = userFio.trim();
        return userFio;
    }

    public List<String> getListPrinters(String pcName) {
        List<String> listPrinters;
        String result;
        String command = "confidential";
        result = batRunner.getBatResult(command);
        listPrinters = Arrays.asList(result.split("Name="));
        return listPrinters;
    }

    Long getServerUptime(Subnet subnet) {
        String result;
        String[] command = new String[]{
                //confidential
        };
        result = batRunner.getBatResultFromArray(command);
        result = result.substring(0);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        Date fromDate = null;
        try {
            fromDate = dateFormat.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert fromDate != null;
        return getDateDiff(fromDate, new Date());
    }

    private long getDateDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public void addPrinter(Computer computer, String printer, String server) {
        String[] command = new String[]{
                // confidential
        };
        batRunner.getBatResultFromArray(command);
    }

    public void deletePrinter(Computer computer, String printer) {
        String command = "confidential";

        batRunner.getBatResult(command);
    }

    public void clearCacheJava(Computer computer) {
        String command = "confidential";
        batRunner.getBatResult(command);
    }

    List<HardDrive> getListHdd(Computer computer) {
        HardDrive hardDrive = applicationContext.getBean(HardDrive.class);
        String command = "confidential";
        String result = batRunner.getBatResult(command);
        result = result.replaceAll("Model|SerialNumber|Size|Status", "");
        String[] mass = result.split("\\s+");
        List<HardDrive> listHdd = new ArrayList<>();
        hardDrive.setStatus(mass[mass.length - 1]);
        hardDrive.setSerialNumber(mass[mass.length - 3]);
        StringBuilder modelResult = new StringBuilder();
        for (int i = 0; i < mass.length - 3; i++) {
            modelResult.append(mass[i]);
        }
        hardDrive.setModel(String.valueOf(modelResult));
        hardDrive.setSize(Long.parseLong(mass[mass.length - 2]) / 1024 / 1024 / 1024);
        hardDrive.setComputer(computer);
        listHdd.add(hardDrive);
        return listHdd;
    }


    public void powerOnPc(String server, String macAddress) {
        String parsedMac = macAddress.replaceAll(":", "");
        String[] command = new String[]{
                // confidential
        };
        batRunner.getBatResultFromArray(command);
    }

    public void powerOffPc(Computer computer) {
        String[] command = new String[]{
                // confidential
        };
        batRunner.getBatResultFromArray(command);
    }


}


