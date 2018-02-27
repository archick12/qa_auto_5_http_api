package utils.framework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyReader {

    // TUTORIAL - http://www.avajava.com/tutorials/lessons/how-do-i-read-a-properties-file.html
    public static Map<String, String> readProperties(String propertyFile) {
        Properties propertyFileValues = new Properties();
        FileInputStream input = null;
        HashMap<String, String> result;

        // TODO ------ 1) opening the file for write access -----------
        try {
            try {
                input = new FileInputStream(propertyFile);
                try {
                    propertyFileValues.load(input);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // TODO ------ 1) end

            // TODO ------ 2) read actual values from the file -----------
            result = new HashMap();
            result.put("test_run_id", propertyFileValues.getProperty("test_run_id"));
            result.put("path_t", propertyFileValues.getProperty("path_t"));
            result.put("user_trail", propertyFileValues.getProperty("user_trail"));
            result.put("password_trail", propertyFileValues.getProperty("password_trail"));
            result.put("username", propertyFileValues.getProperty("username"));
            result.put("password", propertyFileValues.getProperty("password"));
            // TODO ------ 2) end

        } finally {
            // TODO ------ 3) closing connection/stream to the file
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // TODO ------ 3) end
        }

        // TODO ------ 4) returning result
        return result;
        // TODO ------ 4) end
    }


    public static void main(String[] args) {
        Map<String, String> collectionOfKeyValueParameters = readProperties("jira.properties");
        String userName = collectionOfKeyValueParameters.get("username");
        System.out.println("USER NAME: " + userName);

    }
}