package com.mifish.common.chain;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 20:02
 */
public interface Node<P> {

    /**
     * doAction
     *
     * @param param
     * @param chain
     */
    void doAction(P param, Chain<P> chain);
}
