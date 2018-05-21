/*
 * Copyright 2018. AppDynamics LLC and its affiliates. All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates. The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.pcffirehose.metrics;

import com.appdynamics.extensions.AMonitorJob;
import com.appdynamics.extensions.conf.MonitorContextConfiguration;
import com.appdynamics.extensions.metrics.Metric;
import com.appdynamics.extensions.pcffirehose.consumer.ingress.LoggregatorMetric;
import com.appdynamics.extensions.pcffirehose.input.Stat;
import com.appdynamics.extensions.pcffirehose.util.LoggregatorMetricType;
import com.appdynamics.extensions.pcffirehose.util.PCFFirehoseUtils;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.Map;

/**
 * Created by aditya.jagtiani on 5/2/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PCFFirehoseUtils.class, MetricDataProcessor.class})
public class MetricDataProcessorTest {

    private MetricDataProcessor metricDataProcessor;
    private Map<String, Stat> statsMap;
    private String serverName;
    private String metricPrefix;
    private MonitorContextConfiguration monitorContextConfiguration = new MonitorContextConfiguration("PCF Firehose",
            "Custom Metrics|PCF Firehose|", Mockito.mock(File.class), Mockito.mock(AMonitorJob.class));

    @Before
    public void initialize() {
        monitorContextConfiguration.setMetricXml("/Users/aditya.jagtiani/repos/appdynamics/extensions/" +
                "pcf-firehose-monitoring-extension/src/test/resources/test-metrics.xml", Stat.Stats.class);
        Stat.Stats metricConfiguration = (Stat.Stats) monitorContextConfiguration.getMetricsXml();
        statsMap = metricConfiguration.getStats();
        metricPrefix = "Custom Metrics|PCF Firehose|";
        serverName = "Server 1";
        metricDataProcessor = new MetricDataProcessor(metricConfiguration);
        PowerMockito.mockStatic(PCFFirehoseUtils.class);
        PowerMockito.when(PCFFirehoseUtils.getMetricPrefix()).thenReturn(metricPrefix);
    }

    @Test
    public void extractMetricTest() {
        LoggregatorMetric loggregatorMetric = createLoggregatorMetric();
        Metric metric = metricDataProcessor.extractMetric(loggregatorMetric);
        Assert.assertTrue(metric.getMetricName().equals(loggregatorMetric.getName()));
        Assert.assertTrue(metric.getMetricValue().equals(String.valueOf(loggregatorMetric.getValue())));
        Assert.assertTrue(metric.getMetricPath().equals(this.metricPrefix +
                this.statsMap.get(loggregatorMetric.getOrigin()).getAlias() + "|" + loggregatorMetric.getOrigin()
                + "|" + loggregatorMetric.getDeployment() + "|" + loggregatorMetric.getJob() + "|"
                + loggregatorMetric.getName()));
        Assert.assertTrue(metric.getAggregationType().equals("AVERAGE"));
        Assert.assertTrue(metric.getTimeRollUpType().equals("AVERAGE"));
        Assert.assertTrue(metric.getClusterRollUpType().equals("INDIVIDUAL"));
    }


    private LoggregatorMetric createLoggregatorMetric() {
        Map<String, String> tags = Maps.newHashMap();
        tags.put("origin", "auctioneer");
        tags.put("deployment", "cf");
        tags.put("job", "control");
        return new LoggregatorMetric("TestLoggregatorMetric", 10.0,
                100, tags, LoggregatorMetricType.GAUGE);
    }
}
