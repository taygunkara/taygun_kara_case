package com.insider.tests;

import com.insider.base.BaseTest;
import com.insider.pages.CareersPage;
import com.insider.pages.HomePage;
import com.insider.pages.OpenPositionsPage;
import com.insider.pages.QualityAssurancePage;
import com.insider.tests.dataproviders.QAJobsDataProvider;
import com.insider.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * End-to-End Test Case for Insider QA Jobs Verification
 * Steps:
 * 1. Visit "https://useinsider.com/" and verify home page loads
 * 2. Navigate to Careers via Company menu, verify Locations, Teams, Life at Insider
 * 3. Go to QA Jobs, filter by Istanbul, Turkey & Quality Assurance, check job list
 * 4. Validate all jobs: Position, Department, Location
 * 5. Click "View Role" and verify redirect to Lever
 */

public class InsiderEndToEndTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(InsiderEndToEndTest.class);
    private static final String LOCATIONS_TEXT = "Locations";
    private static final String TEAMS_TEXT = "Teams";
    private static final String LIFE_AT_INSIDER_TEXT = "Life at Insider";

    @Test(description = "Verify QA Jobs in Istanbul, Turkey and redirect to Lever",
            dataProvider = "qaJobsData", dataProviderClass = QAJobsDataProvider.class)
    public void shouldVerifyQaJobsAndRedirectToLever(String targetLocation, String targetDepartment) {
        logger.info("=== Starting the End-to-End QA Jobs Verification Test! ===");

        // Step 1: Visit https://useinsider.com/ and check home page is opened
        logger.info("Step 1: Navigating to the Insider homepage and verifying its load.");
        String expectedHomeUrl = ConfigReader.getProperty("app.url");
        String currentUrl = getCurrentPageUrl();
        Assert.assertEquals(currentUrl, expectedHomeUrl, "Home page did not load correctly. Expected: " + expectedHomeUrl + ", Found: " + currentUrl);
        logger.info("Step 1 PASSED: Successfully landed on the Insider homepage: {}", currentUrl);

        logger.debug("Attempting to handle any initial popups or cookie banners.");
        HomePage homePage = new HomePage();
        homePage.closeInitialPopups();

        // Step 2: Select “Company” → “Careers”, check sections
        logger.info("Step 2: Proceeding to the Careers page via the 'Company' menu and verifying key sections.");
        CareersPage careersPage = homePage.goToCareersPage();

        Assert.assertTrue(careersPage.isSectionDisplayed(careersPage.locationsSection, LOCATIONS_TEXT),
                "'Locations' section is NOT displayed on the Careers page.");
        Assert.assertTrue(careersPage.isSectionDisplayed(careersPage.teamsSection, TEAMS_TEXT),
                "'Teams' section is NOT displayed on the Careers page.");
        Assert.assertTrue(careersPage.isSectionDisplayed(careersPage.lifeAtInsiderSection, LIFE_AT_INSIDER_TEXT),
                "'Life at Insider' section is NOT displayed on the Careers page.");
        logger.info("Step 2 PASSED: All expected sections (Locations, Teams, Life at Insider) are present and visible on the Careers page.");

        // Step 3: Go to QA Jobs, click “See all QA jobs”, filter, check job list
        logger.info("Step 3: Navigating to the QA Jobs section, applying filters, and verifying job listings.");
        QualityAssurancePage qualityAssurancePage = careersPage.goToQaJobsPage();
        qualityAssurancePage.clickSeeAllQaJobs();
        OpenPositionsPage openPositionsPage = new OpenPositionsPage();
        openPositionsPage.filterJobs(targetLocation, targetDepartment);
        Assert.assertTrue(openPositionsPage.areJobsDisplayed(), "No QA jobs were found after applying filters. Expected to see job listings.");
        logger.info("Step 3 PASSED: Jobs are successfully filtered and displayed for 'Istanbul, Turkiye' and 'Quality Assurance'.");

        // Step 4: Check all jobs have correct Position, Department, Location
        logger.info("Step 4: Beginning validation of each displayed job's Position, Department, and Location details.");
        List<String> positions = openPositionsPage.getJobPositions();
        List<String> departments = openPositionsPage.getJobDepartments();
        List<String> locations = openPositionsPage.getJobLocations();

        Assert.assertFalse(positions.isEmpty(), "The list of job positions is unexpectedly empty. No positions found to validate.");
        Assert.assertFalse(departments.isEmpty(), "The list of job departments is unexpectedly empty. No departments found to validate.");
        Assert.assertFalse(locations.isEmpty(), "The list of job locations is unexpectedly empty. No locations found to validate.");
        logger.debug("Found {} job entries for detailed validation.", positions.size());

        for (int i = 0; i < positions.size(); i++) {
            String position = positions.get(i);
            String department = departments.get(i);
            String location = locations.get(i);
            logger.debug("Validating job #{}: Position='{}', Department='{}', Location='{}'", (i + 1), position, department, location);

            Assert.assertTrue(position.contains("Quality Assurance"),
                    "Validation FAILED for Job #" + (i + 1) + ": Position title does NOT contain 'Quality Assurance'. Found: '" + position + "'");
            Assert.assertEquals(department, "Quality Assurance",
                    "Validation FAILED for Job #" + (i + 1) + ": Department mismatch. Expected 'Quality Assurance', Found: '" + department + "'");
            Assert.assertEquals(location, "Istanbul, Turkiye",
                    "Validation FAILED for Job #" + (i + 1) + ": Location mismatch. Expected 'Istanbul, Turkiye', Found: '" + location + "'");
            logger.debug("Job #{} details validated successfully.", (i + 1));
        }
        logger.info("Step 4 PASSED: All displayed jobs correctly match the expected Position, Department, and Location criteria.");

        // Step 5: Click “View Role” and check redirect to Lever
        logger.info("Step 5: Clicking 'View Role' for the first job and verifying redirection to the application platform (Lever).");
        String leverUrl = openPositionsPage.clickViewRoleAndGetUrl();
        Assert.assertTrue(leverUrl.contains("lever.co"), "Redirection failed. Expected to land on a 'lever.co' URL, but found: " + leverUrl);
        logger.info("Step 5 PASSED: Successfully redirected to the Lever application form at: {}", leverUrl);

        logger.info("=== End-to-End QA Jobs Verification Test COMPLETED SUCCESSFULLY! All steps passed. ===");
    }
}