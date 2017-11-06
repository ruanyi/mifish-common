package com.mifish.common.chain;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-06 13:44
 */
public interface Engine<P> {

    /**
     * digest
     *
     * @param param
     * @param chain
     * @param <R>
     * @return
     */
    <R> R digest(P param, Chain<P,R> chain);
}
