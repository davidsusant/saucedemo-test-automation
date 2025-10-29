@Epic:Authentication
@Feature:Login
Feature: Login Functionality
  As a user of SauceDemo
  I want to be able to login
  So that I can access the application

  Background:
    Given user is on the SauceDemo login page

  @Severity:normal
  @Story:ValidLogin
  @regression
  Scenario: Login with valid credentials using step by step
    When user enters username "standard_user"
    And user enters password "secret_sauce"
    And user clicks on login button
    Then user should be redirected to products page