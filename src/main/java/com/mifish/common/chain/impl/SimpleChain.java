package com.mifish.common.chain.impl;

import com.google.common.collect.Lists;
import com.mifish.common.chain.Chain;
import com.mifish.common.chain.Node;
import com.mifish.common.chain.Processor;
import com.mifish.common.profiler.MethodProfiler;

import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 20:04
 */
public class SimpleChain<P, R> implements Chain<P, R> {

    /***iterator*/
    private Iterator<Node<P, R>> iterator;

    /***processor*/
    private Processor<P, R> processor;

    /**
     * SimpleChain
     *
     * @param nodes
     * @param processor
     */
    public SimpleChain(List<Node<P, R>> nodes, Processor<P, R> processor) {
        checkArgument(nodes != null, "nodes cannot be null in SimpleChain");
        this.iterator = nodes.iterator();
        this.processor = processor;
    }

    /**
     * SimpleChain
     *
     * @param nodes
     */
    public SimpleChain(List<Node<P, R>> nodes) {
        this(nodes, null);
    }

    /**
     * execute
     *
     * @param param
     */
    @Override
    public R execute(P param) {
        if (this.iterator.hasNext()) {
            Node<P, R> node = this.iterator.next();
            return node.doAction(param, this);
        } else {
            if (this.processor != null) {
                try {
                    MethodProfiler.enter("processor:" + this.processor.getName() + "begin,Param:" + param);
                    return this.processor.process(param);
                } finally {
                    MethodProfiler.release();
                }
            }
            return null;
        }
    }

    /**
     * buildSimpleChain
     *
     * @param processor
     * @param nodes
     * @param <T>
     * @return
     */
    public static <T, S> Chain<T, S> buildSimpleChain(Processor<T, S> processor, Node<T, S>... nodes) {
        return new SimpleChain<>(Lists.newArrayList(nodes), processor);
    }

    /**
     * buildSimpleChain
     *
     * @param nodes
     * @param <T>
     * @return
     */
    public static <T, S> Chain<T, S> buildSimpleChain(Node<T, S>... nodes) {
        return new SimpleChain<>(Lists.newArrayList(nodes));
    }
}
