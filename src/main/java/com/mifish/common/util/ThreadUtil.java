package com.mifish.common.util;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-19 21:39
 */
public final class ThreadUtil {

    /**
     * sleep
     *
     * @param mill
     */
    public static void sleep(long mill) {
        try {
            if (mill > 0) {
                Thread.sleep(mill);
            }
        } catch (InterruptedException e) {
            //ingore
        }
    }

    private ThreadUtil() {

    }
}
