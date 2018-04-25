package com.appdynamics.extensions.pcffirehose;

import com.appdynamics.extensions.pcffirehose.util.PCFFirehoseUtils;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import static com.appdynamics.extensions.pcffirehose.util.Constants.*;

/**
 * Created by aditya.jagtiani on 4/6/18.
 */

public class JVMArgsToConfigYmlMapper {
    private static Logger logger = LoggerFactory.getLogger(JVMArgsToConfigYmlMapper.class);
    private Map<String, ? super Object> configYml;

    public JVMArgsToConfigYmlMapper(Map<String, ? super Object> defaultConfigYml) {
        this.configYml = defaultConfigYml;
    }

    public Map<String, ?> generateConfigYmlFromJVMArgs() {
        if(PCFFirehoseUtils.areServerJVMArgsValid()) {
            logger.info("JVM arguments found for servers. Overriding the default configuration");
            configYml.put("servers", getServersFromJVMArgs());
        }
        else {
            logger.info("No JVM arguments present for servers. Using the default configuration.");
        }
        if(PCFFirehoseUtils.isEncryptionKeyJVMArgValid()) {
            logger.info("JVM arguments found for encryptionKey. Overriding the default configuration");
            configYml.put("encryptionKey", getEncryptionKeyFromJVMArgs());
        }
        else {
            logger.info("No JVM arguments present for encryptionKey. Using the default configuration.");
        }

        if(PCFFirehoseUtils.isMetricPrefixJVMArgValid()) {
            logger.info("JVM arguments found for metricPrefix. Overriding the default configuration");
            configYml.put("metricPrefix", getMetricPrefixFromJVMArgs());
        }
        else {
            logger.info("No JVM arguments present for metricPrefix. Using the default configuration.");
        }

        if(PCFFirehoseUtils.isNumOfThreadsJVMArgValid()) {
            logger.info("JVM arguments found for numberOfThreads. Overriding the default configuration");
            configYml.put("numberOfThreads", getNumOfThreadsFromJVMArgs());
        }
        else {
            logger.info("No JVM arguments present for numberOfThreads. Using the default configuration.");
        }
        return configYml;
    }

    private Map<String, String> getServersFromJVMArgs() {
        Map<String, String> server = Maps.newHashMap();
        server.put("displayName", System.getProperty(PCF_INSTANCE_NAME_ENV));
        server.put("host", System.getProperty(PCF_HOST_ENV));
        server.put("port", System.getProperty(PCF_PORT_ENV));
        server.put("username", System.getProperty(PCF_USERNAME_ENV));
        server.put("password", System.getProperty(PCF_PASSWORD_ENV));
        server.put("encryptedPassword", System.getProperty(PCF_ENCRYPTED_PASSWORD_ENV));
        server.put("skipSslValidation", System.getProperty(PCF_SKIP_SSL_VALIDATION_ENV));
        return server;
    }

    private String getEncryptionKeyFromJVMArgs() {
        return System.getProperty(PCF_ENCRYPTION_KEY_ENV);
    }

    private String getMetricPrefixFromJVMArgs() {
        return System.getProperty(METRIC_PREFIX_ENV);
    }

    private String getNumOfThreadsFromJVMArgs() {
        return System.getProperty(NUMBER_OF_THREADS_ENV);
    }
}
