package com.insider.utils;

import com.insider.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class WaitUtils {

    private static final Logger logger = LoggerFactory.getLogger(WaitUtils.class);
    private static final int DEFAULT_TIMEOUT_SECONDS = ConfigReader.getIntProperty("wait.timeout.seconds");
    private static final ThreadLocal<WebDriverWait> waitThreadLocal = new ThreadLocal<>();

    private static WebDriverWait getWait() {
        if (waitThreadLocal.get() == null) {
            WebDriver driver = DriverManager.getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
            waitThreadLocal.set(wait);
            logger.debug("New WebDriverWait initialized for this thread with timeout {} seconds", DEFAULT_TIMEOUT_SECONDS);
        }
        return waitThreadLocal.get();
    }

    public static void resetWait() {
        waitThreadLocal.remove();
        logger.debug("WebDriverWait instance reset for current thread.");
    }

    private static <T> T waitWithRetry(ExpectedCondition<T> condition, String description) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                return getWait().until(condition);
            } catch (StaleElementReferenceException e) {
                attempts++;
                logger.warn("StaleElementReferenceException on attempt {} while waiting for {}. Retrying...", attempts, description);
            }
        }
        throw new RuntimeException("Element became stale after multiple attempts: " + description);
    }

    public static WebElement waitForVisibility(By by) {
        logger.info("STEP: Wait for element '{}' to be visible", by);
        return waitWithRetry(ExpectedConditions.visibilityOfElementLocated(by), "visibility of " + by);
    }

    public static List<WebElement> waitForVisibilityOfAllElements(By by) {
        logger.info("STEP: Wait for all elements '{}' to be visible", by);
        return waitWithRetry(ExpectedConditions.visibilityOfAllElementsLocatedBy(by), "visibility of all elements " + by);
    }

    public static WebElement waitForClickability(By by) {
        logger.info("STEP: Wait for element '{}' to be clickable", by);
        return waitWithRetry(ExpectedConditions.elementToBeClickable(by), "clickability of " + by);
    }

    public static void waitForNumberOfWindowsToBe(int numberOfWindows) {
        logger.info("STEP: Wait until number of windows/tabs is {}", numberOfWindows);
        try {
            getWait().until(driver -> driver.getWindowHandles().size() == numberOfWindows);
            logger.debug("Number of windows/tabs reached {}", numberOfWindows);
        } catch (Exception e) {
            logger.error("Number of windows/tabs did not reach {} within {} seconds. Error: {}", numberOfWindows, DEFAULT_TIMEOUT_SECONDS, e.getMessage());
            throw new RuntimeException("Failed to wait for number of windows: " + numberOfWindows, e);
        }
    }
}
