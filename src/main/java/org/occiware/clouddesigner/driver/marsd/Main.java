package org.occiware.clouddesigner.driver.marsd;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import kg.apc.perfmon.PerfMonMetricGetter;
import kg.apc.perfmon.client.Transport;
import kg.apc.perfmon.client.TransportFactory;
import kg.apc.perfmon.metrics.MetricParams;
import org.occiware.clouddesigner.driver.marsd.facade.ClientConnector;
import org.occiware.clouddesigner.driver.marsd.impl.MetricData;
import org.occiware.clouddesigner.driver.marsd.impl.PerfMonConnector;

/**
 * Created by cgourdin on 03/02/2017.
 */
public class Main {

    public static void main(String args[]) {
        // 1er test : se connecter à perfmon agent
        // 2nd test : récupérer une métrique
        // 3eme test: se deconnecter de l'agent.

        // Pour l'instant valeurs en dur.
        String host = "localhost";
        int port = 4444;
        String metricCPU = "CPU";
        String metricRAM = "Memory";

        // String params;
        try {
            ClientConnector connector = getConnector(host, port, 1);

            connector.addMetric(metricCPU, "combined");
            connector.addMetric(metricRAM, "usedperc");

            connector.connect();
            for (int j = 0; j <= 4; j++) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.err.println("Monitoring thread is interrupted ! --> " + ex.getMessage());
                }

                List<MetricData> datas = connector.readMetrics();
                System.out.println("Connected and now Getting datas...");
                for (MetricData data : datas) {
                    System.out.println("Metric :--> " + data.getMetricType() + " --> " + data.getMetricValue() +  " --> " + data.getTime());
                }

            }

            connector.disconnect();

        } catch (IOException ex) {
            System.err.println("Exception thrown : " + ex.getClass().getName() + " --> " + ex.getMessage());
        }

    }



    public static PerfMonConnector getConnector(String host, int port, int interval) throws IOException {
        SocketAddress addr = new InetSocketAddress(host, port);
        Transport transport;

        try {
            transport = TransportFactory.TCPInstance(addr);
            if (!transport.test()) {
                throw new IOException("Agent is unreachable via TCP");
            }
        } catch (IOException e) {
            System.err.println("Can't connect TCP transport for host: " + addr.toString());
            try {
                    System.out.println("Connecting UDP !!");
                    transport = TransportFactory.UDPInstance(addr);
                    if (!transport.test()) {
                        throw new IOException("Agent is unreachable via UDP");
                    }
                } catch (IOException ex) {
                    System.err.println("Can't connect UDP transport for host: " + addr.toString());
                    throw ex;
                }
        }

        PerfMonConnector connector = new PerfMonConnector();
        connector.setTransport(transport);
        transport.setInterval(interval);
        return connector;
    }



}



//    private void initiateConnector(String host, int port, int index, String metric, String params) {
//        InetSocketAddress addr = new InetSocketAddress(host, port);
//        String stringKey = addr.toString() + "#" + index;
//        String labelHostname = host;
//
//        // handle label parameter
//        MetricParams paramsParsed = MetricParams.createFromString(params);
//        String label;
//        if (paramsParsed.getLabel().isEmpty()) {
//            label = labelHostname + " " + metric;
//            if (!params.isEmpty()) {
//                label = label + " " + params;
//            }
//        } else {
//            label = labelHostname + " " + metric + " " + paramsParsed.getLabel();
//
//            String[] tokens = params.split("(?<!\\\\)" + PerfMonMetricGetter.DVOETOCHIE);
//
//            params = "";
//
//            for (String token : tokens) {
//                if (!token.startsWith("label=")) {
//                    if (params.length() != 0) {
//                        params = params + PerfMonMetricGetter.DVOETOCHIE;
//                    }
//                    params = params + token;
//                }
//            }
//        }
//
//        try {
//            if (connectors.containsKey(addr)) {
//                connectors.get(addr).addMetric(metric, params, label);
//            } else {
//                PerfMonAgentConnector connector = getConnector(host, port);
//                connector.addMetric(metric, params, label);
//                connectors.put(addr, connector);
//            }
//        } catch (IOException e) {
//            log.error("Problems creating connector", e);
//            connectors.put(stringKey, new UnavailableAgentConnector(e));
//        }
//    }


