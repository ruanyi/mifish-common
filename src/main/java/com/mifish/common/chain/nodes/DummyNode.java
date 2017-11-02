package com.mifish.common.chain.nodes;

import com.mifish.common.chain.Chain;
import com.mifish.common.chain.Node;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 20:16
 */
public class DummyNode<P> implements Node<P> {

    /**
     * doAction
     *
     * @param param
     * @param chain
     */
    @Override
    public void doAction(P param, Chain<P> chain) {
        //do nothing,and go on execute
        chain.execute(param);
    }

    /**
     * newInstance
     *
     * @param <T>
     * @return
     */
    public static <T> Node<T> newInstance() {
        return new DummyNode<>();
    }
}
