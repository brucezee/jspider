package com.brucezee.jspider.selenium;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.selenium.common.enums.DriverType;

/**
 * Created by brucezee on 2017/1/11.
 */
public class DefaultWebDriverChooser implements WebDriverChooser {
    private DriverType driverType;

    public DefaultWebDriverChooser(DriverType driverType) {
        this.driverType = driverType;
    }

    @Override
    public DriverType choose(Request request) {
        return driverType;
    }
}
