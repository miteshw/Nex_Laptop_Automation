package tests;

import base.BaseTest;
import pages.HomePage;
import pages.LoginPage;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest extends BaseTest {

    @Test
    public void verifyNexLaptopLogin() {

        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        // =====================================================================
        // STEP 1: Click Signup button
        // =====================================================================
        System.out.println("Step 1: Clicking the Signup button in the header...");
        homePage.clickSignup();

        // Wait for the URL to contain "/login" to verify we transitioned to the login page
        WebDriverWait loginPageWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        loginPageWait.until(ExpectedConditions.urlContains("/login"));

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/login"),
                "User was not navigated to the Login page."
        );
        System.out.println("Step 1: Navigated to Login page successfully. Current URL: " + driver.getCurrentUrl());


        // =====================================================================
        // STEP 2: Enter Mobile Number
        // =====================================================================
        String mobileNumber = "9764811148";
        System.out.println("Step 2: Entering mobile number: " + mobileNumber);
        loginPage.enterMobileNumber(mobileNumber);


        // =====================================================================
        // STEP 3: Select Terms & Conditions Checkbox
        // =====================================================================
        System.out.println("Step 3: Selecting Terms & Conditions checkbox...");
        loginPage.selectTermsAndConditions();


        // =====================================================================
        // STEP 4: Click Continue (Request OTP)
        // =====================================================================
        System.out.println("Step 4: Clicking Continue button to request OTP...");
        loginPage.clickContinue();


        // =====================================================================
        // STEP 5: Enter OTP
        // =====================================================================
        loginPage.enterOTP();


        // =====================================================================
        // STEP 6: Click Secure Login (Verify OTP)
        // =====================================================================
        loginPage.clickSubmit();


        // =====================================================================
        // STEP 7: Verify successful navigation to Homepage
        // =====================================================================
        System.out.println("Step 7: Verifying successful redirection to Homepage...");

        // Wait for the login page URL to transition back to the homepage
        WebDriverWait homePageWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        homePageWait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));

        String expectedHomepageUrl = "https://uat.nexlaptop.in/";
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(
                actualUrl,
                expectedHomepageUrl,
                "URL verification failed: User was not redirected back to the Homepage."
        );
        System.out.println("Step 7: Redirection URL matched: " + actualUrl);

        // Verify that the user profile icon (e.g. avatar button with 'M') is visible on the homepage
        boolean isUserLoggedIn = homePage.isProfileIconDisplayed();
        Assert.assertTrue(
                isUserLoggedIn,
                "Verification failed: User profile avatar was not displayed on the Homepage."
        );
        System.out.println("Step 7: User profile icon verified successfully. Login flow complete!");

        System.out.println("====================================================");
        System.out.println("LOGIN FLOW AUTOMATION TEST PASSED SUCCESSFULLY!");
        System.out.println("====================================================");
    }
}

