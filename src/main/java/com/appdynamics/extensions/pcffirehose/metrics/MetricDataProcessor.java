/*
 * Copyright 2018. AppDynamics LLC and its affiliates. All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates. The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.pcffirehose.metrics;

import com.appdynamics.extensions.metrics.Metric;
import com.appdynamics.extensions.pcffirehose.consumer.ingress.LoggregatorMetric;
import com.appdynamics.extensions.pcffirehose.input.MetricConfig;
import com.appdynamics.extensions.pcffirehose.input.Stat;
import com.appdynamics.extensions.pcffirehose.util.LoggregatorMetricType;
import com.appdynamics.extensions.util.StringUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.appdynamics.extensions.pcffirehose.util.Constants.AVERAGE;
import static com.appdynamics.extensions.pcffirehose.util.Constants.INDIVIDUAL;
import static com.appdynamics.extensions.pcffirehose.util.Constants.OBSERVATION;

/**
 * Created by aditya.jagtiani on 4/13/18.
 */
public class MetricDataProcessor {

    private static Logger logger = LoggerFactory.getLogger(MetricDataProcessor.class);
    private Stat.Stats metricConfiguration;
    private String metricPrefix;
    private String serverName;
    private Map<String, Stat> statsMap = Maps.newHashMap();

    public MetricDataProcessor(Stat.Stats metricConfiguration, String metricPrefix, String serverName) {
        this.metricConfiguration = metricConfiguration;
        statsMap = this.metricConfiguration.getStats();
        this.metricPrefix = metricPrefix;
        this.serverName = serverName;
    }

    public Metric extractMetric(LoggregatorMetric loggregatorMetric) {
        Metric metric = null;
        if (statsMap.containsKey(loggregatorMetric.getOrigin())) {
            Stat currentStat = statsMap.get(loggregatorMetric.getOrigin());
            Map<String, MetricConfig> metricConfigMap = statsMap.get(loggregatorMetric.getOrigin()).getMetricConfig();
            String metricName = loggregatorMetric.getName();
            if (metricConfigMap.containsKey(loggregatorMetric.getName())) {
                logger.debug("Metric {} found, processing details", metricName);
                MetricConfig currentMetricCfg = metricConfigMap.get(loggregatorMetric.getName());
                if (loggregatorMetric.getValue() != null) {
                    String metricValue = String.valueOf(loggregatorMetric.getValue());
                    Map<String, String> propertiesMap = new ObjectMapper().convertValue(currentMetricCfg, Map.class);
                    setMetricQualifiers(loggregatorMetric, propertiesMap);
                    String baseMetricPrefix = metricPrefix + serverName + "|" + currentStat.getAlias();
                    metricName =  baseMetricPrefix + "|" + formMetricPath(loggregatorMetric) + "|"
                            + currentMetricCfg.getAttr();
                    logger.debug("Currently publishing metric {} with a reported value of {}", metricName, metricValue);
                    metric = new Metric(currentMetricCfg.getAttr(), metricValue, metricName, propertiesMap);
                }
            }
        }
        return metric;
    }

    private String formMetricPath(LoggregatorMetric loggregatorMetric) {
        if(Strings.isNullOrEmpty(loggregatorMetric.getIndex()) && !Strings.isNullOrEmpty(loggregatorMetric.getIP())) {
            return loggregatorMetric.getOrigin() + "|" +
                    loggregatorMetric.getDeployment() + "|" + loggregatorMetric.getJob() + "|" + loggregatorMetric.getIP();
        }

        else if(!Strings.isNullOrEmpty(loggregatorMetric.getIndex()) && Strings.isNullOrEmpty(loggregatorMetric.getIP())) {
            return loggregatorMetric.getOrigin() + "|" +
                    loggregatorMetric.getDeployment() + "|" + loggregatorMetric.getJob() + "|" + loggregatorMetric.getIndex();
        }
        else if(Strings.isNullOrEmpty(loggregatorMetric.getIndex()) && Strings.isNullOrEmpty(loggregatorMetric.getIP())) {
            return loggregatorMetric.getOrigin() + "|" +
                    loggregatorMetric.getDeployment() + "|" + loggregatorMetric.getJob();
        }
        return loggregatorMetric.getOrigin() + "|" +
                loggregatorMetric.getDeployment() + "|" + loggregatorMetric.getJob() + "|" +
                loggregatorMetric.getIndex() + "|" + loggregatorMetric.getIP();
    }

    private void setMetricQualifiers(LoggregatorMetric metric, Map<String, String> metricProps) {
        if (metric.getLoggregatorMetricType().equals(LoggregatorMetricType.GAUGE)) {
            metricProps.put("aggregationType", AVERAGE);
            metricProps.put("timeRollUpType", AVERAGE);
            metricProps.put("clusterRollUpType", INDIVIDUAL);
        } else {
            metricProps.put("aggregationType", OBSERVATION);
            metricProps.put("timeRollUpType", OBSERVATION);
            metricProps.put("clusterRollUpType", INDIVIDUAL);
        }
    }
}
