package com.mifish.common.fsm.road;

import com.mifish.common.fsm.Road;
import com.mifish.common.fsm.Step;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-10 00:05
 */
public abstract class AbstractRoad implements Road {
    
    /**
     * 代表这条路的主题:topic
     */
    private String topic;

    /**
     * firstStep
     * 这条路的第一步，然后，由这一步继续往下走
     */
    private String firstStep;

    /**
     * 这条路包含的所有步
     * 包含步的名字，跟具体如何走
     */
    private Map<String, Step> steps = new HashMap<>();

    /**
     * AbstractRoad
     *
     * @param topic
     * @param firstStep
     */
    public AbstractRoad(String topic, String firstStep) {
        checkArgument(StringUtils.isNotBlank(topic), "topic cannot be blank in Road");
        checkArgument(StringUtils.isNotBlank(firstStep), "firstStep cannot be blank in Road");
        this.topic = topic;
        this.firstStep = firstStep;
    }

    /**
     * getTopic
     *
     * @return
     */
    @Override
    public String getTopic() {
        return this.topic;
    }

    /**
     * getStep
     *
     * @param name
     * @return
     */
    @Override
    public Step getStep(String name) {
        return this.steps.get(name);
    }

    /**
     * getFirstStep
     *
     * @return
     */
    @Override
    public String getFirstStep() {
        return this.firstStep;
    }

    /**
     * setSteps
     *
     * @param steps
     */
    @Override
    public void setSteps(List<Step> steps) {
        if (steps == null || steps.isEmpty()) {
            return;
        }
        for (Step step : steps) {
            this.steps.put(step.getName(), step);
        }
    }
}
