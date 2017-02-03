package org.occiware.clouddesigner.driver.marsd.impl;

/**
 * Created by cgourdin on 03/02/2017.
 * One metric data like a cpu usage.
 * This object is composed of :
 *   - a metric perfmon type like cpu:component
 *   - a metric value in double.
 *   - a time in millisecond when getting this value from system time.
 */
public class MetricData {



    private String metricType;
    private Double metricValue;
    private Long time;

    public MetricData(String metricType, Double metricValue, Long time) {
        this.metricType = metricType;
        this.metricValue = metricValue;
        this.time = time;
    }

    public String getMetricType() {
        return metricType;
    }

    public Double getMetricValue() {
        return metricValue;
    }

    public Long getTime() {
        return time;
    }

}
