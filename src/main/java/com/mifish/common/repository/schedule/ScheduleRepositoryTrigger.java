package com.mifish.common.repository.schedule;

import com.mifish.common.repository.AbstractRepositoryTrigger;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author: rls
 * Date: 2018-01-26 21:09
 */
public class ScheduleRepositoryTrigger extends AbstractRepositoryTrigger {

    /**
     * initialDelay
     */
    private long initialDelay = 30;

    /**
     * delay
     */
    private long delay = 10;

    /**
     * threadPoolExecutor
     */
    private ScheduledThreadPoolExecutor threadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    /**
     * init
     */
    public void init() throws Exception {
        this.threadPoolExecutor.scheduleWithFixedDelay(() -> touchoff(false), this.initialDelay, this.delay, TimeUnit
                .SECONDS);
    }


    /**
     * destroy
     */
    public void destroy() throws Exception {
        threadPoolExecutor.shutdown();
    }

    /**
     * setInitialDelay
     *
     * @param initialDelay
     */
    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    /**
     * setDelay
     *
     * @param delay
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }
}
