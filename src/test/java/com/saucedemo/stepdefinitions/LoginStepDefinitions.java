package com.saucedemo.stepdefinitions;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.testng.Assert;

public class LoginStepDefinitions {

    private LoginPage loginPage;
    private ProductsPage productsPage;

    @Step("Navigate to SauceDemo login page")
    @Given("user is on the SauceDemo login page")
    public void userIsOnLoginPage() {
        DriverManager.getDriver().get(ConfigReader.getBaseUrl());
        loginPage = new LoginPage(DriverManager.getDriver());
    }

    @Step("Enter username: {username}")
    @When("user enters username {string}")
    public void userEntersUsername(String username) {
        loginPage.enterUsername(username);
    }

    @Step("Enter password: {password}")
    @When("user enters password {string}")
    public void userEntersPassword(String password) {
        loginPage.enterPassword(password);
    }

    @Step("Click login button")
    @When("user clicks on login button")
    public void userClicksLoginButton() {
        loginPage.clickLoginButton();
    }

    @Step("Verify products page title is displayed")
    @Then("user should be redirected to products page")
    public void userShouldBeRedirectedToProductsPage() {
        productsPage = new ProductsPage(DriverManager.getDriver());
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "User was not redirected to products page");
    }
}
