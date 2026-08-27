package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators - precise and unique to avoid matching elements in header/footer
    private By mobileNumberField = By.xpath(
            "//input[@placeholder='Mobile Number']"
    );

    private By termsCheckbox = By.xpath(
            "//input[@type='checkbox' and @id='terms'] | //input[@id='terms']"
    );

    private By continueButton = By.xpath(
            "//button[contains(normalize-space(),'Continue')]"
    );

    private By secureLoginButton = By.xpath(
            "//button[contains(normalize-space(),'Secure Login')]"
    );

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void enterMobileNumber(String number) {
        WebElement mobile = wait.until(
                ExpectedConditions.visibilityOfElementLocated(mobileNumberField)
        );
        mobile.clear();
        mobile.sendKeys(number);
    }

    public void selectTermsAndConditions() {
        WebElement checkbox = wait.until(
                ExpectedConditions.presenceOfElementLocated(termsCheckbox)
        );

        if (!checkbox.isSelected()) {
            try {
                checkbox.click();
            } catch (Exception e) {
                try {
                    WebElement label = driver.findElement(By.xpath(
                            "//label[@for='terms'] | " +
                            "//label[contains(., 'Terms') or contains(., 'proceeding')] | " +
                            "//input[@id='terms']/parent::* | " +
                            "//input[@id='terms']/following-sibling::*"
                    ));
                    label.click();
                } catch (Exception e2) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
                }
            }
        }
    }

    public void clickContinue() {
        try { Thread.sleep(1000); } catch (Exception e) {}
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(continueButton));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public void enterOTP(String otp) {
        // Wait for OTP input fields to become visible/present after transition
        By otpInputLocator = By.xpath("//input[not(ancestor::header) and not(@placeholder='Mobile Number') and not(@id='terms') and not(@placeholder='Search Product')]");
        wait.until(ExpectedConditions.presenceOfElementLocated(otpInputLocator));
        
        try {
            Thread.sleep(1000); // slight delay for hydration
        } catch (InterruptedException e) {}

        // Find the specific OTP input fields.
        List<WebElement> inputs = driver.findElements(otpInputLocator);

        System.out.println("Filtered " + inputs.size() + " OTP input fields.");

        if (inputs.size() > 1) {
            for (int i = 0; i < inputs.size() && i < otp.length(); i++) {
                WebElement input = inputs.get(i);
                input.clear();
                input.sendKeys(String.valueOf(otp.charAt(i)));
            }
        } else if (inputs.size() == 1) {
            WebElement input = inputs.get(0);
            input.clear();
            input.sendKeys(otp);
        } else {
            throw new RuntimeException("No OTP input fields found on the page.");
        }
    }

    public void enterOTP() {
        System.out.println("Entering default mock OTP: 123456");
        enterOTP("123456");
    }

    public void clickSecureLogin() {
        try { Thread.sleep(1000); } catch (Exception e) {}
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(secureLoginButton));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public void clickSubmit() {
        System.out.println("Clicking on Submit/Secure Login button...");
        clickSecureLogin();
    }
}