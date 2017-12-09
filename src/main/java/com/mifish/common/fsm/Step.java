package com.mifish.common.fsm;

import com.mifish.common.fsm.model.Society;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-09 23:59
 */
public interface Step {

    String FINISH = "FINISH";

    /**
     * 走这一步的路，走完后，顺便决策好下一步的名字
     * 如果返回：FINISH,则证明这条路已经走完
     * <p>
     * 目前暂时上一步跟下一步之间的路由选择功能，
     * 因此，先简单通过返回值，返回下一步的名字
     * 后续根据实际情况，是否需要有该功能
     *
     * @param society
     * @return
     */
    String step(Society society);

    /**
     * 名字必须唯一
     *
     * @return
     */
    String getName();


}
