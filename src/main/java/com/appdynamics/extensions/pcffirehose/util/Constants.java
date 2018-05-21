/*
 * Copyright 2018. AppDynamics LLC and its affiliates. All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates. The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.pcffirehose.util;

/**
 * Created by aditya.jagtiani on 4/6/18.
 */
public class Constants {
    public static final String SERVER_HOST_SYSTEM_PROP = "APPD_NOZZLE_SERVER_HOST";
    public static final String SERVER_PORT_SYSTEM_PROP = "APPD_NOZZLE_SERVER_PORT";
    public static final String NUM_OF_THREADS_SYSTEM_PROP = "APPD_NOZZLE_NO_OF_THREADS";
    public static final String CERT_FILE_SYSTEM_PROP = "APPD_NOZZLE_CERT_FILE";
    public static final String CA_CERT_FILE_SYSTEM_PROP = "APPD_NOZZLE_CA_CERT_FILE";
    public static final String PRIVATE_KEY_SYSTEM_PROP = "APPD_NOZZLE_PRIVATE_KEY";
    public static final String AUTHORITY_SYSTEM_PROP = "APPD_NOZZLE_AUTHORITY";
    public static final String TIER_ID_SYSTEM_PROP = "APPD_NOZZLE_TIER_ID";
    public static final String CERTIFICATE_HEADER = "-----BEGIN CERTIFICATE-----";
    public static final String CERTIFICATE_FOOTER = "-----END CERTIFICATE-----";
    public static final String PRIVATE_KEY_HEADER = "-----BEGIN PRIVATE KEY-----";
    public static final String PRIVATE_KEY_FOOTER = "-----END PRIVATE KEY-----";

    public static final String DEFAULT_METRIC_PREFIX = "Custom Metrics|PCF Firehose Monitor|";
    public static String METRIC_PREFIX = "Server|Component:{}|Custom Metrics|PCF Firehose Monitor|";
    public static final String MONITOR_NAME = "PCF Firehose Monitor";
    public static final String AVERAGE = "AVERAGE";
    public static final String SUM = "SUM";
    public static final String OBSERVATION = "OBSERVATION";
    public static final String CURRENT = "CURRENT";
    public static final String INDIVIDUAL = "INDIVIDUAL";
    public static final String COLLECTIVE = "COLLECTIVE";
}
