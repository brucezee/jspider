package com.brucezee.jspider.selenium;

import com.google.common.collect.ImmutableMap;
import com.brucezee.jspider.selenium.common.enums.DriverType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by brucezee on 2017/1/11.
 */
public class WebDriverEx extends RemoteWebDriver {
    private DriverService driverService;
    private DriverType driverType;
    private Date createdTime;

    public WebDriverEx() {
        super();
    }

    public WebDriverEx(CommandExecutor executor, Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(executor, desiredCapabilities, requiredCapabilities);
    }

    public WebDriverEx(CommandExecutor executor, Capabilities desiredCapabilities) {
        super(executor, desiredCapabilities);
    }

    public WebDriverEx(Capabilities desiredCapabilities) {
        super(desiredCapabilities);
    }

    public WebDriverEx(URL remoteAddress, Capabilities desiredCapabilities, Capabilities requiredCapabilities) {
        super(remoteAddress, desiredCapabilities, requiredCapabilities);
    }

    public WebDriverEx(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }

    public DriverService getDriverService() {
        return driverService;
    }

    public void setDriverService(DriverService driverService) {
        this.driverService = driverService;
    }

    public DriverType getDriverType() {
        return driverType;
    }

    public void setDriverType(DriverType driverType) {
        this.driverType = driverType;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void shutdown() {
        this.quit();

        if (driverService != null) {
            driverService.stop();
        }
    }

    /**
     * 等待超时毫秒数
     */
    private static final int DEFAULT_TIME_OUT_IN_MILLIS = 45*1000;
    /**
     * 请求返回后默认延迟毫秒数
     */
    private static final int DEFAULT_DELAYED_MILLIS = 1*1000;

    /**
     * 根据标题中包含某个子串来等待
     * @param title 标题子串
     */
    public void waitWithTitle(final String title) {
        waitWithTitle(title, DEFAULT_TIME_OUT_IN_MILLIS);
    }

    /**
     * 根据标题中包含某个子串来等待
     * @param title 标题子串
     * @param timeOutInMillis 超时时间毫秒数
     */
    public void waitWithTitle(final String title, int timeOutInMillis) {
        (new WebDriverWait(this, timeOutInMillis/1000)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                String t = d.getTitle();
                if (StringUtils.isEmpty(title)) {
                    //等到有title就停止
                    return StringUtils.isNotBlank(t);
                }
                return t != null && t.contains(title);
            }
        });
    }

    /**
     * 根据标题中包含某个子串来等待并且延迟一段时间
     * @param title 标题子串
     * @param delayedMillis 延迟毫秒数
     */
    public void waitWithTitleAndDelayed(final String title, int delayedMillis) {
        waitWithTitleAndDelayed(title, DEFAULT_TIME_OUT_IN_MILLIS, delayedMillis);
    }

    /**
     * 根据标题中包含某个子串来等待并且延迟一段时间
     * @param title 标题子串
     */
    public void waitWithTitleAndDelayed(final String title) {
        waitWithTitleAndDelayed(title, DEFAULT_TIME_OUT_IN_MILLIS, DEFAULT_DELAYED_MILLIS);
    }

    /**
     * 根据标题中包含某个子串来等待
     * @param title 标题子串
     * @param timeOutInMillis 超时时间毫秒数
     * @param delayedMillis 延迟毫秒数
     */
    public void waitWithTitleAndDelayed(final String title, int timeOutInMillis, int delayedMillis) {
        (new WebDriverWait(this, timeOutInMillis/1000)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                String t = d.getTitle();
                if (StringUtils.isEmpty(title)) {
                    //等到有title就停止
                    return StringUtils.isNotBlank(t);
                }
                return t != null && t.contains(title);
            }
        });
        if (delayedMillis > 0) {
            sleep(delayedMillis);
        }
    }

    /**
     * 根据内容中包含某个子串来等待
     * @param content 指定的某个子串
     */
    public void waitWithContent(final String content) {
        waitWithContent(content, DEFAULT_TIME_OUT_IN_MILLIS);
    }

    /**
     * 根据内容中包含某个子串来等待
     * @param content 内容子串
     * @param timeOutInMillis 超时时间毫秒数
     */
    public void waitWithContent(final String content, int timeOutInMillis) {
        (new WebDriverWait(this, timeOutInMillis/1000)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                String html = d.getPageSource();
                if (StringUtils.isEmpty(content)) {
                    //等到有内容就停止
                    return StringUtils.isNotBlank(html);
                }
                return html != null && html.contains(content);
            }
        });
    }

    /**
     * 根据内容中包含某个子串来等待并且延迟一段时间
     * @param content 指定的某个子串
     */
    public void waitWithContentAndDelayed(final String content) {
        waitWithContentAndDelayed(content, DEFAULT_TIME_OUT_IN_MILLIS, DEFAULT_DELAYED_MILLIS);
    }

    /**
     * 根据内容中包含某个子串来等待并且延迟一段时间
     * @param content 指定的某个子串
     * @param delayedMillis 延迟毫秒数
     */
    public void waitWithContentAndDelayed(final String content, int delayedMillis) {
        waitWithContentAndDelayed(content, DEFAULT_TIME_OUT_IN_MILLIS, delayedMillis);
    }

    /**
     * 根据内容中包含某个子串来等待
     * @param content 内容子串
     * @param timeOutInMillis 超时时间毫秒数
     * @param delayedMillis 延迟毫秒数
     */
    public void waitWithContentAndDelayed(final String content, int timeOutInMillis, int delayedMillis) {
        (new WebDriverWait(this, timeOutInMillis/1000)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                String html = d.getPageSource();
                if (StringUtils.isEmpty(content)) {
                    //等到有内容就停止
                    return StringUtils.isNotBlank(html);
                }
                return html != null && html.contains(content);
            }
        });
        if (delayedMillis > 0) {
            sleep(delayedMillis);
        }
    }

    public void getWithTitle(String url, String title) {
        getWithTitle(url, title, DEFAULT_TIME_OUT_IN_MILLIS);
    }
    public void getWithTitle(String url, String title, int timeOutInMillis) {
        super.get(url);
        waitWithTitle(title, timeOutInMillis);
    }
    public void getWithTitleAndDelayed(String url, String title, int delayedMillis) {
        getWithTitleAndDelayed(url, title, DEFAULT_TIME_OUT_IN_MILLIS, delayedMillis);
    }
    public void getWithTitleAndDelayed(String url, String title) {
        getWithTitleAndDelayed(url, title, DEFAULT_TIME_OUT_IN_MILLIS, DEFAULT_DELAYED_MILLIS);
    }
    public void getWithTitleAndDelayed(String url, String title, int timeOutInMillis, int delayedMillis) {
        super.get(url);
        waitWithTitleAndDelayed(title, timeOutInMillis, delayedMillis);
    }
    public void getWithContent(String url, String content) {
        getWithContent(url, content, DEFAULT_TIME_OUT_IN_MILLIS);
    }
    public void getWithContent(String url, String content, int timeOutInMillis) {
        super.get(url);
        waitWithContent(content, timeOutInMillis);
    }
    public void getWithContentAndDelayed(String url, String content) {
        getWithContentAndDelayed(url, content, DEFAULT_TIME_OUT_IN_MILLIS, DEFAULT_TIME_OUT_IN_MILLIS);
    }
    public void getWithContentAndDelayed(String url, String content, int delayedMillis) {
        getWithContentAndDelayed(url, content, DEFAULT_TIME_OUT_IN_MILLIS, delayedMillis);
    }
    public void getWithContentAndDelayed(String url, String content, int timeOutInMillis, int delayedMillis) {
        super.get(url);
        waitWithContentAndDelayed(content, timeOutInMillis, delayedMillis);
    }


    public void waitUntil(int timeOutInMillis, ExpectedCondition expectedCondition) {
        waitUntil(timeOutInMillis, 0, expectedCondition);
    }
    public void waitUntil(int timeOutInMillis, int delayedMillis, ExpectedCondition expectedCondition) {
        (new WebDriverWait(this, timeOutInMillis/1000)).until(expectedCondition);
        if (delayedMillis > 0) {
            sleep(delayedMillis);
        }
    }

    @Override
    public WebElement findElement(By by) {
        try {
            return super.findElement(by);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        try {
            return super.findElements(by);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected WebElement findElement(String by, String using) {
        try {
            return super.findElement(by, using);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected List<WebElement> findElements(String by, String using) {
        try {
            return super.findElements(by, using);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }

    public Response sendGet(String url) {
        return execute(DriverCommand.GET, ImmutableMap.of("url", url));
    }

    public Response sendPost(String url) {
        return execute("post", ImmutableMap.of("url", url));
    }
}
