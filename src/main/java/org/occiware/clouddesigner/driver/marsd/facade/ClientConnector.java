package org.occiware.clouddesigner.driver.marsd.facade;

import org.occiware.clouddesigner.driver.marsd.impl.MetricData;

import java.io.IOException;
import java.util.List;

/**
 * Created by cgourdin on 03/02/2017.
 * Interface to define a client to connect to a metric agent.
 */
public interface ClientConnector {

    void connect() throws IOException;

    void disconnect();

    void addMetric(String metricName, String params);

    List<MetricData> readMetrics();

    void clearMetricDatas();
}
