package com.mifish.common.queue.model;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * <p>
 * topicpath的配置方式：
 * topic,kafkaStreamSize,executorCorePoolSize,executorMaxPoolSize,executorQueueSize;topic,kafkaStreamSize,
 * executorCorePoolSize,executorMaxPoolSize,executorQueueSize
 *
 * @author: rls
 * Date: 2017-12-05 18:52
 */
public class TopicPathMeta implements Serializable {

    private static final long serialVersionUID = 4328234476513735008L;

    private String topic;

    private int kafkaStreamSize = 1;

    private int executorCorePoolSize = 1;

    private int executorMaxPoolSize = 10;

    private int executorQueueSize = 10;

    /**
     * TopicPathMeta
     *
     * @param topic
     */
    public TopicPathMeta(String topic) {
        this.topic = topic;
    }

    /**
     * TopicPathMeta
     *
     * @param topic
     * @param kafkaStreamSize
     * @param executorCorePoolSize
     * @param executorMaxPoolSize
     * @param executorQueueSize
     */
    public TopicPathMeta(String topic, int kafkaStreamSize, int executorCorePoolSize, int executorMaxPoolSize, int
            executorQueueSize) {
        this.topic = topic;
        this.kafkaStreamSize = kafkaStreamSize;
        this.executorCorePoolSize = executorCorePoolSize;
        this.executorMaxPoolSize = executorMaxPoolSize;
        this.executorQueueSize = executorQueueSize;
    }

    /**
     * getTopic
     *
     * @return
     */
    public String getTopic() {
        return topic;
    }

    /**
     * getKafkaStreamSize
     *
     * @return
     */
    public int getKafkaStreamSize() {
        return kafkaStreamSize;
    }

    /**
     * getExecutorCorePoolSize
     *
     * @return
     */
    public int getExecutorCorePoolSize() {
        return executorCorePoolSize;
    }

    /**
     * getExecutorMaxPoolSize
     *
     * @return
     */
    public int getExecutorMaxPoolSize() {
        return executorMaxPoolSize;
    }

    /**
     * getExecutorQueueSize
     *
     * @return
     */
    public int getExecutorQueueSize() {
        return executorQueueSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TopicPathMeta topicPathMeta = (TopicPathMeta) o;

        if (kafkaStreamSize != topicPathMeta.kafkaStreamSize) {
            return false;
        }
        if (executorCorePoolSize != topicPathMeta.executorCorePoolSize) {
            return false;
        }
        if (executorMaxPoolSize != topicPathMeta.executorMaxPoolSize) {
            return false;
        }
        if (executorQueueSize != topicPathMeta.executorQueueSize) {
            return false;
        }
        return topic != null ? topic.equals(topicPathMeta.topic) : topicPathMeta.topic == null;
    }

    @Override
    public int hashCode() {
        int result = topic != null ? topic.hashCode() : 0;
        result = 31 * result + kafkaStreamSize;
        result = 31 * result + executorCorePoolSize;
        result = 31 * result + executorMaxPoolSize;
        result = 31 * result + executorQueueSize;
        return result;
    }

    @Override
    public String toString() {
        return this.topic + "," + this.kafkaStreamSize + "," + this.executorCorePoolSize + "," + this
                .executorMaxPoolSize + "," + this.executorQueueSize;
    }

    /**
     * of
     *
     * @param topicPath
     * @return
     */
    public static TopicPathMeta of(String topicPath) {
        String[] metaConfigs = StringUtils.split(topicPath, ",");
        if (metaConfigs == null || metaConfigs.length == 0) {
            return null;
        }
        TopicPathMeta topicPathMeta = new TopicPathMeta(metaConfigs[0]);
        if (metaConfigs.length > 1 && StringUtils.isNumeric(metaConfigs[1])) {
            topicPathMeta.kafkaStreamSize = Integer.parseInt(metaConfigs[1]);
        }
        if (metaConfigs.length > 2 && StringUtils.isNumeric(metaConfigs[2])) {
            topicPathMeta.executorCorePoolSize = Integer.parseInt(metaConfigs[2]);
        }
        if (metaConfigs.length > 3 && StringUtils.isNumeric(metaConfigs[3])) {
            topicPathMeta.executorMaxPoolSize = Integer.parseInt(metaConfigs[3]);
        }
        if (metaConfigs.length > 4 && StringUtils.isNumeric(metaConfigs[4])) {
            topicPathMeta.executorQueueSize = Integer.parseInt(metaConfigs[4]);
        }
        return topicPathMeta;
    }

    /**
     * parse2List
     *
     * @param topicPath
     * @return
     */
    public static List<TopicPathMeta> parse2List(String topicPath) {
        String[] topicPaths = StringUtils.split(topicPath, ";");
        if (topicPaths == null || topicPaths.length == 0) {
            return new ArrayList<>();
        }
        List<TopicPathMeta> topicPathMetas = new ArrayList<>(topicPaths.length);
        for (String tp : topicPaths) {
            TopicPathMeta topicPathMeta = TopicPathMeta.of(tp);
            if (topicPathMeta != null) {
                topicPathMetas.add(topicPathMeta);
            }
        }
        return topicPathMetas;
    }

    /**
     * parse2Map
     *
     * @param topicPath
     * @return
     */
    public static Map<String, TopicPathMeta> parse2Map(String topicPath) {
        String[] topicPaths = StringUtils.split(topicPath, ";");
        if (topicPaths == null || topicPaths.length == 0) {
            return new HashMap<>(0);
        }
        Map<String, TopicPathMeta> topicMetas = new HashMap<>(topicPaths.length);
        for (String tp : topicPaths) {
            TopicPathMeta topicPathMeta = TopicPathMeta.of(tp);
            if (topicPathMeta != null) {
                topicMetas.put(topicPathMeta.getTopic(), topicPathMeta);
            }
        }
        return topicMetas;
    }
}
