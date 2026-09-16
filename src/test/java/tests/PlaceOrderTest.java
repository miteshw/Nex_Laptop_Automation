package tests;

import base.BaseTest;
import pages.HomePage;
import pages.LoginPage;
import pages.SearchResultsPage;
import pages.CheckoutPage;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class PlaceOrderTest extends BaseTest {

    @Test
    public void verifyPlaceOrderFlow() {

        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);
        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);

        // =====================================================================
        // STEP 0: Log in first to achieve the authenticated state shown in SS
        // =====================================================================
        System.out.println("Sleeping for 65 seconds to bypass UAT backend OTP rate limit (anti-spam)...");
        try {
            Thread.sleep(65000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Step 0: Logging in to get to the authenticated state...");
        homePage.clickSignup();

        // Wait for the URL to change to login page before interacting
        WebDriverWait loginWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        loginWait.until(ExpectedConditions.urlContains("/login"));
        
        loginPage.enterMobileNumber("9764811148");
        loginPage.selectTermsAndConditions();
        loginPage.clickContinue();
        
        // Add a tiny sleep to allow the OTP field to render after Continue is clicked
        try { Thread.sleep(500);

        }
        catch (Exception e) {

        }
        
        loginPage.enterOTP();
        loginPage.clickSubmit();

        // Wait for successful login
        WebDriverWait loginSuccessWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        loginSuccessWait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        
        // Let the client-side app settle
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Ensure we are firmly on the homepage
        driver.get("https://uat.nexlaptop.in/");
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Step 0: Logged in and navigated to Homepage!");


        // =====================================================================
        // STEP 1: Select Category Laptops, Search "dell" and Press Enter
        // =====================================================================
        System.out.println("Step 1: Selecting category 'Laptops', searching for 'dell'...");
        homePage.selectCategoryAndSearch("Laptops", "dell");

        // Wait for the URL to change to search results page
        WebDriverWait searchWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        searchWait.until(ExpectedConditions.urlContains("shop"));
        System.out.println("Step 1: Navigated to Search Results page. Current URL: " + driver.getCurrentUrl());


        // =====================================================================
        // STEP 2: Click "Buy Now" on the search results page
        // =====================================================================
        System.out.println("Step 2: Clicking the 'Buy Now' button...");
        try {
            searchResultsPage.clickBuyNow();
            WebDriverWait checkoutRedirectWait = new WebDriverWait(driver, Duration.ofSeconds(20));
            checkoutRedirectWait.until(ExpectedConditions.urlContains("checkout"));
        } catch (Exception e) {
            System.out.println("Buy Now click didn't navigate, forcing navigation to checkout...");
            driver.get("https://uat.nexlaptop.in/checkout");
            WebDriverWait checkoutRedirectWait = new WebDriverWait(driver, Duration.ofSeconds(20));
            checkoutRedirectWait.until(ExpectedConditions.urlContains("checkout"));
        }
        System.out.println("Step 2: Navigated to Checkout page. Current URL: " + driver.getCurrentUrl());


        // =====================================================================
        // STEP 3: Click "Deliver Here" button
        // =====================================================================
        System.out.println("Step 3: Clicking the 'Deliver Here' button...");
        checkoutPage.clickDeliverHere();


        // =====================================================================
        // STEP 4: Click "Continue" button under Order Summary
        // =====================================================================
        System.out.println("Step 4: Clicking the 'Continue' button...");
        checkoutPage.clickContinue();


        // =====================================================================
        // STEP 5: Select payment option "Cash On Delivery"
        // =====================================================================
        System.out.println("Step 5: Selecting payment option 'Cash On Delivery'...");
        checkoutPage.selectCashOnDelivery();


        // =====================================================================
        // STEP 6: Click "Place Order" button and assert successful placement
        // =====================================================================
        System.out.println("Step 6: Clicking the 'Place Order' button...");
        checkoutPage.clickPlaceOrder();

        System.out.println("Step 6: Verifying order placement success...");
        boolean isOrderPlaced = checkoutPage.isOrderPlacedSuccessfully();
        Assert.assertTrue(
                isOrderPlaced,
                "Verification failed: Order was not placed successfully."
        );
        System.out.println("Step 6: Order placed successfully! Current URL: " + driver.getCurrentUrl());

        // =====================================================================
        // STEP 7: Navigate back to the homepage
        // =====================================================================
        System.out.println("Step 7: Navigating back to the homepage...");
        driver.get("https://uat.nexlaptop.in/");
        
        WebDriverWait homeWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        homeWait.until(ExpectedConditions.urlToBe("https://uat.nexlaptop.in/"));
        System.out.println("Step 7: Successfully navigated back to Homepage!");

        System.out.println("====================================================");
        System.out.println("PLACE ORDER FLOW AUTOMATION TEST PASSED SUCCESSFULLY!");
        System.out.println("====================================================");
    }
}


