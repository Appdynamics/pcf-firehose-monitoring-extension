package com.appdynamics.extensions.pcffirehose.metrics;

import com.appdynamics.extensions.AMonitorJob;
import com.appdynamics.extensions.conf.MonitorContextConfiguration;
import com.appdynamics.extensions.metrics.Metric;
import com.appdynamics.extensions.pcffirehose.consumer.ingress.LoggregatorMetric;
import com.appdynamics.extensions.pcffirehose.input.Stat;
import com.appdynamics.extensions.pcffirehose.util.LoggregatorMetricType;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.Map;

/**
 * Created by aditya.jagtiani on 5/2/18.
 */
public class MetricDataProcessorTest {

    private MetricDataProcessor metricDataProcessor;
    private Map<String, Stat> statsMap;
    private Stat.Stats metricConfiguration;
    private String serverName;
    private String metricPrefix;
    private MonitorContextConfiguration monitorContextConfiguration = new MonitorContextConfiguration("PCF Firehose",
            "Custom Metrics|PCF Firehose|", Mockito.mock(File.class), Mockito.mock(AMonitorJob.class));

    @Before
    public void initialize() {
        monitorContextConfiguration.setMetricXml("/Users/aditya.jagtiani/repos/appdynamics/extensions/pcf-firehose-monitoring-extension/src/test/resources/test-metrics.xml", Stat.Stats.class);
        metricConfiguration = (Stat.Stats) monitorContextConfiguration.getMetricsXml();
        statsMap = metricConfiguration.getStats();
        metricPrefix = "Custom Metrics|PCF Firehose|";
        serverName = "Server 1";
        metricDataProcessor = new MetricDataProcessor(metricConfiguration, metricPrefix, serverName);
    }

    @Test
    public void extractMetricTest() {
        LoggregatorMetric loggregatorMetric = createLoggregatorMetric();
        Metric metric = metricDataProcessor.extractMetric(loggregatorMetric);
        Assert.assertTrue(metric.getMetricName().equals("Test Loggregator Metric"));
        Assert.assertTrue(metric.getMetricValue().equals(String.valueOf(loggregatorMetric.getValue())));
        Assert.assertTrue(metric.getMetricPath().equals(this.metricPrefix + this.serverName +
                "|" + loggregatorMetric.getOrigin() + "|" + loggregatorMetric.getDeployment() +
                "|" + loggregatorMetric.getJob() + "|" + this.statsMap.get(loggregatorMetric.getOrigin()).getAlias() + "|"
                + statsMap.get(loggregatorMetric.getOrigin()).getMetricConfig()
                .get(loggregatorMetric.getName()).getAlias()));
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
