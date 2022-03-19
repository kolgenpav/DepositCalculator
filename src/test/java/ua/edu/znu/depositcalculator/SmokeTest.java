package ua.edu.znu.depositcalculator;

// Generated by Selenium IDE

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SmokeTest {

    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void smokeTest() {
        driver.get("https://fin-calc.org.ua/ua/deposit/calculate/");
        WebElement sum = driver.findElement(By.id("sum"));
        WebElement term = driver.findElement(By.id("term"));
        WebElement interest = driver.findElement(By.id("percent"));
        sum.clear();
        term.clear();
        interest.clear();
        sum.sendKeys("25000");
        term.sendKeys("12");
        interest.sendKeys("11");
        driver.findElement(By.id("i_scheme5")).click();
        driver.findElement(By.id("submit")).click();

        /*Explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        */

        /*Fluent wait*/
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(2))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement interestAmount = wait.until(ExpectedConditions
                .presenceOfElementLocated(By.cssSelector(".finale > .right:nth-child(3)")));
        WebElement sumWithInterest = wait.until(ExpectedConditions
                .presenceOfElementLocated(By.cssSelector(".finale > .right:nth-child(4)")));

//        String interestAmount = driver.findElement(By.cssSelector(".finale > .right:nth-child(3)")).getText();
//        String sumWithInterest = driver.findElement(By.cssSelector(".finale > .right:nth-child(4)")).getText();
        assertEquals("2750.04", interestAmount.getText());
        assertEquals("27750.04", sumWithInterest.getText());
    }
}
