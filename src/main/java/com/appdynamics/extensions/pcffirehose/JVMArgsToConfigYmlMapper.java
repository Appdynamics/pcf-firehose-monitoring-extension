package com.appdynamics.extensions.pcffirehose;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.appdynamics.extensions.pcffirehose.util.PCFFirehoseUtils.*;

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
        if(!Strings.isNullOrEmpty(getMetricPrefixFromJVMArgs())) {
            logger.info("Overriding metric prefix using JVM arguments");
            configYml.put("metricPrefix", getMetricPrefixFromJVMArgs());
        }
        if(!Strings.isNullOrEmpty(getNumberOfThreadsFromJVMArgs())) {
            logger.info("Overriding number of threads using JVM arguments");
            configYml.put("numberOfThreads", getNumberOfThreadsFromJVMArgs());
        }
        if(!Strings.isNullOrEmpty(getEncryptionKeyFromJVMArgs())) {
            logger.info("Overriding encryption key using JVM arguments");
            configYml.put("encryptionKey", getEncryptionKeyFromJVMArgs());
        }
        if(areServerJVMArgsValid()) {
            logger.info("Overriding server using JVM arguments");
            configYml.put("server", getServerFromJVMArgs());
        }
        return configYml;
    }
}
