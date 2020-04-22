package com.qmetry.qaf.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class FileManager {

    /**
     * Get the values of the properties from the properties
     *
     * @param key, Key of the property to get value for
     * @return propertyValue
     */
    public static String getPropertyValue(String filePath, String key) {

        String propertyValue = "message not found - ";
        try (InputStream input = new FileInputStream(filePath)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            propertyValue = prop.getProperty(key);
        }
        //In case the message isn't retrieved, the test case flow can continue,
        //as this is just for console logging, and the exceptions will be logged in the console.
        catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return propertyValue;
    }

    /**
     * To convert JSON string into List
     *
     * @param payload, Request Body for POST Method
     */
    public Map<Object, Object> stringToMap(String payload) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new TreeMap<Object, Object>(mapper.readValue(payload, Map.class));
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
