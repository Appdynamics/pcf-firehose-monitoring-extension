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

import java.util.List;
import java.util.Map;

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
        this.config = config;
        this.metricWriteHelper = metricWriteHelper;
        this.server = server;
        this.monitorContextConfiguration = monitorContextConfiguration;
        this.metricConfiguration = (Stat.Stats) monitorContextConfiguration.getMetricsXml();
    }

    public void run() {
        try {
            populateAndPrintStats();
        }
        catch(Exception e) {
            logger.error("PCF Firehose Monitoring Task failed for server " + e);
        }
    }

    private void populateAndPrintStats() throws Exception {
        LoggregatorConsumer consumer = new LoggregatorConsumer(server.get("host"), 9000,
                "/Users/aditya.jagtiani/repos/appdynamics/extensions/pcf-firehose-monitoring-extension/src/test/resources/cert.pem",
                "/Users/aditya.jagtiani/repos/appdynamics/extensions/pcf-firehose-monitoring-extension/src/test/resources/privateKey-Apr30-PKCS8.key",
                "/Users/aditya.jagtiani/repos/appdynamics/extensions/pcf-firehose-monitoring-extension/src/test/resources/ca.pem",
                "reverselogproxy");
        while(true) {
            try {
                LoggregatorMetric loggregatorMetric = consumer.getLoggregatorMetric();
                Metric metric = new MetricDataProcessor(metricConfiguration,
                        monitorContextConfiguration.getMetricPrefix(), server.get("name")).extractMetric(loggregatorMetric);
                if(metric != null) {
                    metricsToBePublished.add(metric);
                    metricWriteHelper.transformAndPrintMetrics(metricsToBePublished);
                    metricsToBePublished.clear();
                }
            }
            catch(Exception e) {
                logger.error("Error encountered while processing metrics", e);
                consumer.resetEnvelopes();
            }
        }
        //List<LoggregatorMetric> metrics = consumer.getLoggregatorMetricsList();
    }

    public void onTaskComplete() {}
}

// io.grpc.StatusRuntimeException: UNAVAILABLE: Channel closed while performing protocol negotiation (443)
