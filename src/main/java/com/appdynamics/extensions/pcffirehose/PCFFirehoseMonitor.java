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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.appdynamics.extensions.pcffirehose.util.Constants.DEFAULT_METRIC_PREFIX;
import static com.appdynamics.extensions.pcffirehose.util.Constants.MONITOR_NAME;

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
        List<Map<String, String>> servers = (List<Map<String, String>>) configYml.get("servers");
        AssertUtils.assertNotNull(servers, "The 'servers' section in config.yml is not initialised");
        return servers.size();
    }

    @Override
    public void doRun(TasksExecutionServiceProvider taskExecutor) {
        List<Map<String, String>> servers = (List<Map<String, String>>) configYml.get("servers");
        AssertUtils.assertNotNull(servers, "The 'servers' section in config.yml is not initialised");
        AssertUtils.assertNotNull(this.monitorContextConfiguration.getMetricsXml(), "The metrics.xml has been " +
                "not been created.");
        for (Map<String, String> server : servers) {
            logger.debug("Starting the PCF Firehose Monitoring Task for server : " + server.get("name"));
            PCFFirehoseMonitorTask task = new PCFFirehoseMonitorTask(configYml, monitorContextConfiguration,
                    taskExecutor.getMetricWriteHelper(), server);
            taskExecutor.submit(server.get("name"), task);
        }
        //As this is continuous extension, make this thread wait indefinitely.
        CountDownLatch infiniteWait = new CountDownLatch(1);
        try {
            infiniteWait.await();   //Will make this thread to wait till the CountDownLatch reaches to 0.
        } catch (InterruptedException e) {
            logger.error("Failed to wait indefinitely ", e);
        }
        //latch it
    }

    @Override
    protected void initializeMoreStuff(Map<String, String> args) {
        monitorContextConfiguration = getContextConfiguration();
        configYml = new JVMArgsToConfigYmlMapper((Map<String, ? super Object>)monitorContextConfiguration.getConfigYml())
                .generateConfigYmlFromJVMArgs();
        monitorContextConfiguration.setMetricXml(args.get("metric-file"), Stat.Stats.class);
    }

    public static void main(String[] args) throws TaskExecutionException {
        ConsoleAppender ca = new ConsoleAppender();
        ca.setWriter(new OutputStreamWriter(System.out));
        ca.setLayout(new PatternLayout("%-5p [%t]: %m%n"));
        ca.setThreshold(Level
                .DEBUG);
        org.apache.log4j.Logger.getRootLogger().addAppender(ca);


    /*FileAppender fa = new FileAppender(new PatternLayout("%-5p [%t]: %m%n"), "cache.log");
    fa.setThreshold(Level.DEBUG);
    LOGGER.getRootLogger().addAppender(fa);*/


        PCFFirehoseMonitor monitor = new PCFFirehoseMonitor();


        Map<String, String> taskArgs = new HashMap<String, String>();
        taskArgs.put("config-file", "/Users/aditya.jagtiani/repos/appdynamics/extensions/pcf-firehose-monitoring-extension/src/main/resources/conf/config.yml");
        taskArgs.put("metric-file", "/Users/aditya.jagtiani/repos/appdynamics/extensions/pcf-firehose-monitoring-extension/src/main/resources/conf/metrics.xml");
        monitor.execute(taskArgs, null);
    }
}
