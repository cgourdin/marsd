package org.occiware.clouddesigner.driver.marsd.impl;

import org.occiware.clouddesigner.driver.marsd.facade.ClientConnector;
import kg.apc.perfmon.PerfMonMetricGetter;

import java.io.IOException;
import java.util.*;

import kg.apc.perfmon.client.Transport;

/**
 * Created by cgourdin on 03/02/2017.
 * A connector capable to connect to a perfmon agent.
 */
public class PerfMonConnector implements ClientConnector {

    private Transport transport;

    private List<MetricData> metricsDatas = new LinkedList<>();

    private List<String> orderedMetrics = new LinkedList<>();

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    @Override
    public void connect() throws IOException {
        if (orderedMetrics.isEmpty()) {
            throw new IOException("No metrics defined.");
        }
        String[] metricsOrder = orderedMetrics.toArray(new String[orderedMetrics.size()]);

        transport.startWithMetrics(metricsOrder);

    }

    @Override
    public void disconnect() {
        transport.disconnect();
    }

    @Override
    public void addMetric(String metricName, String params) {
        orderedMetrics.add(metricName.toLowerCase() + PerfMonMetricGetter.DVOETOCHIE + params);
    }

    @Override
    public List<MetricData> readMetrics() {
        String[] metricsArr = transport.readMetrics();
        String metricName;
        clearMetricDatas();
        for (int i = 0; i < metricsArr.length; i++) {
            System.out.println("Data :--> " + metricsArr[i]);
            metricName = orderedMetrics.get(i);
            metricsDatas.add(new MetricData(metricName, new Double(metricsArr[i]), System.currentTimeMillis()));
        }

        return metricsDatas;
    }

    @Override
    public void clearMetricDatas() {
        if (metricsDatas == null) {
            metricsDatas = new LinkedList<>();
        }
        metricsDatas.clear();
    }
}
