package com.mifish.common.chain.impl;

import com.google.common.collect.Lists;
import com.mifish.common.chain.Chain;
import com.mifish.common.chain.Node;
import com.mifish.common.chain.Processor;

import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 20:04
 */
public class SimpleChain<P> implements Chain<P> {

    /***iterator*/
    private Iterator<Node<P>> iterator;

    /***processor*/
    private Processor<P> processor;

    /**
     * SimpleChain
     *
     * @param nodes
     * @param processor
     */
    public SimpleChain(List<Node<P>> nodes, Processor<P> processor) {
        checkArgument(nodes != null, "nodes cannot be null in SimpleChain");
        this.iterator = nodes.iterator();
        this.processor = processor;
    }

    /**
     * SimpleChain
     *
     * @param nodes
     */
    public SimpleChain(List<Node<P>> nodes) {
        this(nodes, null);
    }

    /**
     * execute
     *
     * @param param
     */
    @Override
    public void execute(P param) {
        if (this.iterator.hasNext()) {
            Node<P> node = this.iterator.next();
            node.doAction(param, this);
        } else {
            if (this.processor != null) {
                this.processor.process(param);
            }
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
    public static <T> Chain<T> buildSimpleChain(Processor<T> processor, Node<T>... nodes) {
        return new SimpleChain<>(Lists.newArrayList(nodes), processor);
    }

    /**
     * buildSimpleChain
     *
     * @param nodes
     * @param <T>
     * @return
     */
    public static <T> Chain<T> buildSimpleChain(Node<T>... nodes) {
        return new SimpleChain<>(Lists.newArrayList(nodes));
    }
}
