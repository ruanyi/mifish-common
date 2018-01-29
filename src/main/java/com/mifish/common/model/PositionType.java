package com.mifish.common.model;

import org.apache.commons.lang.StringUtils;

/**
 * Description:
 *
 * @author: rls
 * Date: 2018-01-15 10:18
 */
public enum PositionType {

    U("U", "上面"),

    D("D", "下面"),

    R("R", "右边"),

    L("L", "左边");

    /**
     * code
     */
    private String code;

    /**
     * desc
     */
    private String desc;

    /**
     * PositionType
     *
     * @param code
     * @param desc
     */
    PositionType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * getCode
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * getDesc
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     * of
     *
     * @param code
     * @return
     */
    public static PositionType of(String code) {
        for (PositionType pt : values()) {
            if (StringUtils.equalsIgnoreCase(pt.getCode(), code)) {
                return pt;
            }
        }
        return null;
    }
}
