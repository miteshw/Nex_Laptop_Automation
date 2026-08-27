package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SearchResultsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locator for the "Buy Now" button on the search results page
    private By buyNowButton = By.xpath(
            "//button[contains(normalize-space(),'Buy Now')] | //*[text()='Buy Now']"
    );

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void clickBuyNow() {
        WebElement btn = wait.until(
                ExpectedConditions.elementToBeClickable(buyNowButton)
        );
        btn.click();
    }
}
