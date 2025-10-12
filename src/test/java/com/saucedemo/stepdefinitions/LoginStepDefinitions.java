package com.saucedemo.stepdefinitions;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.ConfigReader;
import com.saucedemo.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginStepDefinitions {

    private LoginPage loginPage;
    private ProductsPage productsPage;

    @Given("user is on the SauceDemo login page")
    public void userIsOnLoginPage() {
        DriverManager.getDriver().get(ConfigReader.getBaseUrl());
        loginPage = new LoginPage(DriverManager.getDriver());
    }

    @When("user enters username {string}")
    public void userEntersUsername(String username) {
        loginPage.enterUsername(username);
    }

    @When("user enters password {string}")
    public void userEntersPassword(String password) {
        loginPage.enterPassword(password);
    }

    @When("user clicks on login button")
    public void userClicksLoginButton() {
        loginPage.clickLoginButton();
    }

    @Then("user should be redirected to products page")
    public void userShouldBeRedirectedToProductsPage() {
        productsPage = new ProductsPage(DriverManager.getDriver());
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "User was not redirected to products page");
    }
}
