package com.mifish.common.queue;

import com.mifish.common.queue.model.SendResult;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-19 22:26
 */
public interface SendCallback {

    /**
     * onSuccess
     *
     * @param sendResult
     */
    void onSuccess(final SendResult sendResult);

    /**
     * onException
     *
     * @param e
     */
    void onException(final Throwable e);
}
