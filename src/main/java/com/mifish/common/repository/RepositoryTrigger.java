package com.mifish.common.repository;

import java.util.List;

/**
 * Description:
 *
 * @author: rls
 * Date: 2018-01-04 21:18
 */
public interface RepositoryTrigger {

    /**
     * touchoff
     *
     * @param isForced
     * @return
     */
    boolean touchoff(boolean isForced);

    /**
     * getRepositories
     *
     * @return
     */
    List<Repository> getRepositories();
}
