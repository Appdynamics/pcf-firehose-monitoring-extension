/*
 * Copyright 2018. AppDynamics LLC and its affiliates. All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates. The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.pcffirehose;

import com.appdynamics.extensions.ABaseMonitor;
import com.appdynamics.extensions.TasksExecutionServiceProvider;
import com.appdynamics.extensions.conf.MonitorContextConfiguration;
import com.appdynamics.extensions.pcffirehose.input.Stat;
import com.appdynamics.extensions.util.AssertUtils;
import com.google.common.collect.Maps;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.appdynamics.extensions.pcffirehose.util.Constants.DEFAULT_METRIC_PREFIX;
import static com.appdynamics.extensions.pcffirehose.util.Constants.MONITOR_NAME;
import static com.appdynamics.extensions.pcffirehose.util.PCFFirehoseUtils.initializeConfigFromEnvironmentVariables;

/**
 * Created by aditya.jagtiani on 3/20/18.
 */
public class PCFFirehoseMonitor extends ABaseMonitor {
    private static Logger logger = LoggerFactory.getLogger(PCFFirehoseMonitor.class);
    private MonitorContextConfiguration monitorContextConfiguration;
    private Map<String, ?> configYml = Maps.newHashMap();

    @Override
    public String getDefaultMetricPrefix() {
        return DEFAULT_METRIC_PREFIX;
    }

    @Override
    public String getMonitorName() {
        return MONITOR_NAME;
    }

    @Override
    public int getTaskCount() {
        return 1;
    }

    @Override
    public void doRun(TasksExecutionServiceProvider taskExecutor) {
        Map<String, String> server = (Map<String, String>) configYml.get("server");
        AssertUtils.assertNotNull(server, "Server not initialized.");
        AssertUtils.assertNotNull(this.monitorContextConfiguration.getMetricsXml(), "The metrics.xml has been " +
                "not been created.");
        logger.debug("Starting the PCF Firehose Monitoring Task for server : " + server.get("name"));
        PCFFirehoseMonitorTask task = new PCFFirehoseMonitorTask(monitorContextConfiguration,
                taskExecutor.getMetricWriteHelper(), server);
        taskExecutor.submit(server.get("name"), task);

        //As this is a continuous extension, make this thread wait indefinitely.
        CountDownLatch infiniteWait = new CountDownLatch(1);
        try {
            infiniteWait.await();   //Will make this thread wait till the CountDownLatch reaches 0.
        } catch (InterruptedException e) {
            logger.error("Failed to wait indefinitely ", e);
        }
    }

    @Override
    protected void initializeMoreStuff(Map<String, String> args) {
        monitorContextConfiguration = getContextConfiguration();
        configYml = initializeConfigFromEnvironmentVariables((Map<String, ? super Object>) monitorContextConfiguration.getConfigYml());
        monitorContextConfiguration.setMetricXml(args.get("metric-file"), Stat.Stats.class);
    }

    public static void main(String[] args) throws TaskExecutionException {
       ConsoleAppender ca = new ConsoleAppender();
        ca.setWriter(new OutputStreamWriter(System.out));
        ca.setLayout(new PatternLayout("%-5p [%t]: %m%n"));
        ca.setThreshold(Level
                .DEBUG);
        org.apache.log4j.Logger.getRootLogger().addAppender(ca);

        PCFFirehoseMonitor monitor = new PCFFirehoseMonitor();


        Map<String, String> taskArgs = new HashMap<String, String>();
        taskArgs.put("config-file", "/Users/aditya.jagtiani/repos/appdynamics/extensions/pcf-firehose-monitoring-extension/src/main/resources/conf/config.yml");
        taskArgs.put("metric-file", "/Users/aditya.jagtiani/repos/appdynamics/extensions/pcf-firehose-monitoring-extension/src/main/resources/conf/metrics.xml");
        monitor.execute(taskArgs, null);
    }

}
