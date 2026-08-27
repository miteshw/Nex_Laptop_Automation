package base;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected final String BASE_URL = "https://uat.nexlaptop.in";

    @BeforeMethod
    public void setUp() {

        driver = new ChromeDriver();

        driver.manage().window().maximize();

        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(60));

        wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(30)
        );

        driver.get(BASE_URL);

        System.out.println("Application opened successfully.");
        System.out.println("Current URL: " + driver.getCurrentUrl());
    }

    @AfterMethod
    public void tearDown() {

        if (driver != null) {
           // driver.quit();
        }
    }
}