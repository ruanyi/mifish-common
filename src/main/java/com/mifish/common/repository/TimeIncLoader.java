package com.mifish.common.repository;

import java.util.List;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2017-11-02 22:09
 */
public interface TimeIncLoader<T> {

    /**
     * loadAll
     *
     * @return
     */
    List<T> loadAll();

    /**
     * loadSome
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<T> loadSome(long startTime, long endTime);

}
