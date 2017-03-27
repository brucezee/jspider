package com.brucezee.jspider.selenium;

import com.google.common.collect.ImmutableMap;
import com.brucezee.jspider.SiteConfig;
import com.brucezee.jspider.selenium.common.enums.DriverType;
import com.brucezee.jspider.selenium.extended.ChromeDesiredCapabilities;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CommandInfo;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.internal.ApacheHttpClient;
import org.openqa.selenium.remote.internal.HttpClientFactory;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by brucezee on 2017/1/11.
 */
public class WebDriverFactory {
    public WebDriverEx createWebDriver(SiteConfig siteConfig, DriverConfig driverConfig, DriverType driverType) throws IOException {
        if (driverType == DriverType.CHROME) {
            return createChromeWebDriver(siteConfig, driverConfig);
        }
        if (driverType == DriverType.PHANTOMJS) {
            return createPhantomJsWebDriver(siteConfig, driverConfig);
        }
        return null;
    }

    public WebDriverEx createChromeWebDriver(SiteConfig siteConfig, DriverConfig driverConfig) throws IOException {
        File driverFile = createDriverFile(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY);
        DesiredCapabilities desiredCapabilities = createChromeDesiredCapabilities(siteConfig, driverConfig);
        ChromeDriverService driverService = createChromeDriverService(siteConfig, driverConfig, driverFile);
        return createWebDriver(driverService, desiredCapabilities, siteConfig, driverConfig);
    }

    public WebDriverEx createWebDriver(DriverService driverService,
                                          DesiredCapabilities desiredCapabilities,
                                          SiteConfig siteConfig,
                                          DriverConfig driverConfig) throws IOException {
        driverService.start();

        //自定义HttpClientFactory用于设置命令超时时间
        ApacheHttpClient.Factory httpClientFactory = createHttpClientFactory(siteConfig, driverConfig);
        HttpCommandExecutor httpCommandExecutor = new HttpCommandExecutor(
                ImmutableMap.<String, CommandInfo>of(), driverService.getUrl(), httpClientFactory);
        WebDriverEx webDriver = new WebDriverEx(httpCommandExecutor, desiredCapabilities);
        webDriver.setDriverService(driverService);
        webDriver.setCreatedTime(new Date());

        webDriver.manage().timeouts().implicitlyWait(driverConfig.getImplicitlyWait(), TimeUnit.MILLISECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(driverConfig.getPageLoadTimeout(), TimeUnit.MILLISECONDS);
        webDriver.manage().timeouts().setScriptTimeout(driverConfig.getScriptTimeout(), TimeUnit.MILLISECONDS);

        return webDriver;
    }

    public ApacheHttpClient.Factory createHttpClientFactory(SiteConfig siteConfig, DriverConfig driverConfig) {
        return new ApacheHttpClient.Factory(new HttpClientFactory(
                siteConfig.getConnectionRequestTimeout(), siteConfig.getSocketTimeout()));
    }

    public DesiredCapabilities createChromeDesiredCapabilities(SiteConfig siteConfig, DriverConfig driverConfig) {
        ChromeDesiredCapabilities capabilities = ChromeDesiredCapabilities.create();

        capabilities.setImagesEnabled(false);
        capabilities.setFlashEnabled(false);
        capabilities.setAutomaticDownloadsEnabled(false);
        capabilities.setJavascriptEnabled(true);

        return capabilities;
    }

    public ChromeDriverService createChromeDriverService(SiteConfig siteConfig, DriverConfig driverConfig, File driverFile) {
        return createChromeDriverServiceBuilder(siteConfig, driverConfig, driverFile).build();
    }

    public ChromeDriverService.Builder createChromeDriverServiceBuilder(SiteConfig siteConfig, DriverConfig driverConfig, File driverFile) {
        ChromeDriverService.Builder serviceBuilder = new ChromeDriverService.Builder();

        serviceBuilder.usingDriverExecutable(driverFile);
        serviceBuilder.usingAnyFreePort();
//        serviceBuilder.usingPort(int)
//        serviceBuilder.withVerbose(boolean);
//        serviceBuilder.withLogFile(File)
//        serviceBuilder.withEnvironment(Map<String, String>)
//        serviceBuilder.withSilent(boolean)
//        serviceBuilder.withWhitelistedIps(String)

        return serviceBuilder;
    }


    public WebDriverEx createPhantomJsWebDriver(SiteConfig siteConfig, DriverConfig driverConfig) throws IOException {
        File driverFile = createDriverFile(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY);
        DesiredCapabilities desiredCapabilities = createPhantomJsDesiredCapabilities(siteConfig, driverConfig);
        PhantomJSDriverService driverService = createPhantomJsDriverService(siteConfig, driverConfig, driverFile);
        return createWebDriver(driverService, desiredCapabilities, siteConfig, driverConfig);
    }


    public DesiredCapabilities createPhantomJsDesiredCapabilities(SiteConfig siteConfig, DriverConfig driverConfig) {
        DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();

        capabilities.setJavascriptEnabled(true);

        return capabilities;
    }

    public PhantomJSDriverService createPhantomJsDriverService(SiteConfig siteConfig, DriverConfig driverConfig, File driverFile) {
        return createPhantomJsDriverServiceBuilder(siteConfig, driverConfig, driverFile).build();
    }

    public PhantomJSDriverService.Builder createPhantomJsDriverServiceBuilder(SiteConfig siteConfig, DriverConfig driverConfig, File driverFile) {
        PhantomJSDriverService.Builder serviceBuilder = new PhantomJSDriverService.Builder();

        serviceBuilder.usingPhantomJSExecutable(driverFile);
//        serviceBuilder.usingAnyFreePort();
//        serviceBuilder.withEnvironment(Map<String, String>);
//        serviceBuilder.withLogFile(File);
//        serviceBuilder.usingPort(int);
//
//        serviceBuilder.withProxy(Proxy);
//        serviceBuilder.usingGhostDriver(File);
//        serviceBuilder.usingCommandLineArguments(String[]);
//        serviceBuilder.usingGhostDriverCommandLineArguments(String[]);

        return serviceBuilder;
    }

    public File createDriverFile(String filePathPropertyName) {
        String driverFilePath = System.getProperty(filePathPropertyName);
        if (StringUtils.isBlank(driverFilePath)) {
            throw new IllegalArgumentException("System property '" + filePathPropertyName + "' is required! Use System.setProperty(...,...).");
        }

        File driverFile = new File(driverFilePath);
        if (!driverFile.exists()) {
            throw new IllegalArgumentException("File " + driverFilePath + " is not found!");
        }
        return driverFile;
    }
}
