package com.paypal.example.paypalandroidsdkexample.test;

import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;

import static com.lukekorth.deviceautomator.AutomatorAction.click;
import static com.lukekorth.deviceautomator.AutomatorAssertion.text;
import static com.lukekorth.deviceautomator.UiObjectMatcher.withText;
import static com.lukekorth.deviceautomator.UiObjectMatcher.withTextStartingWith;
import static com.paypal.example.paypalandroidsdkexample.test.TestHelper.onDeviceWithScreenShot;
import static com.paypal.example.paypalandroidsdkexample.test.TestHelper.onLogin;
import static com.paypal.example.paypalandroidsdkexample.test.TestHelper.onAgree;
import static com.paypal.example.paypalandroidsdkexample.test.TestHelper.takeScreenshot;
import static org.hamcrest.CoreMatchers.containsString;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class PaymentTest {

    @Before
    public void setup() {
        TestHelper.setup();
    }

    /**
     * Makes a Single Payment
     */
    @Test(timeout = 60000)
    public void buyAThing() {
        onDeviceWithScreenShot(withText("Buy a Thing")).perform(click());
        onDeviceWithScreenShot(withText("Pay with")).perform(click());
        onLogin();
        // Payment Confirmation
        onDeviceWithScreenShot(withText("Pay")).perform(click());
        // Confirm Result Success
        onDeviceWithScreenShot(withTextStartingWith("Result :")).check(text(containsString("PaymentConfirmation")));
    }

    /**
     * Gets a Future Payment Consent
     */
    @Test(timeout = 60000)
    public void futurePaymentConsent() {
        takeScreenshot("futurePaymentConsent");
        onDeviceWithScreenShot(withText("Future Payment Consent")).perform(click());
        onLogin();
        onAgree();
        // Confirm Result Success
        onDeviceWithScreenShot(withTextStartingWith("Result :")).check(text(containsString("Future Payment")));

    }

    /**
     * Makes a Future Payment Purchase
     */
    @Test(timeout = 60000)
    public void futurePaymentPurchase() {
        takeScreenshot("futurePaymentPurchase");
        onDeviceWithScreenShot(withText("Future Payment Purchase")).perform(click());
        // Confirm Result Success
        onDeviceWithScreenShot(withTextStartingWith("Result :")).check(text(containsString("Client Metadata Id")));
    }

    /**
     * Gets Profile Sharing Consent
     */
    @Test(timeout = 60000)
    public void profileSharingConsent() {
        takeScreenshot("profileSharingConsent");
        onDeviceWithScreenShot(withText("Profile Sharing Consent")).perform(click());
        onLogin();
        onAgree();
        // Confirm Result Success
        onDeviceWithScreenShot(withTextStartingWith("Result :")).check(text(containsString("Profile Sharing code")));
    }

}