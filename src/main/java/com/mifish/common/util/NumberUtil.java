package com.mifish.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author: rls
 * Date: 2018-01-27 18:28
 */
public final class NumberUtil {

    private static final Pattern NUM_PATTERN = Pattern.compile("-?[0-9]+.*[0-9]*");

    private static final Pattern INTEGER_PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");

    private static final Pattern DOUBLE_PATTERN = Pattern.compile("^[-\\+]?[.\\d]*$");

    /**
     * isNumeric
     * <p>
     * 判断一个字符串是否是数字，包括整数和小数
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        Matcher isNum = NUM_PATTERN.matcher(str);
        return isNum.matches();
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        Matcher isNum = INTEGER_PATTERN.matcher(str);
        return isNum.matches();
    }

    /**
     * 判断是否为浮点数，包括double和float
     *
     * @param str 传入的字符串
     * @return 是浮点数返回true, 否则返回false
     */
    public static boolean isDouble(String str) {
        if (str == null) {
            return false;
        }
        Matcher isNum = DOUBLE_PATTERN.matcher(str);
        return isNum.matches();
    }


    /**
     * NumberUtil
     */
    private NumberUtil() {

    }
}
