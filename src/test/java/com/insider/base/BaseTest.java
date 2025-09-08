package com.insider.base;

import com.insider.driver.DriverManager;
import com.insider.pages.BasePage;
import com.insider.utils.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;

public abstract class BaseTest extends BasePage {

    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp(@Optional("default") String browser) {
        logger.info("Starting test setup for method: {}", getClass().getSimpleName());
        DriverManager.getDriver();
        logger.debug("WebDriver instance successfully initialized for the current thread.");
        String appUrl = ConfigReader.getProperty("app.url");
        DriverManager.getDriver().get(appUrl);
        logger.info("Navigated to the application at URL: {}", appUrl);
    }

    @AfterMethod
    public void tearDown() {
        logger.info("Tearing down test environment for method.");
        DriverManager.quitDriver();
        logger.debug("WebDriver instance shut down for the current thread.");
    }
}