package com.saucedemo.utils;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v85.input.Input;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class AllureEnvironmentWriter {

    /**
     * Writes environment properties to allure-results folder
     * This method collects real-time system and browser information
     */

    public static void writeEnvironmentProperties() {
        Map<String, String> environment = new LinkedHashMap<>();

        try {
            // Get browser information from config
            String browser = ConfigReader.getBrowser();
            environment.put("Browser", capitalizeFirstLetter(browser));

            // Get browser version (only if driver is active)
            try {
                if (DriverManager.getDriver() != null) {
                    Capabilities capabilities = ((RemoteWebDriver) DriverManager.getDriver()).getCapabilities();
                    String browserVersion = capabilities.getBrowserVersion();
                    environment.put("Browser.Version", browserVersion != null ? browserVersion : "Unknown");
                }
            } catch (Exception e) {
                environment.put("Browser.Version", "Unknown");
            }

            // Get application URL from config
            environment.put("Base.URL", ConfigReader.getBaseUrl());

            // Get actual Java version from system
            environment.put("Java.Version", System.getProperty("java.version"));

            // Get actual OS information
            environment.put("OS", System.getProperty("os.name"));
            environment.put("OS.Version", System.getProperty("os.version"));
            environment.put("OS.Architecture", System.getProperty("os.arch"));

            // Get user information
            environment.put("User", System.getProperty("user.name"));

            // Framework information
            environment.put("Automation.Framework", "Selenium + Cucumber + TestNG");
            environment.put("Report.Tool", "Allure");

            // Selenium Version
            environment.put("Selenium.Version", getSeleniumVersion());

            // Test execution information
            environment.put("Test.Environment", getEnvironmentType());
            environment.put("Execution.Date", java.time.LocalDateTime.now().toString());

            // Write to file
            writePropertiesToFile(environment, "allure-results/environment.properties");
            System.out.println("✅ Environment properties written successfully");
        } catch (Exception e) {
            System.err.println("⚠\uFE0F Failed to write environment properties: " + e.getMessage());
        }
    }

    /**
     * Capitalize first letter of a string
     */
    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Get Selenium version from actual JAR manifest
     */
    private static String getSeleniumVersion() {
        // Try MANIFEST.MF
        try {
            String className = org.openqa.selenium.WebDriver.class.getName().replace(".", "/");
            String classJar = org.openqa.selenium.WebDriver.class.getResource("/" + className + ".class").toString();

            if (classJar.startsWith("jar:")) {
                String manifestPath = classJar.substring(0, classJar.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
                java.net.URL url = new java.net.URL(manifestPath);
                java.util.jar.Manifest manifest = new java.util.jar.Manifest(url.openStream());
                java.util.jar.Attributes attr = manifest.getMainAttributes();

                // Try different attribute names
                String version = attr.getValue("Implementation-Version");
                if (version == null) version = attr.getValue("Bundle-Version");
                if (version == null) version = attr.getValue("Specification-Version");

                if (version != null && !version.isEmpty()) {
                    return version;
                }
            }
        } catch (Exception e) {
            // Continue to next method
        }

        // Try pom.properties
        try {
            InputStream is = org.openqa.selenium.WebDriver.class.getClassLoader()
                    .getResourceAsStream("META-INF/maven/org.seleniumhq.selenium/selenium-api/pom.properties");

            if (is != null) {
                Properties properties = new Properties();
                properties.load(is);
                String version = properties.getProperty("version");
                is.close();

                if (version != null && !version.isEmpty()) {
                    return version;
                }
            }
        } catch (Exception e) {
            // Continue to next method
        }

        // Extract from JAR file path
        try {
            String className = org.openqa.selenium.WebDriver.class.getName().replace(".", "/");
            String classJar = org.openqa.selenium.WebDriver.class.getResource("/" + className + ".class").toString();

            // Pattern: selenium-api-4.15.0.jar
            if (classJar.contains("selenium-api-")) {
                String jarName = classJar.substring(classJar.indexOf("selenium-api-"), classJar.indexOf(".jar"));
                String version = jarName.replace("selenium-api-", "");
                if (version != null && !version.isEmpty() && version.matches("\\d+\\.\\d+\\.\\d+.*")) {
                    return version;
                }
            }
        } catch (Exception e) {
            // Fall through
        }

        return "Unknown";
    }

    /**
     * Determine environment type based on URL
     */
    private static String getEnvironmentType() {
        String url = ConfigReader.getBaseUrl().toLowerCase();
        if (url.contains("Localhost") || url.contains("127.0.0.1")) {
            return "Local";
        } else if (url.contains("staging") || url.contains("stg")) {
            return "Staging";
        } else if (url.contains("qa") || url.contains("test")) {
            return "QA";
        } else if (url.contains("uat")) {
            return "UAT";
        } else {
            return "Production";
        }
    }

    /**
     * Write properties to file
     */
    private static void writePropertiesToFile(Map<String, String> properties, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Failed to write to file: " + filePath);
            e.printStackTrace();
        }
    }
}
