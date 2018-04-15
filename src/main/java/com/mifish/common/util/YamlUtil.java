package com.mifish.common.util;

import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 *
 * @author: rls
 * Date: 2018-04-02 11:26
 */
public final class YamlUtil {

    /**
     * 解析文件成指定的领域模型
     * <p>
     * ]
     *
     * @param filepath
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseFile(String filepath, Class<T> clazz) {
        if (StringUtils.isBlank(filepath) || clazz == null) {
            return null;
        }
        try (InputStream input = new FileInputStream(new File(filepath))) {
            Yaml yaml = new Yaml();
            return yaml.loadAs(input, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * parseFile
     *
     * @param yamlFile
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseFile(File yamlFile, Class<T> clazz) {
        if (yamlFile == null || clazz == null || !yamlFile.isFile()) {
            return null;
        }
        try (InputStream input = new FileInputStream(yamlFile)) {
            Yaml yaml = new Yaml();
            return yaml.loadAs(input, clazz);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 解析指定对象成yaml格式的文件
     *
     * @param data
     * @return
     */
    public static String serialization(Object data) {
        if (data == null) {
            return "";
        }
        Yaml yaml = new Yaml();
        return yaml.dump(data);
    }


    /**
     * forbit instance
     */
    private YamlUtil() {

    }
}


