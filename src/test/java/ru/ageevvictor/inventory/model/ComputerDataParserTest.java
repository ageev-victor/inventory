package ru.ageevvictor.inventory.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ageevvictor.inventory.model.service.BatRunner;


@RunWith(SpringRunner.class)
@SpringBootTest

public class ComputerDataParserTest {

    private String computerIP = "confidential";

    @Autowired
    private BatRunner batRunner;


    @Test
    public void getPCName() {
        String result;
        String command = "confidential";
        result = batRunner.getBatResult(command);
        if (result.contains("confidential")) {
            result = result.replaceAll("confidential", "");
        } else result = "its not a computer";

        Assert.assertEquals("confidential" , result);

    }

    @Test
    public void isPC() {
        boolean result = false;
        String serverName = "confidential";
        String command = "confidential";
        String text = batRunner.getBatResult(command);
        if (text.contains("confidential") & !text.contains(serverName)) {
            result = true;
        }
        Assert.assertTrue(result);
    }

    @Test
    public void getTotalMemory() {
        int totalMemory;
        String command = "confidential";
        String result = batRunner.getBatResult(command);
        result = result.replaceAll("confidential", "");
        result = result.trim();
        totalMemory = (int) (Double.parseDouble(result) / 1024 / 1024);
        Assert.assertEquals(0, totalMemory);
    }

    @Test
    public void getMacAddress() {
        String command = "confidential";
        String result = batRunner.getBatResult(command);
        result = result.replaceAll("confidential", "");
        result = result.substring(0, 0);
        Assert.assertEquals("confidential", result);
    }

    @Test
    public void getPCSerialNumber() {
        String command = "confidential";
        String result = batRunner.getBatResult(command);
        result = result.replaceAll("confidential", "");
        Assert.assertEquals("confidential", result);
    }

    @Test
    public void getPCModel() {
        String command = "confidential";
        String result = batRunner.getBatResult(command);
        result = result.substring(0);
        result = result.trim();
        Assert.assertEquals("confidential", result);
    }
}