package es.us.isa.jsonmutator.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Sergio Segura
 */
public class AuthPropertyManager {

    static String propertyFilePath = "src/test/resources/auth.properties";
    static 	Properties properties = null;

    static public String readProperty(String name) {

        if (properties==null) {
            properties = new Properties();
            try {
                properties.load(new FileInputStream(propertyFilePath));
            } catch (IOException e) {
                System.err.println("Error reading property file: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return properties.getProperty(name);

    }

}