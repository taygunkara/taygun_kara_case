package com.insider.pages;

import com.insider.utils.WaitUtils;
import com.insider.driver.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.insider.utils.WaitUtils.waitForVisibility;

public class BasePage {

    protected WebDriver driver;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BasePage() {
        this.driver = DriverManager.getDriver();
        logger.debug("BasePage initialized with WebDriver instance.");
    }

    protected void clickElement(By locator) {
        logger.info("STEP: Click element {}", locator);
        try {
            WebElement element = WaitUtils.waitForClickability(locator);
            element.click();
            logger.debug("Clicked element normally: {}", locator);
        } catch (ElementClickInterceptedException e) {
            logger.warn("Standard click intercepted for {}. Retrying with JavaScript.", locator);
            try {
                WebElement element = WaitUtils.waitForClickability(locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                logger.debug("Clicked element with JavaScript: {}", locator);
            } catch (Exception jsClickException) {
                logger.error("Both standard and JavaScript click failed for {}. Error: {}", locator, jsClickException.getMessage());
                throw jsClickException;
            }
        } catch (Exception e) {
            logger.error("Click failed for {}. Error: {}", locator, e.getMessage());
            throw e;
        }
    }

    protected void hoverOverElement(By locator) {
        logger.info("STEP: Hover over element {}", locator);
        WebElement element = waitForVisibility(locator);
        new Actions(driver).moveToElement(element).perform();
        logger.debug("Hovered successfully: {}", locator);
    }

    protected void hoverAndClickElement(By hoverLocator, By clickLocator) {
        logger.info("STEP: Hover {} then click {}", hoverLocator, clickLocator);
        hoverOverElement(hoverLocator);
        clickElement(clickLocator);
    }

    protected List<WebElement> getElements(By locator, String logName) {
        logger.info("STEP: Get elements for {}", logName);
        try {
            List<WebElement> elements = WaitUtils.waitForVisibilityOfAllElements(locator);
            logger.debug("Found {} {} element(s)", elements.size(), logName);
            return elements;
        } catch (Exception e) {
            logger.warn("Failed to get elements {}. Error: {}", logName, e.getMessage());
            return List.of();
        }
    }

    protected WebElement getElement(By locator, String logName) {
        logger.info("STEP: Get single element for '{}'", logName);
        try {
            WebElement element = WaitUtils.waitForVisibility(locator);
            logger.debug("Successfully found element for '{}' using locator: {}", logName, locator);
            return element;
        } catch (Exception e) {
            logger.warn("Failed to get element for '{}'. Locator: {}. Error: {}", logName, locator, e.getMessage());
            return null;
        }
    }

    protected List<String> getTextsFromElements(By locator, String logName) {
        logger.info("STEP: Get texts of {} from elements {}", logName, locator);
        List<WebElement> elements = getElements(locator, logName);

        List<String> texts = elements.stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .collect(Collectors.toList());

        logger.debug("Collected {} text(s) for '{}': {}", texts.size(), logName, texts);
        return texts;
    }

    protected List<String> getTextsAfterHover(By hoverLocator, By targetLocator, String logName) {
        hoverOverElement(hoverLocator);
        return getTextsFromElements(targetLocator, logName);
    }


    protected boolean isElementDisplayed(By locator, String logName) {
        logger.info("STEP: Check if '{}' is displayed", logName);
        boolean isDisplayed = getElement(locator, logName) != null;
        logger.debug("'{}' display status: {}", logName, isDisplayed);
        return isDisplayed;
    }


    protected boolean areElementsDisplayed(By locator, String logName) {
        WaitUtils.waitForVisibilityOfAllElements(locator);
        List<WebElement> elements = getElements(locator, logName);
        boolean result = !elements.isEmpty();
        logger.debug("{} displayed: {}", logName, result);
        return result;
    }


    protected String getCurrentPageUrl() {
        logger.info("STEP: Get current page URL");
        String url = driver.getCurrentUrl();
        logger.debug("Current URL is: {}", url);
        return url;
    }

    protected void scrollToElementInContainer(By containerLocator, By elementLocator) {
        logger.info("STEP: Scroll to element {} inside container {}", elementLocator, containerLocator);
        WebElement container = waitForVisibility(containerLocator);
        WebElement element = waitForVisibility(elementLocator);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollTop = arguments[1].offsetTop - arguments[0].offsetHeight / 2;",
                container, element
        );

        logger.debug("Scrolled to element {} in container {}", elementLocator, containerLocator);
    }
}
