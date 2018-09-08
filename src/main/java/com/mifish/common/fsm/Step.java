package com.mifish.common.fsm;

import com.mifish.common.fsm.model.Society;

/**
 * 约定：
 * 1、所有step必须是无状态的。
 * 2、step与step之间的数据传递是通过：Society里的attributes。记住：这种传递是临时的
 * 3、当整体流程已经结束时，返回的NEXT_STEP 是：FINISH
 * 4、每一个step类：最好不好超过300行，里面的step方法，不超过：50行。假如超过了，则建议：重新思考step拆分是否合理
 * 5、抽象跟分解：一个工作流的步骤整合和拆分，很关键，否则，这套框架又会废了
 * 6、目前step与step之间暂不支持多线程跑，后续根据实际情况再进行考虑
 * 7、每个step必须具有唯一的名称
 * 8、一个step尽量少些 if else,如果场景不一样，那最好新写一个step，后续可以把废弃的step直接删除，避免if else过多，看代码时头都疼死了
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
