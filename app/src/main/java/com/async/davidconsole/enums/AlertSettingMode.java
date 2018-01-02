package com.async.davidconsole.enums;

/**
 * author: Ling Lin
 * created on: 2017/12/26 16:29
 * email: 10525677@qq.com
 * description:
 */
public enum AlertSettingMode {
    TEMP_DEVH("TEMP.DEVH", 0), O2_DEVH("O2.DEVH", 1), HUM_DEVH("HUM.DEVH", 2),

    Heat_Air("Heat.Air", 4), Heat_Flow("Heat.Flow", 5),

    SPO2_OVH("SPO2.OVH", 6), SPO2_OVL("SPO2.OVL", 7),
    PR_OVH("PR.OVH", 8), PR_OVL("PR.OVL", 9);

    private final String name;
    private final int index;

    AlertSettingMode(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static AlertSettingMode getMode(String alertModeString) throws Exception {
        for (AlertSettingMode mode : values()) {
            if (mode.getName().equals(alertModeString)) {
                return mode;
            }
        }
        throw new Exception("Illegal alertString mode: " + alertModeString);
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
