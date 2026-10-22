package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By deliverHereButton = By.xpath(
            "//button[contains(normalize-space(), 'Deliver Here')] | //*[text()='Deliver Here']"
    );

    private By continueButton = By.xpath(
            "//button[contains(normalize-space(), 'Continue')] | //button[text()='Continue']"
    );

    private By codPaymentOption = By.xpath(
            "//input[@type='radio' and @id='cod'] | " +
            "//input[@type='radio' and contains(@value, 'cod')] | " +
            "//label[contains(normalize-space(), 'Cash On Delivery')] | " +
            "//*[normalize-space()='Cash On Delivery'] | " +
            "//*[contains(text(), 'Cash On Delivery')] | " +
            "//*[contains(text(), 'Cash on Delivery')]"
    );

    private By placeOrderButton = By.xpath(
            "//button[contains(normalize-space(), 'Place Order')] | //button[text()='Place Order']"
    );

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }



    public void clickDeliverHere() {
        WebElement btn = wait.until(
                ExpectedConditions.presenceOfElementLocated(deliverHereButton)
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btn);
        try {
            Thread.sleep(500);
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (Exception e) {
            System.out.println("Deliver Here standard click failed, using JS click: " + e.getMessage());
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public void clickContinue() {
        // Sleep briefly to let client-side transitions complete
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement btn = wait.until(
                ExpectedConditions.presenceOfElementLocated(continueButton)
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btn);
        try {
            Thread.sleep(250);
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (Exception e) {
            System.out.println("Continue standard click failed, using JS click: " + e.getMessage());
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public void selectCashOnDelivery() {
        // Sleep to let payment section render
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        WebElement option = wait.until(
                ExpectedConditions.presenceOfElementLocated(codPaymentOption)
        );

        try {
            // Try direct click
            option.click();
        } catch (Exception e) {
            try {
                // Try parent label or wrapper click
                WebElement parent = option.findElement(By.xpath("./.."));
                parent.click();
            } catch (Exception e2) {
                // Try JS click
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
            }
        }
    }

    public void clickPlaceOrder() {
        WebElement btn = wait.until(
                ExpectedConditions.presenceOfElementLocated(placeOrderButton)
        );
        
        // Scroll into view
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btn);
        
        try {
            Thread.sleep(250); // Wait for smooth scroll
            new WebDriverWait(driver, Duration.ofSeconds(2)).until(ExpectedConditions.elementToBeClickable(placeOrderButton)).click();
        } catch (Exception e) {
            System.out.println("Normal click failed, falling back to JS Click: " + e.getMessage());
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public boolean isOrderPlacedSuccessfully() {
        try {
            // Wait for URL to change to something like order success page
            new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                    ExpectedConditions.or(
                            ExpectedConditions.urlContains("success"),
                            ExpectedConditions.urlContains("order"),
                            ExpectedConditions.urlContains("checkout") // if it stays on checkout success
                    )
            );

            // Wait a moment for page load/hydration
            Thread.sleep(500);

            String currentUrl = driver.getCurrentUrl();
            String pageSource = driver.getPageSource().toLowerCase();

            System.out.println("Successful Place Order redirect URL: " + currentUrl);

            // The order is placed successfully if the URL contains success or if the page source contains success text
            return currentUrl.contains("success") || 
                   currentUrl.contains("order") || 
                   pageSource.contains("success") || 
                   pageSource.contains("thank you") ||
                   pageSource.contains("order placed");
        } catch (Exception e) {
            return false;
        }
    }
}
