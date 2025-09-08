package com.insider.pages;

import org.openqa.selenium.By;


public class QualityAssurancePage extends BasePage {
    private final By seeAllQaJobsButton = By.cssSelector("a[href*='/open-positions/?department=qualityassurance']");

    public void clickSeeAllQaJobs() {
        logger.info("STEP: Click 'See all QA jobs' to load available positions");
        try {
            clickElement(seeAllQaJobsButton);
        } catch (Exception e) {
            logger.debug("'See all QA jobs' button not found or not clickable. Assuming jobs are already displayed.", e);
        }
    }
}
