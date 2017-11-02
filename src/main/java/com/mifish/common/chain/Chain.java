package com.mifish.common.chain;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 20:02
 */
public interface Chain<P> {

    /**
     * execute
     *
     * @param param
     */
    void execute(P param);
}
