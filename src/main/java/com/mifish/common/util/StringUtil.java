package com.mifish.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * StringUtil
 *
 * @author rls
 * @time:2013-07-18
 */
public final class StringUtil extends StringUtils {

    /***/
    private StringUtil() throws Exception {
        throw new IllegalAccessException("");
    }

    /**
     * splitStr
     *
     * @param str
     * @param splitchar
     * @return String[]
     */
    public static String[] splitStr(String str, String splitchar) {
        if (str == null || str.length() == 0) {
            return new String[]{};
        }
        //
        List<String> result = new ArrayList<String>();
        int loc = str.indexOf(splitchar);
        String temp = str;
        while (loc != -1) {
            result.add(temp.substring(0, loc));
            temp = temp.substring(loc + splitchar.length());
            loc = temp.indexOf(splitchar);
        }
        //
        if (temp != null) {
            result.add(temp);
        }
        //
        return result.toArray(new String[]{});
    }

    /**
     * trimLeft
     *
     * @param str
     * @return String str
     */
    public static String trimLeft(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        String temp = str;
        temp = temp + "H";
        temp = temp.trim();
        return temp.substring(0, temp.length() - 1);
    }

    /**
     * trimRight
     *
     * @param str
     * @return String str
     */
    public static String trimRight(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        String temp = str;
        temp = "H" + temp;
        temp = temp.trim();
        return temp.substring(1);
    }

    /**
     * 汉字分割成字符串数组，请勿包含字母，就算包含字母，也会丢弃掉。
     *
     * @param str
     * @return
     */
    public static final String[] splitChineseCharacter(String str) {
        if (StringUtil.isBlank(str)) {
            return new String[0];
        }

        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        List<String> result = new ArrayList<String>();
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                result.add(m.group());
            }
        }
        return result.toArray(new String[]{});
    }
}
