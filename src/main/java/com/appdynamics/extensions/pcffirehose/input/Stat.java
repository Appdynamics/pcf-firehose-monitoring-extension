/*
 * Copyright 2018. AppDynamics LLC and its affiliates. All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates. The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.pcffirehose.input;

import com.google.common.collect.Maps;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aditya.jagtiani on 4/9/18.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Stat {

    @XmlAttribute
    private String origin;
    @XmlAttribute
    private String alias;
    @XmlElement(name = "metric")
    private ArrayList<MetricConfig> metricConfigs;
    @XmlElement(name = "stat")
    public List<Stat> stats;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Map<String, MetricConfig> getMetricConfig() {
        Map<String, MetricConfig> metricConfigMap = Maps.newHashMap();
        for (MetricConfig metricConfig : metricConfigs) {
            metricConfigMap.put(metricConfig.getAttr(), metricConfig);
        }
        return metricConfigMap;
    }

    public void setMetricConfig(ArrayList<MetricConfig> metricConfigs) {
        this.metricConfigs = metricConfigs;
    }

    public List<Stat> getStats() {
        return stats;
    }

    public void setStats(List<Stat> stats) {
        this.stats = stats;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Stats {
        @XmlElement(name = "stat")
        private Stat[] stats;

        public void setStats(Stat[] stats) {
            this.stats = stats;
        }

        public Map<String, Stat> getStats() {
            Map<String, Stat> statsMap = Maps.newHashMap();
            for (Stat stat : stats) {
                statsMap.put(stat.getOrigin(), stat);
            }
            return statsMap;
        }
    }
}

