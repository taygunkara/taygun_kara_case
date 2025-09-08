package com.insider.driver;

import com.insider.enums.BrowserType;
import com.insider.utils.ConfigReader;
import com.insider.utils.WaitUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DriverManager {

    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();


    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            WebDriver driver;

            BrowserType browserType = BrowserType.valueOf(ConfigReader.getProperty("browser.type").toUpperCase());
            logger.info("Initializing WebDriver for browser type: {}", browserType);

            switch (browserType) {
                case FIREFOX:
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    logger.debug("FirefoxDriver initialized.");
                    break;
                case EDGE:
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    logger.debug("EdgeDriver initialized.");
                    break;
                case CHROME:
                default:
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    logger.debug("ChromeDriver initialized.");
                    break;
            }

            driver.manage().window().maximize();
            logger.debug("Browser window maximized.");

            driverThreadLocal.set(driver);
            logger.info("WebDriver successfully created and set for the current thread.");
        }
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        if (driverThreadLocal.get() != null) {
            logger.info("Quitting WebDriver for the current thread.");
            WaitUtils.resetWait();
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
            logger.debug("WebDriver successfully quit and removed from ThreadLocal.");
        } else {
            logger.warn("No WebDriver instance found for the current thread to quit.");
        }
    }
}