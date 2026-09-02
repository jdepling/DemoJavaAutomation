package com.demo.seleniumgrid.fixtures;

import java.net.MalformedURLException;
import java.net.URI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class SeleniumFixture implements AutoCloseable {
    private final WebDriver driver;

    public SeleniumFixture(BrowserType browserType) {
        String gridUrl = System.getenv().getOrDefault("SELENIUM_GRID_URL", "http://localhost:4444");
        try {
            driver = switch (browserType) {
                case CHROME -> new RemoteWebDriver(URI.create(gridUrl).toURL(), new ChromeOptions());
                case FIREFOX -> new RemoteWebDriver(URI.create(gridUrl).toURL(), new FirefoxOptions());
            };
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException("SELENIUM_GRID_URL is not a valid URL: " + gridUrl, exception);
        }
    }

    public WebDriver driver() {
        return driver;
    }

    @Override
    public void close() {
        driver.quit();
    }

    public enum BrowserType {
        CHROME, FIREFOX
    }
}
