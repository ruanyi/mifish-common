package com.mifish.common.util;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.apache.commons.lang.StringUtils;

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
     * GROOVY_CLASS_LOADER
     */
    private static final GroovyClassLoader GROOVY_CLASS_LOADER = new GroovyClassLoader();

    /**
     * groovyShell
     */
    private static GroovyShell groovyShell = new GroovyShell();

    /**
     * parseClass
     *
     * @param text
     * @return
     */
    public static Class<?> parseClass(String text) {
        try {
            if (StringUtils.isBlank(text)) {
                return null;
            }
            Class<?> clazz = GROOVY_CLASS_LOADER.parseClass(text);
            return clazz;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * executeScript
     *
     * @param scriptStr
     * @param params
     * @return
     */
    public static Object executeScript(String scriptStr, Map<String, Object> params) {
        try {
            Script script = groovyShell.parse(scriptStr);
            Binding binding = new Binding();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    binding.setVariable(entry.getKey(), entry.getValue());
                }
            }
            script.setBinding(binding);
            return script.run();
        } catch (Exception ex) {
            return null;
        }
    }

}
