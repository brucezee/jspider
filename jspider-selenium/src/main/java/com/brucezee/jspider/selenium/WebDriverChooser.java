package com.brucezee.jspider.selenium;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.selenium.common.enums.DriverType;

/**
 * WebDriver选择器
 * Created by brucezee on 2017/1/11.
 */
public interface WebDriverChooser {
    /**
     * 根据请求选择相应WebDriver的类型
     * @param request 请求
     * @return 请求对应WebDriver类型
     */
    public DriverType choose(Request request);
}
