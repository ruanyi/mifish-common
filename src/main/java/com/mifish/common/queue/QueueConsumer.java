package com.mifish.common.queue;

import com.mifish.common.queue.model.ConsumerStatus;

/**
 * Description:
 *
 * @author: jifeng
 * Date: 2015-08-07 18:44
 */
public interface QueueConsumer {

    /**
     * start
     *
     */
    void start();

    /**
     * stop
     *
     * @param async
     */
    void stop(boolean async);

    /**
     * getConsumerStatus
     *
     * @return
     */
    ConsumerStatus getConsumerStatus();

    /**
     * status
     *
     * @return
     */
    String status();

    /**
     * isStopped
     *
     * @return
     */
    boolean isStopped();

    /**
     * isStopping
     *
     * @return
     */
    boolean isStopping();

    /**
     * isRunning
     *
     * @return
     */
    boolean isRunning();

    /**
     * isStarting
     *
     * @return
     */
    boolean isStarting();

}
