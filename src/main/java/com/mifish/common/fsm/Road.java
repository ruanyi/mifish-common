package com.mifish.common.fsm;

import com.mifish.common.fsm.model.Society;

import java.util.List;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-09 23:59
 */
public interface Road {

    int SUCCESS = 0;

    int FAILURE = -1;

    /**
     * getTopic
     *
     * @return
     */
    String getTopic();

    /**
     * getStep
     *
     * @param name
     * @return
     */
    Step getStep(String name);

    /**
     * getFirstStep
     *
     * @return
     */
    String getFirstStep();

    /**
     * 真正开始走这条路，假如执行成功了，则返回：0，否则返回-1
     *
     * @param society
     * @return int
     * @throws Exception
     */
    int walk(Society society) throws Exception;

    /**
     * 实例化bean时，调用这个方法设置具体的每一步，只允许调用一次
     * 例如：假如通过spring来实例化road，则赋值：steps，注意是：list格式的
     *
     * @param steps
     */
    void setSteps(List<Step> steps);


}
