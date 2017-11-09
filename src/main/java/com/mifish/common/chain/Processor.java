package com.mifish.common.chain;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 20:09
 */
public interface Processor<P, R> {

    /**
     * getName
     *
     * @return
     */
    String getName();

    /**
     * process
     *
     * @param param
     * @return R
     */
    R process(P param);
}
