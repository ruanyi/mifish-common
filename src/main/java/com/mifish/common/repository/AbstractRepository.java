package com.mifish.common.repository;


import com.mifish.common.util.RandomUtil;
import com.mifish.common.util.ThreadUtil;

/**
 * Description:
 *
 * @author: rls
 * Date: 2018-01-18 17:04
 */
public abstract class AbstractRepository implements Repository {

    /**
     * timeVersion
     */
    protected volatile long timeVersion = -1;

    /**
     * init
     *
     * @throws Exception
     */
    @Override
    public void init()  {
        this.timeVersion = -1;
    }

    /**
     * destroy
     *
     * @throws Exception
     */
    @Override
    public void destroy() {
        this.timeVersion = -1;
    }

    /**
     * getVersion
     *
     * @return
     */
    @Override
    public long getVersion() {
        return this.timeVersion;
    }

    /**
     * refresh
     *
     * @param version
     * @return
     */
    @Override
    public synchronized boolean refresh(long version) {
        if (this.timeVersion >= version) {
            return false;
        }
        //随机休眠
        ThreadUtil.sleep(RandomUtil.randomRange(10, 1000));
        boolean isSuccess = doLoad(this.timeVersion, version);
        if (isSuccess) {
            this.timeVersion = version;
        }
        return isSuccess;
    }

    /**
     * 真正去加载相关的数据
     *
     * @param startTime
     * @param endTime
     * @return
     */
    protected abstract boolean doLoad(long startTime, long endTime);
}
