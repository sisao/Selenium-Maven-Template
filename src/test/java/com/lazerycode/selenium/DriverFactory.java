package com.lazerycode.selenium;

import com.lazerycode.selenium.config.WebDriverThread;
import com.lazerycode.selenium.listeners.ScreenshotRule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DriverFactory {

    @Rule
    public ScreenshotRule screenshotRule = new ScreenshotRule();

    private static List<WebDriverThread> webDriverThreadPool = Collections.synchronizedList(new ArrayList<WebDriverThread>());
    private static ThreadLocal<WebDriverThread> driverThread;

    @BeforeClass
    public static void instantiateDriverObject() {
        driverThread = new ThreadLocal<WebDriverThread>() {
            @Override
            protected WebDriverThread initialValue() {
                WebDriverThread webDriverThread = new WebDriverThread();
                webDriverThreadPool.add(webDriverThread);
                return webDriverThread;
            }
        };
    }

    public static WebDriver getDriver() throws Exception {
        return driverThread.get().getDriver();
    }

    @After
    public void clearCookies() throws Exception {
        getDriver().manage().deleteAllCookies();
    }

    @AfterClass
    public static void closeDriverObjects() {
        for (WebDriverThread webDriverThread : webDriverThreadPool) {
            webDriverThread.quitDriver();
        }
    }
}