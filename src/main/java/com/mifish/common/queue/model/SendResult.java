package com.mifish.common.queue.model;

import java.io.Serializable;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-19 22:22
 */
public class SendResult implements Serializable {

    private static final long serialVersionUID = -7507404154725057982L;

    /**
     * success
     */
    private boolean success;

    /**
     * msgId
     */
    private String msgId;

    /**
     * 消息存储位置
     */
    private long offset;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
