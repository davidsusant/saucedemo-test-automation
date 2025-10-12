package com.saucedemo.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@CucumberOptions(
    features = "src/test/resources/features",
        glue = {"com.saucedemo.stepdefinitions"},
        tags = "@smoke or @regression",
        plugin = {
            "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "junit:target/cucumber-reports/cucumber.xml"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    @Test(groups = "cucumber", description = "Run Cucumber Scenarios")
    public void runScenarios() {

    }
}
