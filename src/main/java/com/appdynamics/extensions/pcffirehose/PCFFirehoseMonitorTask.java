/*
 * Copyright 2018. AppDynamics LLC and its affiliates. All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates. The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.pcffirehose;

import com.appdynamics.extensions.AMonitorTaskRunnable;
import com.appdynamics.extensions.MetricWriteHelper;
import com.appdynamics.extensions.conf.MonitorContextConfiguration;
import com.appdynamics.extensions.metrics.Metric;
import com.appdynamics.extensions.pcffirehose.consumer.ingress.LoggregatorConsumer;
import com.appdynamics.extensions.pcffirehose.consumer.ingress.LoggregatorMetric;
import com.appdynamics.extensions.pcffirehose.input.Stat;
import com.appdynamics.extensions.pcffirehose.metrics.MetricDataProcessor;
import com.appdynamics.extensions.pcffirehose.util.PCFFirehoseUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.appdynamics.extensions.pcffirehose.util.PCFFirehoseUtils.*;

/**
 * Created by aditya.jagtiani on 3/20/18.
 */
public class PCFFirehoseMonitorTask implements AMonitorTaskRunnable {
    private static Logger logger = LoggerFactory.getLogger(PCFFirehoseMonitorTask.class);
    private Map<String, ?> config;
    private MetricWriteHelper metricWriteHelper;
    private Map<String, String> server;
    private MonitorContextConfiguration monitorContextConfiguration;
    private List<Metric> metricsToBePublished = Lists.newArrayList();
    private Stat.Stats metricConfiguration;

    public PCFFirehoseMonitorTask(Map<String, ?> config, MonitorContextConfiguration monitorContextConfiguration,
                                  MetricWriteHelper metricWriteHelper, Map<String, String> server) {
        this.metricWriteHelper = metricWriteHelper;
        this.server = server;
        this.monitorContextConfiguration = monitorContextConfiguration;
        this.metricConfiguration = (Stat.Stats) monitorContextConfiguration.getMetricsXml();
    }

    public void run() {
        try {
            populateAndPrintStats();
        } catch (Exception e) {
            logger.error("PCF Firehose Monitoring Task failed for server " + e);
        }
    }

    private void populateAndPrintStats() throws Exception {
        LoggregatorConsumer consumer = new LoggregatorConsumer(server.get("host"), Integer.parseInt(server.get("port")),
                getCertificate(), getPrivateKey(), getCACertificate(), getAuthority());

        while (true) {
            try {
                LoggregatorMetric loggregatorMetric = consumer.getLoggregatorMetric();
                Metric metric = new MetricDataProcessor(metricConfiguration,
                        monitorContextConfiguration.getMetricPrefix(), server.get("name")).extractMetric(loggregatorMetric);
                if (metric != null) {
                    metricsToBePublished.add(metric);
                    metricWriteHelper.transformAndPrintMetrics(metricsToBePublished);
                    metricsToBePublished.clear();
                }
            } catch (Exception e) {
                logger.error("Error encountered while processing metrics", e);
                consumer.resetEnvelopes();
            }
        }
    }

    public void onTaskComplete() {
    }
}
