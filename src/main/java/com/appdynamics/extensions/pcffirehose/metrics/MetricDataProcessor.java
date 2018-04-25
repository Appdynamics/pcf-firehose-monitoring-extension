package com.appdynamics.extensions.pcffirehose.metrics;

import com.appdynamics.extensions.metrics.Metric;
import com.appdynamics.extensions.pcffirehose.consumer.ingress.LoggregatorMetric;
import com.appdynamics.extensions.pcffirehose.input.Stat;

import java.util.List;

/**
 * Created by aditya.jagtiani on 4/13/18.
 */
public class MetricDataProcessor {
    public MetricDataProcessor(LoggregatorMetric loggregatorMetric, Stat.Stats metricConfiguration) {
        this.loggregatorMetric = loggregatorMetric;
        this.metricConfiguration = metricConfiguration;
    }

    private LoggregatorMetric loggregatorMetric;
    private Stat.Stats metricConfiguration;

    public Metric extractMetric() {
        return null;
        // todo metric manipulation
        // todo heartbeat metric
        // todo apply props based on gauge and counter types

    }
}
