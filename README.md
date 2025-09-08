# Insider Careers - End-to-End Test Automation

<a href="https://selenium.dev"><img src="https://selenium.dev/images/selenium_logo_square_green.png" width="40" height="40" alt="Selenium"/></a>

Welcome to the Insider Careers Test Automation project! This repository contains an automated test framework designed to verify the end-to-end user journey on the Insider Careers page, from navigating the homepage to verifying job listings and application form redirection.


## Table of Contents
- [Introduction](#introduction)
- [Important Observation](#important-observation)
- [Key Features](#key-features)
- [Technologies Used](#technologies-used)
- [Maven Libraries Used](#maven-libraries-used)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Running Tests](#running-tests)
- [Contact](#contact)


## Introduction

This project provides a robust solution for testing the Insider website's career section. Built with Java, Selenium, and TestNG, it follows industry best practices to ensure stability, maintainability, and readability.

The core of this project is a single, comprehensive **End-to-End (E2E) test case**. This approach was intentionally chosen to validate the entire critical user path in a single, integrated flow, simulating a real user's experience precisely. Instead of breaking down the test into smaller, isolated units, the E2E test ensures that all components—from UI navigation and dynamic filtering to cross-page interactions—work together seamlessly.

## Important Observation

This test suite was designed not only to validate the specified user path but also to act as a sensitive instrument for detecting application instabilities. During development, critical, intermittent issues were identified in the filtering logic, which the test now successfully detects.

*Note: These observations were made during testing on **Ubuntu OS with Google Chrome.***

### Finding 1: Filter State Inconsistency (Manually & Automatically Confirmed)

*   **Scenario:** The test flow involves clicking a link on the Careers page (`See all QA jobs`) that directs the user to the Open Positions page. **This action pre-selects the "Quality Assurance" department via URL parameters.**
*   **Observed Bug:** Upon page load, although the filter UI correctly shows "Quality Assurance" as selected, the job list intermittently displays results for "All Departments." Re-selecting the already active "Quality Assurance" filter does not trigger an update.
*   **Workaround:** The issue resolves only after selecting a different department and then re-selecting "Quality Assurance," or by performing a full page refresh.
*   **Test Behavior:** The automation script **correctly fails** at the validation step when it encounters this state inconsistency, as the displayed jobs do not match the selected filter.

### Finding 2: Filter Application Race Condition (Observed via Automation)

*   **Scenario:** The E2E test selects both a "Location" and a "Department" in sequence as required by the test case.
*   **Observed Bug:** On rare occasions, the job list fails to reflect both filter criteria and defaults to showing "All Jobs." This suggests a potential race condition in the front-end application. A screenshot of this behavior has been captured.

### Finding 3: Deep DOM Instability Leading to StaleElementReferenceException

*   **Observation:** Beyond the logical bugs, the test suite frequently encountered `StaleElementReferenceException`. This indicates a high level of DOM instability, where elements are being re-rendered or removed while the test is trying to interact with them.
*   **Attempted Solution:** To combat this, a robust retry mechanism was implemented within the `WaitUtils` class. This solution attempts to re-locate the element multiple times if a SERE is caught, which is a standard best practice for handling this issue.
*   **Result:** Despite this robust solution, the test **still occasionally fails due to this exception.** This is a significant finding: it suggests that the application's front-end instability is so pronounced that even standard resilience patterns are not always sufficient. This points to a deeper performance or architectural issue that would require investigation in collaboration with the development team.

### Strategic Decision: Why Workarounds Were Intentionally Not Implemented

It would have been technically possible to force the test to "pass" every time by implementing workarounds (e.g., adding a page refresh or re-selecting filters until the correct state is achieved).

However, **this approach was intentionally avoided.** The primary goal of a robust QA process is to provide an honest and accurate reflection of the end-user experience. Masking these intermittent bugs with automation tricks would hide significant usability issues and provide a false sense of security. Therefore, the test is designed to be "brittle" in the face of these specific bugs, ensuring they are transparently reported through its failure.

## Key Features
- **Page Object Model (POM):** The framework is built on the POM design pattern, ensuring a clean separation between test logic and UI interactions, which makes the code highly reusable and easy to maintain.
- **Robust WebDriver Management:** Utilizes a `DriverManager` with `ThreadLocal` to guarantee thread safety, making the framework scalable and ready for parallel test execution.
- **Data-Driven Testing:** Test data (location, department) is externalized into a `JSON` file and fed into tests using TestNG's `DataProvider`, allowing for easy modification and extension without changing the test code.
- **Advanced Logging:** Integrated with `SLF4J` and `Logback` to provide detailed, step-by-step logs of the entire test execution flow. This makes debugging incredibly efficient and provides full visibility into the test's lifecycle.
- **Smart Wait Mechanism:** A centralized `WaitUtils` class handles all explicit waits, eliminating flaky tests and avoiding the use of unreliable static waits (`Thread.sleep`).
- **Robust Error Handling:** The framework includes intelligent error handling, such as automatically retrying a click action with JavaScript if a standard click is intercepted, which significantly increases test stability.
- **Cross-Browser Compatibility:** The browser type can be easily configured in a properties file, allowing the same test suite to run on different browsers like Chrome, Firefox, and Edge.

## Technologies Used

- [Java](https://www.java.com/)
- [Selenium WebDriver](https://selenium.dev)
- [TestNG](https://testng.org/)
- [Maven](https://maven.apache.org/)
- [SLF4J & Logback](https://logback.qos.ch/)
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager)

## Maven Libraries Used
- [Selenium Java](https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java)
- [TestNG](https://mvnrepository.com/artifact/org.testng/testng)
- [WebDriverManager](https://mvnrepository.com/artifact/io.github.bonigarcia/webdrivermanager)
- [SLF4J API](https://mvnrepository.com/artifact/org.slf4j/slf4j-api)
- [Logback Classic](https://mvnrepository.com/artifact/ch.qos.logback/logback-classic)

## Project Structure

The project follows a logical, layered architecture to separate responsibilities:

- `base`: Contains the `BaseTest` class for common test setup and teardown.
- `driver`: Manages WebDriver creation, initialization, and cleanup using `ThreadLocal`.
- `pages`: Includes all Page Object classes that encapsulate UI elements and interactions.
- `tests`: Contains the TestNG test classes and data providers.
- `utils`: Provides helper classes for configuration reading, data reading, and explicit waits.
- `resources`: Stores external files like `config.properties`, test data (`.json`), and the TestNG suite (`.xml`).

```plaintext
.
├── pom.xml
└── src
    ├── main
    │   └── java
    └── test
        ├── java
        │   └── com
        │       └── insider
        │           ├── base
        │           │   └── BaseTest.java
        │           ├── driver
        │           │   └── DriverManager.java
        │           ├── pages
        │           │   ├── BasePage.java
        │           │   ├── CareersPage.java
        │           │   ├── HomePage.java
        │           │   ├── OpenPositionsPage.java
        │           │   └── QualityAssurancePage.java
        │           ├── tests
        │           │   ├── dataproviders
        │           │   │   └── QAJobsDataProvider.java
        │           │   └── InsiderEndToEndTest.java
        │           └── utils
        │               ├── ConfigReader.java
        │               ├── JsonDataReader.java
        │               └── WaitUtils.java
        └── resources
            ├── config.properties
            ├── logback.xml
            ├── qaJobsData.json
            └── testng.xml
```
## Installation

To set up and run this project locally, follow these steps:

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/taygunkara/taygun_kara_case.git
    cd taygun_kara_case
    ```

2.  **Open the project in your preferred Java IDE** (e.g., IntelliJ IDEA, Eclipse).

3.  **Build the project with Maven.** The IDE should automatically detect the `pom.xml` and download all the necessary dependencies. If not, trigger a manual Maven build.

## Running Tests

### From Terminal

You can run the entire test suite from the command line using Maven:
```bash
mvn clean test
```
This command will execute the tests defined in the `testng.xml` suite file.

### Configuration

- **Browser Selection:** To run tests on a different browser, modify the `browser.type` property in the `src/test/resources/config.properties` file. Supported values are `CHROME`, `FIREFOX`, and `EDGE`.
- **URL Configuration:** The application base URL can also be changed in the `config.properties` file.

## Contact
For any queries or collaboration, feel free to reach out to me via email at [kara.taygun@gmail.com](mailto:kara.taygun@gmail.com).
