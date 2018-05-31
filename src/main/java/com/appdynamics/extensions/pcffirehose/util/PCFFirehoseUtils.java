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

    public static String getNumberOfThreads() {
        return System.getenv(NUM_OF_THREADS_SYSTEM_PROP);
    }

    public static String getAuthority() {
        return System.getenv(AUTHORITY_SYSTEM_PROP);
    }

    public static String getComponentId() {
        return System.getenv(TIER_ID_SYSTEM_PROP);
    }

    public static Map<String, String> getServer() {
        Map<String, String> server = Maps.newHashMap();
        server.put("host", System.getenv(SERVER_HOST_SYSTEM_PROP));
        server.put("port", System.getenv(SERVER_PORT_SYSTEM_PROP));
        return server;
    }

    public static Map<String, ?> initializeConfigFromEnvironmentVariables(Map<String, ? super Object> config) {
        String numberOfThreads = getNumberOfThreads();
        logger.info("Initializing number of threads from environment variables: {}", numberOfThreads);
        config.put("numberOfThreads", Integer.parseInt(numberOfThreads));

        Map<String, String> server = getServer();
        logger.info("Initializing server from environment variables. Server name = {}, host = {}, port = {}",
                server.get("name"), server.get("host"), server.get("port"));
        config.put("server", server);
        return config;
    }

    public static String getMetricPrefix() {
        return METRIC_PREFIX.replace("{}", getComponentId());
    }

}
