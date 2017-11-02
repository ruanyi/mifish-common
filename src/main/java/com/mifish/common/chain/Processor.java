package com.mifish.common.chain;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 20:09
 */
public interface Processor<P> {

    /**
     * process
     *
     * @param param
     */
    void process(P param);
}
