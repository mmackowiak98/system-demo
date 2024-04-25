package org.dpd.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomPartitioner implements Partitioner {

    @Override
    public int partition(
            String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        String shipmentNumber = (String) key;
        int numPartitions = cluster.partitionCountForTopic(topic);
        return Math.abs(shipmentNumber.hashCode() % numPartitions);
    }

    @Override
    public void close() {}

    @Override
    public void configure(Map<String, ?> configs) {}
}
