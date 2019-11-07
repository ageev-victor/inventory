package ru.ageevvictor.inventory.model.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BatRunner {

    public String getBatResult(String batCommand) {
        String result = null;
        try {
            Process process = Runtime.getRuntime().exec(batCommand);
            InputStream inputStream = process.getInputStream();
            byte[] buffer = new byte[32768];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (true) {
                int readBytesCount = inputStream.read(buffer);
                if (readBytesCount == -1) {
                    break;
                }
                if (readBytesCount > 0) {
                    baos.write(buffer, 0, readBytesCount);
                }
            }
            baos.flush();
            baos.close();
            byte[] data = baos.toByteArray();
            result = new String(data, Charset.forName("CP866"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    String getBatResultFromArray(String[] command) {
        StringBuilder result = new StringBuilder();
        Process tempResult = null;
        try {
            tempResult = Runtime.getRuntime().exec(command);
            Reader reader = new InputStreamReader(tempResult.getInputStream(), "Cp866");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            try {
                tempResult.waitFor();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            bufferedReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        assert tempResult != null;
        tempResult.destroy();
        return result.toString();
    }
}
