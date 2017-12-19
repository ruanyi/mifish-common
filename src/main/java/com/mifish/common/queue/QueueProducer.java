package com.mifish.common.queue;

import com.mifish.common.queue.model.SendResult;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-19 21:52
 */
public interface QueueProducer {

    /**
     * start
     */
    void start();

    /**
     * stop
     */
    void stop();

    /**
     * send
     *
     * @param topic
     * @param value
     */
    SendResult send(String topic, Object value);

    /**
     * asyncSend
     *
     * @param topic
     * @param value
     * @param callback
     * @return
     */
    void asyncSend(String topic, Object value, SendCallback callback);
}
