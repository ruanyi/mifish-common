package com.mifish.common.chain;

import com.mifish.common.chain.impl.SimpleChain;
import com.mifish.common.chain.nodes.DummyNode;
import org.junit.Test;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 20:18
 */
public class SimpleChainTest {

    @Test
    public void testDummy() {
        Chain<String> chain = SimpleChain.buildSimpleChain(DummyNode.newInstance(), DummyNode.newInstance(),
                DummyNode.newInstance());
        chain.execute("1");
    }
}
