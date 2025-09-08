package com.insider.pages;

import org.openqa.selenium.By;

public class CareersPage extends BasePage {

    public final By teamsSection = By.xpath("//h3[normalize-space(.)='Find your calling']");
    public final By locationsSection = By.xpath("//h3[normalize-space(.)='Our Locations']");
    public final By lifeAtInsiderSection = By.xpath("//h2[normalize-space(.)='Life at Insider']");
    private final By seeAllTeamsButton = By.xpath("//a[normalize-space(.)='See all teams']");
    private final By qaSection = By.xpath("//h3[normalize-space(.)='Quality Assurance']");

    public boolean isSectionDisplayed(By sectionLocator, String sectionName) {
        logger.info("STEP: Verify '{}' section is displayed on Careers page", sectionName);
        return isElementDisplayed(sectionLocator, sectionName);
    }

    public QualityAssurancePage goToQaJobsPage() {
        logger.info("STEP: Navigate to QA job listings page");
        clickElement(seeAllTeamsButton);
        clickElement(qaSection);
        return new QualityAssurancePage();
    }
}
