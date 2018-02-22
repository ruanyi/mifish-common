package com.mifish.common.repository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 *
 * @author: rls
 * Date: 2018-01-26 21:10
 */
public abstract class AbstractRepositoryTrigger implements RepositoryTrigger {

    /**
     * repositories
     */
    private List<Repository> repositories;

    /**
     * lock
     */
    private Lock lock = new ReentrantLock();

    /**
     * touchoff
     *
     * @param isForced
     * @return
     */
    @Override
    public boolean touchoff(boolean isForced) {
        lock.lock();
        boolean isSuccess = true;
        try {
            Date now = new Date();
            for (Repository repository : this.repositories) {
                isSuccess = repository.refresh(now.getTime());
            }
            return isSuccess;
        } catch (Exception ex) {
            //ingore
            return false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * getRepository
     *
     * @return
     */
    @Override
    public List<Repository> getRepositories() {
        return this.repositories;
    }

    /**
     * setRepositories
     *
     * @param repositories
     */
    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }
}
