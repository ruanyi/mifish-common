package com.mifish.common.queue.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-05 18:09
 */
public class QueueMessage implements Serializable {

    private static final long serialVersionUID = -1264797147151455344L;

    public static final String MSG_KEY = "MSG_KEY";

    public static final String CLIENT_ID = "CLIENT_ID";

    public static final String OFFSET = "OFFSET";

    /***group*/
    private String group;

    /***topic*/
    private String topic;

    /***partition*/
    private int partition;

    /***message*/
    private String message;

    /***attributes*/
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * QueueMessage
     *
     * @param group
     * @param topic
     * @param partition
     * @param message
     */
    public QueueMessage(String group, String topic, int partition, String message) {
        this.group = group;
        this.topic = topic;
        this.partition = partition;
        this.message = message;
    }

    /**
     * getGroup
     *
     * @return
     */
    public String getGroup() {
        return group;
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
     * getMessage
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * getPartition
     *
     * @return
     */
    public int getPartition() {
        return partition;
    }

    /**
     * addAttribute
     *
     * @param key
     * @param value
     */
    public void addAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    /**
     * addAttributes
     *
     * @param attributes
     */
    public void addAttributes(Map<String, Object> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return;
        }
        this.attributes.putAll(attributes);
    }

    /**
     * getAttribute
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getAttribute(String key, Class<T> clazz) {
        Object obj = this.attributes.get(key);
        if (obj == null) {
            return null;
        }
        return clazz.cast(obj);
    }

    @Override
    public String toString() {
        return "QueueMessage{group=" + group + ", topic='" + topic + ", partition=" + partition + ", message='" +
                message + '}';
    }
}
