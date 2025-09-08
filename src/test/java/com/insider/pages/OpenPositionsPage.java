package com.insider.pages;

import com.insider.utils.WaitUtils;
import org.openqa.selenium.By;

import java.util.List;

public class OpenPositionsPage extends BasePage{

    private final By locationFilter = By.cssSelector("#select2-filter-by-location-container");
    private final By departmentFilter = By.cssSelector("#select2-filter-by-department-container");
    private final By jobsList = By.cssSelector(".position-list");
    private final By positionTitle = By.cssSelector("#jobs-list .position-list-item:first-child .position-title");
    private final By positionDepartment = By.cssSelector("#jobs-list .position-list-item:first-child .position-department");
    private final By positionLocation = By.cssSelector("#jobs-list .position-list-item:first-child .position-location");
    private final By firstJobViewRoleButton = By.xpath("(//a[text()='View Role'])[1]");
    private final By departmentFilterQATitle = By.cssSelector("#select2-filter-by-department-container[title='Quality Assurance']");
    private final By qaDepartmentSpans = By.xpath("//span[contains(@class,'position-department') and text()='Quality Assurance']");


    private void scrollToAndSelectDropdownOption(By dropdownContainerBy, String optionText) {
        logger.info("STEP: Select '{}' from dropdown '{}'", optionText, dropdownContainerBy);
        WaitUtils.waitForVisibility(departmentFilterQATitle);
        clickElement(dropdownContainerBy);
        String DROPDOWN_OPTION_XPATH = "//li[@role='option' and normalize-space(text())='%s']";
        By optionBy = By.xpath(String.format(DROPDOWN_OPTION_XPATH, optionText));
        scrollToElementInContainer(dropdownContainerBy, optionBy);
        clickElement(optionBy);
    }

    public void filterJobs(String location, String department) {
        logger.info("STEP: Apply job filters → Location: '{}', Department: '{}'", location, department);
        scrollToAndSelectDropdownOption(locationFilter, location);
        scrollToAndSelectDropdownOption(departmentFilter, department);
    }

    public boolean areJobsDisplayed() {
        return areElementsDisplayed(jobsList, "Job listings");
    }

    public List<String> getJobPositions() {
        return getTextsAfterHover(qaDepartmentSpans, positionTitle, "job positions");
    }

    public List<String> getJobDepartments() {
        return getTextsAfterHover(qaDepartmentSpans, positionDepartment, "job departments");
    }

    public List<String> getJobLocations() {
        return getTextsAfterHover(qaDepartmentSpans, positionLocation, "job locations");
    }

    public String clickViewRoleAndGetUrl() {
        logger.info("STEP: Click 'View Role' for the first job and navigate to application form");
        hoverAndClickElement(qaDepartmentSpans, firstJobViewRoleButton);

        String originalWindow = driver.getWindowHandle();
        WaitUtils.waitForNumberOfWindowsToBe(2);

        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        return getCurrentPageUrl();
    }
}
