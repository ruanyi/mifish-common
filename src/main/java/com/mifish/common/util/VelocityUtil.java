package com.mifish.common.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.StringWriter;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-14 13:50
 */
public final class VelocityUtil {


    /**
     * execute
     *
     * @param vmpath
     * @return
     */
    public static String execute(String vmpath) {
        //1. create a new instance of the engine
        VelocityEngine ve = new VelocityEngine();
        //2. configure the engine
        ve.setProperty(VelocityEngine.RESOURCE_LOADER, "file");
        //设置velocity资源加载方式为file时的处理类
        ve.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        ve.init();
        VelocityContext context = new VelocityContext();
        context.put("stringUtil", "com.mifish.common.util.StringUtil");
        context.put("ddd","a");
        Template template = null;
        try {
            template = ve.getTemplate(vmpath);
        } catch (ResourceNotFoundException e) {
            // couldn't find the template
        } catch (ParseErrorException pee) {
            // syntax error: problem parsing the template
        } catch (MethodInvocationException mie) {
            // something invoked in the template
            // threw an exception
        } catch (Exception e) {

        }
        StringWriter sw = new StringWriter();
        template.merge(context, sw);
        return sw.toString();
    }

    /**
     * VelocityUtil
     */
    private VelocityUtil() {

    }
}
