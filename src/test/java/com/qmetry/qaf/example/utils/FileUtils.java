package com.qmetry.qaf.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.stream.IntStream;


/**
 * This file utility class contains the methods to extract information from different files
 *
 * @author Maria.Saif
 */
@UtilityClass
@Slf4j
public class FileUtils {


    /**
     * get file from classpath, resources folder
     * @param fileName Name of the file
     */
    public synchronized File getFileFromResources(String fileName) {

        ClassLoader classLoader = FileUtils.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }
    }

    /**
     * Return contents of json file
     *
     * @param file The file to be read
     * @return String
     */
    public synchronized String returnFile(File file) {

        StringBuilder contentBuilder = new StringBuilder();
        String line;
        if (file == null) return "File not found";

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return contentBuilder.toString();
    }
}
