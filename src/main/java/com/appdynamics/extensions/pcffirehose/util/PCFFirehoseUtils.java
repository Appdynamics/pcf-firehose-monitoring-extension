package com.appdynamics.extensions.pcffirehose.util;

import com.google.common.base.Strings;

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

    public static boolean isMetricPrefixJVMArgValid() {
        return Strings.isNullOrEmpty(System.getProperty(METRIC_PREFIX_ENV));
    }

    public static boolean isNumOfThreadsJVMArgValid() {
        return Strings.isNullOrEmpty(System.getProperty(NUMBER_OF_THREADS_ENV));
    }

    public static boolean isEncryptionKeyJVMArgValid() {
        return Strings.isNullOrEmpty(System.getProperty(ENCRYPTION_KEY_ENV));
    }
}
