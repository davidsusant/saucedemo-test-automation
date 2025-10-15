package com.saucedemo.stepdefinitions;

import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("========================================");
        System.out.println("Starting scenario: " + scenario.getName());
        System.out.println("Tags: " + scenario.getSourceTagNames());
        System.out.println("========================================");

        DriverManager.setDriver(ConfigReader.getBrowser());

        // Add scenario info to Allure report
        Allure.epic("SauceDemo E2E Testing");
        Allure.feature(scenario.getUri().toString().replaceAll(".*/features/", ""));

        // Add environment info to Allure
        Allure.parameter("Browser", ConfigReader.getBrowser());
        Allure.parameter("Base URL", ConfigReader.getBaseUrl());
    }

    @After
    public void tearDown(Scenario scenario) {
        // Take screenshot for both passed and failed scenarios
        byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
                .getScreenshotAs(OutputType.BYTES);

        if (scenario.isFailed()) {
            // Attach to Cucumber report
            scenario.attach(screenshot, "image/png", "Failed Screenshot - " + scenario.getName());

            // Attach to Allure report
            Allure.addAttachment("Failed Screenshot", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");

            System.out.println("❌ Scenario FAILED: " + scenario.getName());
        } else {
            // Attach final screenshot even for passed scenarios
            Allure.addAttachment("Final Screenshot", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");

            System.out.println("✅ Scenario PASSED: " + scenario.getName());
        }

        System.out.println("Scenario status: " + scenario.getStatus());
        System.out.println("=========================================\n");

        DriverManager.quitDriver();
    }
}
