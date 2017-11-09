package com.mifish.common.chain.impl;

import com.mifish.common.chain.Chain;
import com.mifish.common.chain.Engine;
import com.mifish.common.profiler.MethodProfiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-06 13:47
 */
public class SimpleEngine<P> implements Engine<P> {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(SimpleEngine.class);

    /**
     * digest
     *
     * @param param
     * @param chain
     * @param <R>
     * @return R
     */
    @Override
    public <R> R digest(P param, Chain<P, R> chain) {
        try {
            MethodProfiler.enter("engine begin");
            return chain.execute(param);
        } finally {
            MethodProfiler.release();
            if (logger.isDebugEnabled()) {
                logger.debug(MethodProfiler.dump("SimpleEngine"));
            }
            MethodProfiler.reset();
        }
    }
}
