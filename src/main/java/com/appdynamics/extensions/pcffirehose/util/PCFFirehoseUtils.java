/*
 * Copyright 2018. AppDynamics LLC and its affiliates. All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates. The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.pcffirehose.util;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import static com.appdynamics.extensions.pcffirehose.util.Constants.*;

/**
 * Created by aditya.jagtiani on 4/9/18.
 */
public class PCFFirehoseUtils {
    private static Logger logger = LoggerFactory.getLogger(PCFFirehoseUtils.class);

    public static String getMetricPrefix() {
        return System.getenv(METRIC_PREFIX_SYSTEM_PROP);
    }

    public static String getNumberOfThreads() {
        return System.getenv(NUM_OF_THREADS_SYSTEM_PROP);
    }

    public static String getCertificate() {
        return System.getenv(CERT_FILE_SYSTEM_PROP);
    }

    public static String getCACertificate() {
        return System.getenv(CA_CERT_FILE_SYSTEM_PROP);
    }

    public static String getPrivateKey() {
        return System.getenv(PRIVATE_KEY_SYSTEM_PROP);
    }

    public static String getAuthority() {
        return System.getenv(AUTHORITY_SYSTEM_PROP);
    }

    public static Map<String, String> getServer() {
        Map<String, String> server = Maps.newHashMap();
        server.put("name", System.getenv(SERVER_NAME_SYSTEM_PROP));
        server.put("host", System.getenv(SERVER_HOST_SYSTEM_PROP));
        server.put("port", System.getenv(SERVER_PORT_SYSTEM_PROP));
        return server;
    }

    public static Map<String, ?> initializeConfigFromEnvironmentVariables(Map<String, ? super Object> config) {
        String metricPrefix = getMetricPrefix();
        logger.info("Initializing metric prefix from environment variables: {}", metricPrefix);
        config.put("metricPrefix", metricPrefix);

        String numberOfThreads = getNumberOfThreads();
        logger.info("Initializing number of threads from environment variables: {}", numberOfThreads);
        config.put("numberOfThreads", Integer.parseInt(numberOfThreads));

        Map<String, String> server = getServer();
        logger.info("Initializing server from environment variables. Server name = {}, host = {}, port = {}",
                server.get("name"), server.get("host"), server.get("port"));
        config.put("server", server);
        return config;
    }

    public static String writeCertFile(String content, String fileName) throws Exception {
        File file = new File(fileName);
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
        return file.getAbsolutePath();
    }

    //the environment expects .pem files to have exactly 64 characters on each line. This method ensures that
    public static String processCertFile(String cert, String start_header, String end_header) {
        try {
            int start_header_len = start_header.length();
            int start_header_index = cert.indexOf(start_header);

            int end_header_index = cert.indexOf(end_header);

            String certificate = cert.substring(start_header_len, end_header_index);
            String[] certSubstrings = certificate.split("(?<=\\G.{64})");
            String temp =  start_header + '\n' + String.join("\n", certSubstrings) + '\n' + end_header;
            return temp;
        }
        catch (Exception e) {
            logger.error("Error while processing certificates: ", e);
        }
        return "";
    }
}
