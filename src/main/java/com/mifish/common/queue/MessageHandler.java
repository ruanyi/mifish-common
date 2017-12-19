package com.mifish.common.queue;

import com.mifish.common.queue.model.MessageStatus;
import com.mifish.common.queue.model.QueueMessage;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-05 18:07
 */
public interface MessageHandler {

    /**
     * handle
     *
     * @param message
     * @return
     * @throws Exception
     */
    MessageStatus handle(QueueMessage message) throws Exception;
}
