package com.mifish.common.chain.nodes;

import com.mifish.common.chain.Chain;
import com.mifish.common.chain.Node;
import com.mifish.common.profiler.MethodProfiler;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 20:16
 */
public class DummyNode<P, R> implements Node<P, R> {

    /**
     * getName
     *
     * @return
     */
    @Override
    public String getName() {
        return "dummy";
    }

    /**
     * doAction
     *
     * @param param
     * @param chain
     * @return
     */
    @Override
    public R doAction(P param, Chain<P, R> chain) {
        try {
            MethodProfiler.enter("node:" + getName() + "begin,Param:" + param);
            //do nothing,and go on execute
            return chain.execute(param);
        } finally {
            MethodProfiler.release();
        }
    }

    /**
     * newInstance
     *
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> Node<T, S> newInstance() {
        return new DummyNode<>();
    }
}
