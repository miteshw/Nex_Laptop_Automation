package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SearchResultsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locator for the "Buy Now" button on the search results page (only enabled buttons)
    private By buyNowButton = By.xpath(
            "//button[(contains(normalize-space(),'Buy Now') or text()='Buy Now') and not(@disabled)]"
    );

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void clickBuyNow() {
        WebElement btn = wait.until(
                ExpectedConditions.presenceOfElementLocated(buyNowButton)
        );

        // Scroll into view to avoid header interception
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btn);

        try {
            Thread.sleep(500);
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (Exception e) {
            System.out.println("Buy Now standard click intercepted/failed, using JS click: " + e.getMessage());
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }
}
