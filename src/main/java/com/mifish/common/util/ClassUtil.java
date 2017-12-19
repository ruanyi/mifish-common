package com.mifish.common.util;

import org.apache.commons.lang.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * ClassUtil
 *
 * @author by rls
 * @time:2012-10-09
 */
public final class ClassUtil extends ClassUtils {

    /**
     * join
     *
     * @param left
     * @param right
     * @return
     */
    public static Field[] join(Field[] left, Field[] right) {
        return ArrayUtil.join(left, right);
    }

    /**
     * getDefaultClassLoader()
     *
     * @return ClassLoader
     */
    public static ClassLoader getDefaultClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * getAllFields
     *
     * @param cls
     * @param annoCls
     * @return Field[]
     */
    public static Field[] getAllFields(Class<?> cls, Class<? extends Annotation> annoCls) {
        Field[] fields = getDeclaredField(cls, annoCls);
        if (cls.getSuperclass() == null) {
            return fields;
        } else {
            fields = join(fields, getAllFields(cls.getSuperclass(), annoCls));
        }
        return fields;
    }

    /**
     * getAllFields
     *
     * @param cls
     * @return field[]
     */
    public static Field[] getAllFields(Class<?> cls) {
        Field[] fields = getDeclaredField(cls);
        if (cls.getSuperclass() == null) {
            return fields;
        } else {
            fields = join(fields, getAllFields(cls.getSuperclass()));
        }
        return fields;
    }

    /**
     * getDeclaredField
     *
     * @param cls
     * @param annoCls
     * @return
     */
    public static Field[] getDeclaredField(Class<?> cls, Class<? extends Annotation> annoCls) {
        Field[] fields = cls.getDeclaredFields();
        List<Field> ls = new ArrayList<Field>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getAnnotation(annoCls) == null) {
                continue;
            }
            fields[i].setAccessible(true);
            ls.add(fields[i]);
        }
        return ls.toArray(new Field[]{});
    }

    /**
     * getDeclaredField
     *
     * @param cls
     * @return
     */
    public static Field[] getDeclaredField(Class<?> cls) {
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
        }
        return fields;
    }

    private ClassUtil() {

    }
}
