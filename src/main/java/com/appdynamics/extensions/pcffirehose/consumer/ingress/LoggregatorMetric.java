/*
 * Copyright 2018. AppDynamics LLC and its affiliates. All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates. The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.pcffirehose.consumer.ingress;

import com.appdynamics.extensions.pcffirehose.util.LoggregatorMetricType;
import com.google.common.annotations.VisibleForTesting;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by aditya.jagtiani on 4/12/18.
 */
public class LoggregatorMetric {
    private String name = "";
    private Double value = 0d;
    private long timestamp;
    private String deployment;
    private String job;
    private String index;
    private String IP;
    private String origin;
    private Map<String, String> tags;
    private LoggregatorMetricType metricType;

    public LoggregatorMetric(String name, Double value, long timestamp, Map<String, String> tags,
                             LoggregatorMetricType metricType) {
        this.tags = new HashMap<String, String>(tags);
        this.tags = this.tags.entrySet()
                .stream()
                .filter(entry -> !entry.getKey().startsWith("__v1"))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        this.value = value;
        this.timestamp = timestamp;
        this.deployment = this.tags.remove("deployment");
        this.job = this.tags.remove("job");
        this.index = this.tags.remove("index");
        this.IP = this.tags.remove("ip");
        this.origin = this.tags.remove("origin");
        this.tags.remove("id");
        this.tags.remove("name");
        this.tags.remove("role");
        this.metricType = metricType;
        this.name = generateName(name);
    }


    private String generateName(String name) {
        if (origin != null) {
            name = origin + "." + name;
        }
        if (!this.tags.isEmpty()) {
            TreeSet<String> keys = new TreeSet<String>(this.tags.keySet());
            name += "[" +
                    keys
                            .stream()
                            .map(key -> String.format("%s=%s", key, this.tags.get(key)))
                            .collect(Collectors.joining(",")) +
                    "]";
        }
        return name;
    }

    public String getOrigin() {
        return origin;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getDeployment() {
        return deployment;
    }

    public String getJob() {
        return job;
    }

    public String getIndex() {
        return index;
    }

    public String getIP() {
        return IP;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public LoggregatorMetricType getLoggregatorMetricType() {
        return metricType;
    }
}
