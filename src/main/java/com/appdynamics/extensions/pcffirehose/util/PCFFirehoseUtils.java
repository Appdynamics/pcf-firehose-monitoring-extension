package com.appdynamics.extensions.pcffirehose.util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import static com.appdynamics.extensions.pcffirehose.util.Constants.*;

/**
 * Created by aditya.jagtiani on 4/9/18.
 */
public class PCFFirehoseUtils {

    public static boolean areServerJVMArgsValid() {
        return Strings.isNullOrEmpty(System.getProperty(INSTANCE_NAME_ENV))
                && Strings.isNullOrEmpty(System.getProperty(USERNAME_ENV))
                && (Strings.isNullOrEmpty(System.getProperty(PASSWORD_ENV)) || Strings.isNullOrEmpty(System.getProperty(ENCRYPTED_PASSWORD_ENV)))
                && Strings.isNullOrEmpty(System.getProperty(HOST_ENV))
                && Strings.isNullOrEmpty(System.getProperty(PORT_ENV));
    }

    public static String getMetricPrefixFromJVMArgs() {
        return System.getProperty(METRIC_PREFIX_ENV);
    }

    public static String getNumberOfThreadsFromJVMArgs() {
        return System.getProperty(NUMBER_OF_THREADS_ENV);
    }

    public static String getEncryptionKeyFromJVMArgs() {
        return System.getProperty(ENCRYPTION_KEY_ENV);
    }

    public static Map<String, String> getServerFromJVMArgs() {
        Map<String, String> server = Maps.newHashMap();
        server.put("name", System.getProperty(INSTANCE_NAME_ENV));
        server.put("host", System.getProperty(HOST_ENV));
        server.put("port", System.getProperty(PORT_ENV));
        server.put("username", System.getProperty(USERNAME_ENV));
        server.put("password", System.getProperty(PASSWORD_ENV));
        server.put("skipSslValidation", System.getProperty(SKIP_SSL_VALIDATION_ENV));
        return server;
    }

    public static String readFile(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
