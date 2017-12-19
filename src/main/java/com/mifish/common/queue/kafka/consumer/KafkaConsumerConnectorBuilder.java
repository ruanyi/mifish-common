package com.mifish.common.queue.kafka.consumer;

import com.mifish.common.util.NetworkUtil;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * Description:
 *
 * @author: YangZhiChao
 * Date: 2017-12-05 18:59
 */
public class KafkaConsumerConnectorBuilder {

    /**
     * build
     *
     * @param zk
     * @param groupId
     * @param configs
     * @return
     */
    static ConsumerConnector build(String zk, String groupId, Map<String, String> configs) {

        Properties props = new Properties();
        props.put("zookeeper.connect", zk);
        props.put("group.id", groupId);

        if (!props.containsKey("zookeeper.connect")) {
            throw new IllegalArgumentException("zookeeper.connect can't be null");
        }

        if (!props.containsKey("group.id")) {
            throw new IllegalArgumentException("group.id can't be null");
        }

        String propName = "offsets.storage";
        if (!props.containsKey(propName)) {
            props.put(propName, "zookeeper");
        }

        propName = "auto.commit.interval.ms";
        if (!props.containsKey(propName)) {
            props.put(propName, "30000");
        }

        if (configs != null) {
            for (String key : configs.keySet()) {
                props.put(key, configs.get(key));
            }
        }

        String localIP = NetworkUtil.getLocalIP();
        if (localIP != "") {
            String consumerID = localIP + "-"
                    + System.currentTimeMillis() + "-"
                    + Long.toHexString(UUID.randomUUID().getMostSignificantBits()).substring(0, 8);
            props.put("consumer.id", consumerID);
        }

        return kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
    }

    /**
     * buildStringDecoder
     *
     * @param charset
     * @return
     */
    static StringDecoder buildStringDecoder(String charset) {
        if (Charset.isSupported(charset)) {
            Properties properties = new Properties();
            properties.setProperty("serializer.encoding", charset);
            return new StringDecoder(new VerifiableProperties(properties));
        }
        //默认是utf-8
        return new StringDecoder(null);
    }
}
