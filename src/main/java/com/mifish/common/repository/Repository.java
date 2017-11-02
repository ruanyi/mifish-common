package com.mifish.common.repository;

import java.util.Date;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 22:06
 */
public interface Repository {

    /**
     * init
     *
     * @return void
     */
    void init();

    /**
     * destroy
     *
     * @return void
     */
    void destroy();

    /**
     * getTimeVersion
     *
     * @return
     */
    Date getTimeVersion();


    /**
     * refresh
     *
     * @param timeVersion
     * @return
     */
    boolean refresh(Date timeVersion);
}
