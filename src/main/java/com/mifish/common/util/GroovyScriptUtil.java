package com.mifish.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * GroovyScriptUtil
 * Groovy脚本引擎
 *
 * @author ruanlongsheng
 * @time:2013-12-18
 */
public final class GroovyScriptUtil {

    //不允许实例化
    private GroovyScriptUtil() {
    }

    /**
     * eval
     *
     * @param script
     * @param params
     * @return Object
     */
    public static Object eval(String script, Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<String, Object>();
        }

        return null;
    }

    public static Object eval(String script) {
        return eval(script, null);
    }

    //
    public static Object evalCache(String key, String script, Map<String, Object> params) {

        return null;
    }
}
