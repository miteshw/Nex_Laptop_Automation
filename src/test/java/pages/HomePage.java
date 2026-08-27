package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Precise locator for the Signup button in the header
    private By signupButton = By.xpath(
            "//header//a[normalize-space()='Signup'] | " +
            "//header//button[normalize-space()='Signup'] | " +
            "//a[contains(@href, '/login') and contains(normalize-space(), 'Signup')]"
    );

    // Profile icon locator (typically displayed after successful login)
    private By profileIcon = By.xpath(
            "//header//*[self::button or self::div or self::a][normalize-space()='M'] | " +
            "//header//*[contains(@class, 'rounded-full') or contains(@class, 'avatar')]"
    );

    // Dropdown and Search locators
    private By categoryDropdown = By.xpath(
            "//span[contains(text(), 'Categories')] | //*[normalize-space()='All Categories']"
    );

    private By searchInput = By.xpath(
            "//input[@placeholder='Search Product']"
    );

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void clickSignup() {
        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(signupButton)
        );
        element.click();
    }

    public boolean isProfileIconDisplayed() {
        try {
            // First check if signup button is gone (most reliable indicator)
            boolean signupGone = wait.until(ExpectedConditions.invisibilityOfElementLocated(signupButton));
            if (signupGone) {
                return true;
            }
            
            WebElement profile = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(profileIcon));
            return profile.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectCategoryAndSearch(String category, String query) {
        // Wait for and click the category dropdown
        WebElement dropdown = wait.until(
                ExpectedConditions.elementToBeClickable(categoryDropdown)
        );
        dropdown.click();

        // Wait a tiny bit for the dropdown menu to expand
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Locate and click the desired category option
        By categoryOptionLocator = By.xpath(
                "//*[text()='" + category + "' and (contains(@class, 'cursor-pointer') or contains(@class, 'text-left') or self::span or self::div)]"
        );
        WebElement option = wait.until(
                ExpectedConditions.elementToBeClickable(categoryOptionLocator)
        );
        option.click();

        // Wait for search input to be visible, enter search term, and press Enter
        WebElement search = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchInput)
        );
        search.clear();
        search.sendKeys(query, Keys.ENTER);
    }
}


