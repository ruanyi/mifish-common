package com.mifish.common.fsm.life;

import com.mifish.common.fsm.Life;
import com.mifish.common.fsm.Road;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-10 00:10
 */
public class SimpleLife implements Life {

    /**
     * 默认的路，假如没有选择好主题，则返回默认的路
     */
    private Road defaultRoad;

    /**
     * 漫漫人生所包含的所有可能要走的路
     * 根据主题划分
     */
    private Map<String, Road> roadContainer = new ConcurrentHashMap<>();

    /**
     * select
     *
     * @param topic
     * @return
     */
    @Override
    public Road select(String topic) {
        if (this.roadContainer.containsKey(topic)) {
            return this.roadContainer.get(topic);
        }
        return defaultRoad;
    }

    /**
     * addRoad
     *
     * @param road
     * @return
     */
    @Override
    public boolean addRoad(Road road) {
        if (road == null) {
            return false;
        }
        this.roadContainer.put(road.getTopic(), road);
        return true;
    }

    /**
     * setDefaultRoad
     *
     * @param defaultRoad
     * @return
     */
    @Override
    public void setDefaultRoad(Road defaultRoad) {
        this.defaultRoad = defaultRoad;
    }

    /**
     * getDefaultRoad
     *
     * @return
     */
    @Override
    public Road getDefaultRoad() {
        return this.defaultRoad;
    }

    /**
     * removeRoad
     *
     * @param topic
     * @return
     */
    @Override
    public Road removeRoad(String topic) {
        return this.roadContainer.remove(topic);
    }

    /**
     * setRoadContainer
     *
     * @param roadContainer
     */
    public synchronized void setRoadContainer(Map<String, Road> roadContainer) {
        if (roadContainer == null || roadContainer.isEmpty()) {
            return;
        }
        this.roadContainer.clear();
        this.roadContainer.putAll(roadContainer);
    }
}
