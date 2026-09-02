package com.demo.seleniumgrid.tests;

import com.demo.seleniumgrid.fixtures.SeleniumFixture;
import com.demo.seleniumgrid.fixtures.SeleniumFixture.BrowserType;
import com.demo.seleniumgrid.models.WebTableRecord;
import com.demo.seleniumgrid.pages.WebTablesPage;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class WebTablesPageTest {
    @ParameterizedTest
    @EnumSource(BrowserType.class)
    void tableDisplaysExpectedHeaders(BrowserType browserType) {
        try (SeleniumFixture fixture = new SeleniumFixture(browserType)) {
            WebTablesPage page = open(fixture);
            assertEquals(WebTablesPage.EXPECTED_HEADERS, page.getHeaderTexts());
        }
    }

    @ParameterizedTest
    @EnumSource(BrowserType.class)
    void tableDisplaysDefaultRows(BrowserType browserType) {
        try (SeleniumFixture fixture = new SeleniumFixture(browserType)) {
            WebTablesPage page = open(fixture);
            List<org.openqa.selenium.WebElement> rows = page.getAllRows();
            assertEquals(3, rows.size());
            assertTrue(rows.get(0).getText().contains("Cierra"));
            assertTrue(rows.get(0).getText().contains("Vega"));
            assertTrue(rows.get(0).getText().contains("39"));
        }
    }

    @ParameterizedTest
    @EnumSource(BrowserType.class)
    void searchFiltersRowsByFirstName(BrowserType browserType) {
        try (SeleniumFixture fixture = new SeleniumFixture(browserType)) {
            WebTablesPage page = open(fixture);
            page.search("Alden");
            assertEquals(1, page.getAllRows().size());
            assertTrue(page.getAllRows().get(0).getText().contains("Cantrell"));
        }
    }

    @ParameterizedTest
    @EnumSource(BrowserType.class)
    void addRecordAddsRowToTable(BrowserType browserType) {
        try (SeleniumFixture fixture = new SeleniumFixture(browserType)) {
            WebTablesPage page = open(fixture);
            String firstName = "Test" + System.nanoTime();
            int initialCount = page.getRowCount();
            page.addRecord(new WebTableRecord(firstName, "User", firstName + "@example.com",
                    "30", "50000", "QA"));
            assertEquals(initialCount + 1, page.getRowCount());
            assertNotNull(page.getRowContaining(firstName));
        }
    }

    @ParameterizedTest
    @EnumSource(BrowserType.class)
    void deleteRecordRemovesRowFromTable(BrowserType browserType) {
        try (SeleniumFixture fixture = new SeleniumFixture(browserType)) {
            WebTablesPage page = open(fixture);
            int initialCount = page.getRowCount();
            page.deleteRowContaining("Cierra");
            assertEquals(initialCount - 1, page.getRowCount());
            assertFalse(page.rowExists("Cierra"));
        }
    }

    private WebTablesPage open(SeleniumFixture fixture) {
        WebTablesPage page = new WebTablesPage(fixture.driver());
        page.open();
        return page;
    }
}
