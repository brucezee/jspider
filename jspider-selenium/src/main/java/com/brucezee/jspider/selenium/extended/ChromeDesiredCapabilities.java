package com.brucezee.jspider.selenium.extended;

import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brucezee on 2017/1/12.
 */
public class ChromeDesiredCapabilities extends DesiredCapabilities {
    public static final String SETTING_VALUES_PREFIX = "profile.default_content_setting_values.";
    public static final String SETTING_VALUES_IMAGES = SETTING_VALUES_PREFIX + "images";
    public static final String SETTING_VALUES_JAVASCRIPT = SETTING_VALUES_PREFIX + "javascript";
    public static final String SETTING_VALUES_FLASH = SETTING_VALUES_PREFIX + "plugins";
    public static final String SETTING_VALUES_AUTOMATIC_DOWNLOADS = SETTING_VALUES_PREFIX + "automatic_downloads";
    public static final String SETTING_VALUES_GEOLOCATION = SETTING_VALUES_PREFIX + "geolocation";

    private Map<String, Object> prefs;
    private ChromeOptions options;

    public ChromeDesiredCapabilities() {
        super(BrowserType.ANDROID, "", Platform.ANDROID);

        prefs = new HashMap<String, Object>();
        options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        setCapability(ChromeOptions.CAPABILITY, options);
    }

    public static ChromeDesiredCapabilities create() {
        return new ChromeDesiredCapabilities();
    }

    public Map<String, Object> getPrefs() {
        return prefs;
    }

    public ChromeOptions getOptions() {
        return options;
    }

    public void setJavascriptEnabled(boolean enabled) {
        super.setJavascriptEnabled(enabled);
        if (!enabled) {
            prefs.put(SETTING_VALUES_JAVASCRIPT, 2);
        } else {
            prefs.remove(SETTING_VALUES_JAVASCRIPT);
        }
    }

    public void setImagesEnabled(boolean enabled) {
        if (!enabled) {
            prefs.put(SETTING_VALUES_IMAGES, 2);
        } else {
            prefs.remove(SETTING_VALUES_IMAGES);
        }
    }

    public void setFlashEnabled(boolean enabled) {
        if (!enabled) {
            prefs.put(SETTING_VALUES_FLASH, 2);
        } else {
            prefs.remove(SETTING_VALUES_FLASH);
        }
    }

    public void setAutomaticDownloadsEnabled(boolean enabled) {
        if (!enabled) {
            prefs.put(SETTING_VALUES_AUTOMATIC_DOWNLOADS, 2);
        } else {
            prefs.remove(SETTING_VALUES_AUTOMATIC_DOWNLOADS);
        }
    }

    public void setGeoLocationEnabled(boolean enabled) {
        if (!enabled) {
            prefs.put(SETTING_VALUES_GEOLOCATION, 2);
        } else {
            prefs.remove(SETTING_VALUES_GEOLOCATION);
        }
    }

    public void setUserDataDir(String path) {
        options.addArguments("user-data-dir="+path);
    }

    public void setProxy(Proxy proxy) {
        setCapability("proxy", proxy);
    }

}
