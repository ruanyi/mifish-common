package com.mifish.common.fsm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-10 00:00
 */
public class Society implements Serializable {

    private static final long serialVersionUID = 6677914339921477981L;
    
    /**
     * 主要提供给每一步共享一些临时信息
     * 注意：临时，永久类信息，请自行持久化
     * 它的生命周期：一条路，当这条路，走完后，就不在了
     */
    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    /**
     * walkStepPath
     * 存放正在走的路的路径
     * 每走一步加入到里面，可以打印出来，目前已经走到哪儿了
     */
    private LinkedList<String> walkStepPath = new LinkedList<>();

    /**
     * addAttribute
     *
     * @param key
     * @param value
     */
    public void addAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    /**
     * addAttributes
     *
     * @param attributes
     */
    public void addAttributes(Map<String, Object> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return;
        }
        this.attributes.putAll(attributes);
    }

    /**
     * getAttribute
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getAttribute(String key, Class<T> clazz) {
        Object obj = this.attributes.get(key);
        if (obj == null) {
            return null;
        }
        return clazz.cast(obj);
    }

    /**
     * 获取当前执行过程中的快照路径
     *
     * @return
     */
    public String getWalkStepSnapshot() {
        List<String> currentPath = new ArrayList<>(this.walkStepPath);
        StringBuilder result = new StringBuilder();
        for (Iterator<String> itr = currentPath.iterator(); itr.hasNext(); ) {
            result.append(itr.next());
            if (itr.hasNext()) {
                result.append("->");
            }
        }
        return result.toString();
    }

    /**
     * 注意：锁
     * <p>
     * 不做去重操作没做一步则放入目前正在做的步的名字
     *
     * @param stepName
     */
    public synchronized void addWalkStepName(String stepName) {
        this.walkStepPath.add(stepName);
    }
}
