package com.mifish.common.chain;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 20:02
 */
public interface Chain<P, R> {

    /**
     * execute
     *
     * @param param
     */
    R execute(P param);
}
