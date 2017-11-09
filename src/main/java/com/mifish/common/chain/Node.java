package com.mifish.common.chain;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 20:02
 */
public interface Node<P, R> {

    /**
     * getName
     *
     * @return
     */
    String getName();

    /**
     * doAction
     *
     * @param param
     * @param chain
     * @return R
     */
    R doAction(P param, Chain<P, R> chain);
}
