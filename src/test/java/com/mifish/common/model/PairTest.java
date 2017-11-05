package com.mifish.common.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-05 22:37
 */
public class PairTest {

    @Test
    public void testPair() {
        Pair<String, String> pair = Pair.of("k", "a");
        Assert.assertNotNull(pair.getFirst(), "first get k");
        Assert.assertNotNull(pair.getSecond(), "second get a");
    }

    @Test
    public void testPair2() {

    }
}
