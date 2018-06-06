/*
 * Copyright 2018. AppDynamics LLC and its affiliates. All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates. The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.extensions.pcffirehose.consumer.ingress;

import com.appdynamics.extensions.pcffirehose.consumer.loggregator.v2.EgressGrpc;
import com.appdynamics.extensions.pcffirehose.consumer.loggregator.v2.LoggregatorEgress;
import com.appdynamics.extensions.pcffirehose.consumer.loggregator.v2.LoggregatorEnvelope;
import com.appdynamics.extensions.pcffirehose.util.LoggregatorMetricType;
import io.grpc.ManagedChannel;
/*import io.grpc.netty.shaded.NegotiationType;
import io.grpc.netty.shaded.NettyChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth;
import io.grpc.netty.shaded.io.netty.handler.ssl.SupportedCipherSuiteFilter;*/
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SupportedCipherSuiteFilter;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by aditya.jagtiani on 4/12/18.
 */
public class LoggregatorConsumer {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(LoggregatorConsumer.class);
    private NettyChannelBuilder channelBuilder;
    private EgressGrpc.EgressBlockingStub blockingStub;
    private Iterator<LoggregatorEnvelope.Envelope> envelopes;

    public LoggregatorConsumer(String host, int port, String certFile, String keyFile, String caCertFile, String authority)
            throws SSLException {
        List<String> ciphers = new ArrayList<>();
        ciphers.add("TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384");
        this.channelBuilder = NettyChannelBuilder.forAddress(host, port)
                .negotiationType(NegotiationType.TLS)
                .sslContext(GrpcSslContexts.forClient()
                        .clientAuth(ClientAuth.REQUIRE)
                        .keyManager(new File(certFile), new File(keyFile))
                        .trustManager(new File(caCertFile))
                        .ciphers(ciphers, SupportedCipherSuiteFilter.INSTANCE)
                        .build())
                .keepAliveTime(30, TimeUnit.SECONDS)
                .idleTimeout(30, TimeUnit.SECONDS)
                .overrideAuthority(authority);
    }

    private void safeSleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {}
    }

    public LoggregatorMetric getLoggregatorMetric() throws NoMetricException {
        if(envelopes == null || !envelopes.hasNext()) {
            createRequest();
        }
        LoggregatorEnvelope.Envelope envelope = envelopes.next();
        switch (envelope.getMessageCase()) {
            case GAUGE:
                Map<String, LoggregatorEnvelope.GaugeValue> metricsMap = envelope.getGauge().getMetricsMap();
                Map.Entry<String, LoggregatorEnvelope.GaugeValue> first = metricsMap.entrySet().iterator().next();
                return new LoggregatorMetric(first.getKey(), first.getValue().getValue(), envelope.getTimestamp(),
                        envelope.getTagsMap(), LoggregatorMetricType.GAUGE);
            case COUNTER:
                return new LoggregatorMetric(envelope.getCounter().getName(), (double) envelope.getCounter().getTotal(),
                        envelope.getTimestamp(), envelope.getTagsMap(), LoggregatorMetricType.COUNTER);
        }
        throw new NoMetricException();
    }

    public void resetEnvelopes() {
        safeSleep(1000);
        envelopes = null;
    }

    private void createRequest() {
        logger.debug("Creating new connection to loggregator's rlp");
        ManagedChannel channel = channelBuilder.build();

        blockingStub = EgressGrpc.newBlockingStub(channel);

        LoggregatorEgress.EgressRequest request = LoggregatorEgress.EgressRequest.newBuilder()
                .addSelectors(getCounterSelector())
                .addSelectors(getGaugeSelector())
                .setUsePreferredTags(true)
                .build();
        envelopes = blockingStub.receiver(request);
    }

    private LoggregatorEgress.Selector getGaugeSelector() {
        return LoggregatorEgress.Selector.newBuilder().setGauge(LoggregatorEgress.GaugeSelector.newBuilder().build()).build();
    }

    private LoggregatorEgress.Selector getCounterSelector() {
        return LoggregatorEgress.Selector.newBuilder().setCounter(LoggregatorEgress.CounterSelector.newBuilder().build()).build();
    }

    private class NoMetricException extends Exception{}

}
