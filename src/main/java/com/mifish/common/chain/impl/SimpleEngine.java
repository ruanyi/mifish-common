package com.mifish.common.chain.impl;

import com.mifish.common.chain.Chain;
import com.mifish.common.chain.Engine;
import com.mifish.common.profiler.MethodProfiler;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-06 13:47
 */
public class SimpleEngine<P> implements Engine<P> {

    /**
     * digest
     *
     * @param param
     * @param chain
     * @param <R>
     * @return
     */
    @Override
    public <R> R digest(P param, Chain<P, R> chain) {
        try {
            MethodProfiler.enter("engine begin");
            return chain.execute(param);
        } finally {
            MethodProfiler.release();
            System.out.println(MethodProfiler.dump());
            MethodProfiler.reset();
        }
    }
}
