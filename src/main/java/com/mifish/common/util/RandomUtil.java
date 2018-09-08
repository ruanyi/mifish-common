package com.mifish.common.util;

import java.util.Random;

/**
 * Description:
 * <p>
 *
 * @author: rls
 * Date: 2017-08-26 14:26
 */
public final class RandomUtil {

    /**
     * randomRange
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomRange(int min, int max) {
        Random random = new Random();
        if (min > max) {
            return -1;
        }
        int result = random.nextInt(max) % (max - min + 1) + min;
        return result;
    }

    /**
     * forbit instance
     */
    private RandomUtil() {

    }
}
