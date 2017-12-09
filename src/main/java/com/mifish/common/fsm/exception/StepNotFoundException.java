package com.mifish.common.fsm.exception;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-06 17:55
 */
public class StepNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 2219265910792122934L;

    public StepNotFoundException(String message) {
        super(message);
    }

    public StepNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StepNotFoundException(Throwable cause) {
        super(cause);
    }

    protected StepNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
