package com.brucezee.jspider.selenium.common.enums;

/**
 * Created by brucezee on 2017/1/11.
 */
public enum DriverType {
    CHROME("chrome"),
    PHANTOMJS("phantomjs"),

    //暂时只支持两种
    ;

    private String name;

    DriverType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
