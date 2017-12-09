package com.mifish.common.fsm;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-09 23:58
 */
public interface Life {


    /**
     * 根据主题来选择：要走哪一条路
     * 假如根据自己的主题，没有选择到road，则使用默认的road
     * <p>
     * 肯定不会返回null
     *
     * @param topic
     * @return
     */
    Road select(String topic);

    /**
     * addRoad
     *
     * @param road
     * @return
     */
    boolean addRoad(Road road);

    /**
     * setDefaultRoad
     *
     * @param defaultRoad
     * @return
     */
    void setDefaultRoad(Road defaultRoad);

    /**
     * getDefaultRoad
     *
     * @return
     */
    Road getDefaultRoad();

    /**
     * removeRoad
     *
     * @param topic
     * @return
     */
    Road removeRoad(String topic);
}
