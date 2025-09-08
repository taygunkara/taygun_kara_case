package com.insider.pages;

import org.openqa.selenium.By;

public class HomePage extends BasePage {

    private final By companyMenu = By.xpath("//a[normalize-space(.)='Company']");
    private final By careersLink = By.xpath("//a[text()='Careers']");
    private final By cookieBanner = By.cssSelector("#wt-cli-cookie-banner");
    private final By acceptCookiesButton = By.cssSelector("#wt-cli-accept-all-btn");
    private final By popupBanner = By.cssSelector("#ins-responsive-banner");
    private final By closePopupBannerButton = By.cssSelector(".ins-close-button");

    public void acceptCookiesIfVisible() {
        logger.info("STEP: Handle cookie consent banner if visible");
        try {
            if (isElementDisplayed(cookieBanner, "Cookie Consent Banner")) {
                clickElement(acceptCookiesButton);
                logger.debug("Cookie banner accepted and closed.");
            } else {
                logger.debug("No cookie banner detected or already dismissed.");
            }
        } catch (Exception e) {
            logger.warn("Issue while trying to close cookie banner. Error: {}", e.getMessage());
        }
    }

    public void closePopupBannerIfVisible() {
        logger.info("STEP: Handle popup banner if visible");
        try {
            if (isElementDisplayed(popupBanner, "Promotional Popup Banner")) {
                clickElement(closePopupBannerButton);
                logger.debug("Popup banner accepted and closed.");
            } else {
                logger.debug("No popup banner detected or already dismissed.");
            }
        } catch (Exception e) {
            logger.warn("Issue while trying to close popup banner. Error: {}", e.getMessage());
        }
    }

    public void closeInitialPopups() {
        logger.info("STEP: Close initial popups/modals on Home Page");
        //closePopupBannerIfVisible();
        acceptCookiesIfVisible();
    }

    public CareersPage goToCareersPage() {
        logger.info("STEP: Navigate to Careers Page via 'Company' → 'Careers'");
        hoverAndClickElement(companyMenu, careersLink);
        return new CareersPage();
    }
}
