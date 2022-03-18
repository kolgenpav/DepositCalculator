package ua.edu.znu.depositcalculator;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositCalculatorTest {

    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    WebElement sum;
    WebElement term;
    WebElement interest;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();

        driver.get("https://fin-calc.org.ua/ua/deposit/calculate/");
        sum = driver.findElement(By.id("sum"));
        term = driver.findElement(By.id("term"));
        interest = driver.findElement(By.id("percent"));
        sum.clear();
        term.clear();
        interest.clear();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @ParameterizedTest
    @CsvSource({
            "25000, 12, 11, i_scheme1, 2892.97, 27892.97",
            "25000, 12, 11, i_scheme2, 2865.54, 27865.54",
            "25000, 12, 11, i_scheme5, 2750.04, 27750.04"
    })
    public void positiveTest(String dSum, String dTerm, String dInterest, String schemeId,
                                          String interestAmounExpResult, String sumWithInterestExpResult) {
        sum.sendKeys(dSum);
        term.sendKeys(dTerm);
        interest.sendKeys(dInterest);
        driver.findElement(By.id(schemeId)).click();
        driver.findElement(By.id("submit")).click();
        String interestAmount = driver.findElement(By.cssSelector(".finale > .right:nth-child(3)")).getText();
        String sumWithInterest = driver.findElement(By.cssSelector(".finale > .right:nth-child(4)")).getText();
        assertEquals(interestAmounExpResult, interestAmount);
        assertEquals(sumWithInterestExpResult, sumWithInterest);
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 1, 0.000001, i_scheme1",
            "0, -1, 0.0000011, i_scheme2",
            "-1, 0, 1000.0, i_scheme5",
            "1, 255, -1, i_scheme1",
            "-1, 12, 0, i_scheme2",
            "25000, 256, 0.000000999999999999999, i_scheme5",
            "-1, -1, 1000.0000000000001, i_scheme1",
    })
    public void negativeTest(String dSum, String dTerm, String dInterest, String schemeId) {
        sum.sendKeys(dSum);
        term.sendKeys(dTerm);
        interest.sendKeys(dInterest);
        driver.findElement(By.id(schemeId)).click();
        driver.findElement(By.id("submit")).click();
        String expErrorMessage = "Введені значення для розрахунку некоректні. " +
                "Поля з помилками виділені кольором. Виправте дані в зазначених полях і повторіть розрахунок.";
        String errorMessage = driver.findElement(By.cssSelector("#errors > p")).getText();
        assertEquals(expErrorMessage, errorMessage);
    }
}
