package com.demo.seleniumgrid.pages;

import com.demo.seleniumgrid.models.WebTableRecord;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class WebTablesPage extends BasePage {
    public static final String URL = "https://demoqa.com/webtables";
    public static final List<String> EXPECTED_HEADERS = List.of(
            "First Name", "Last Name", "Age", "Email", "Salary", "Department", "Action");

    private final By headerCells = By.cssSelector("table thead th");
    private final By tableRows = By.cssSelector("table tbody tr");
    private final By searchBox = By.id("searchBox");
    private final By addButton = By.id("addNewRecordButton");
    private final By submitButton = By.xpath("//button[text()='Submit']");

    public WebTablesPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigate(URL);
    }

    public List<String> getHeaderTexts() {
        waitForElement(headerCells);
        return driver.findElements(headerCells).stream().map(WebElement::getText).toList();
    }

    public List<WebElement> getAllRows() {
        return driver.findElements(tableRows);
    }

    public int getRowCount() {
        return getAllRows().size();
    }

    public WebElement getRowContaining(String text) {
        return getAllRows().stream()
                .filter(row -> row.getText().contains(text))
                .findFirst()
                .orElse(null);
    }

    public void search(String query) {
        fillInput(searchBox, query);
        wait.until(driver -> getRowContaining(query) != null);
    }

    public void addRecord(WebTableRecord record) {
        click(addButton);
        fillInput(By.id("firstName"), record.firstName());
        fillInput(By.id("lastName"), record.lastName());
        fillInput(By.id("userEmail"), record.email());
        fillInput(By.id("age"), record.age());
        fillInput(By.id("salary"), record.salary());
        fillInput(By.id("department"), record.department());
        click(submitButton);
    }

    public void deleteRowContaining(String text) {
        WebElement row = getRowContaining(text);
        if (row == null) {
            throw new IllegalArgumentException("Row containing '" + text + "' not found");
        }
        row.findElement(By.cssSelector("span[title='Delete']")).click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.stalenessOf(row));
    }

    public boolean rowExists(String text) {
        return getRowContaining(text) != null;
    }

    @Override
    protected void onPageLoaded() {
        waitForElement(tableRows);
    }
}
