package com.mifish.common.util;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;

/**
 * ArraysUtil
 *
 * @author ruanlsh
 * @time:2013-07-18
 */
public final class ArrayUtil extends ArrayUtils {

    /****/
    private ArrayUtil() {
    }

    /**
     * join
     *
     * @param left
     * @param right
     * @return T[]
     */
    public static <T> T[] join(T[] left, T[] right) {
        if (left == null) {
            throw new IllegalArgumentException("the left array cannot be null!!!");
        }
        /**if the left is null or empty,return left!!!*/
        if (right == null || right.length == 0) {
            return left;
        }
        T[] result = Arrays.copyOf(left, left.length + right.length);
        System.arraycopy(right, 0, result, left.length, right.length);
        return result;
    }
}

