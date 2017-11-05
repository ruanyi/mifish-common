package com.mifish.common.repository;

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
     * getVersion
     *
     * @return
     */
    long getVersion();

    /**
     * refresh
     *
     * @param version
     * @return
     */
    boolean refresh(long version);
}
