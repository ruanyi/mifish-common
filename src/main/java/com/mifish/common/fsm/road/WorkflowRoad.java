package com.mifish.common.fsm.road;

import com.mifish.common.fsm.Step;
import com.mifish.common.fsm.exception.StepNotFoundException;
import com.mifish.common.fsm.model.Society;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-10 00:07
 */
public class WorkflowRoad extends AbstractRoad {

    /***logger*/
    private static final Logger logger = LoggerFactory.getLogger(WorkflowRoad.class);

    /**
     * WorkflowRoad
     *
     * @param topic
     * @param firstStep
     */
    public WorkflowRoad(String topic, String firstStep) {
        super(topic, firstStep);
    }

    /**
     * walk
     *
     * @param society
     * @return
     * @throws Exception
     */
    @Override
    public int walk(Society society) throws Exception {
        long start = System.currentTimeMillis();
        int result = FAILURE;
        try {
            //开始走第一步
            doStep(getFirstStep(), society);
            //如果没有抛出异常，则证明是成功的
            result = SUCCESS;
            return result;
        } finally {
            if (logger.isInfoEnabled()) {
                logger.info("WorkflowRoad," + getTopic() + "," + getFirstStep() + "result:" + result + ",Society:" +
                        society + "," + (System.currentTimeMillis() - start));
            }
        }
    }

    /**
     * 递归走好漫漫人生路的每一步
     * <p>
     * 假如其中一步是：FINISH，则证明这条路已经走完了,暂时先不具备路由功能，后续在看
     *
     * @param stepName
     * @param society
     */
    private void doStep(String stepName, Society society) {
        if (StringUtils.equals(stepName, Step.FINISH)) {
            return;
        }
        Step step = getStep(stepName);
        if (step != null) {
            society.addWalkStepName(stepName);
            String nextStepName = step.step(society);
            //走好下一步
            doStep(nextStepName, society);
        } else {
            throw new StepNotFoundException(getTopic() + "," + getFirstStep() + "," + stepName + " is not exist");
        }
    }
}
