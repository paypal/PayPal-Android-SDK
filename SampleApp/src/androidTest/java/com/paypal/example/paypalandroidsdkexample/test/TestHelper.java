package com.paypal.example.paypalandroidsdkexample.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.text.format.DateFormat;
import android.view.View;

import com.lukekorth.deviceautomator.DeviceAutomator;
import com.lukekorth.deviceautomator.UiObjectMatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static com.lukekorth.deviceautomator.AutomatorAction.click;
import static com.lukekorth.deviceautomator.AutomatorAction.setText;
import static com.lukekorth.deviceautomator.DeviceAutomator.onDevice;
import static com.lukekorth.deviceautomator.UiObjectMatcher.withContentDescription;
import static com.lukekorth.deviceautomator.UiObjectMatcher.withText;

/**
 * This helper class provides with methods to help write better tests.
 */
public abstract class TestHelper {

    public static final String PAYPAL_SAMPLE_APP_PACKAGE_NAME = "com.paypal.example.paypalandroidsdkexample.debug";
    public static final String PAYPAL_SANDBOX_USERNAME = "sample@buy.com";
    public static final String PAYPAL_SANDBOX_PASSWORD = "123123123";

    public static void setup() {
        PreferenceManager.getDefaultSharedPreferences(getTargetContext())
                .edit()
                .clear()
                .commit();
        onDevice().onHomeScreen().launchApp(PAYPAL_SAMPLE_APP_PACKAGE_NAME);
    }

    public static DeviceAutomator onDeviceWithScreenShot() {
        DeviceAutomator result = onDevice();
        takeScreenshot(null);
        return result;
    }

    public static DeviceAutomator onDeviceWithScreenShot(UiObjectMatcher matcher) {
        DeviceAutomator result = onDevice(matcher);
        takeScreenshot(null);
        return result;
    }

    public static void takeScreenshot(String name) {
        if (name == null) {
            Date now = new Date();
            DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
            name = String.valueOf(now.getTime());
        }
        // In Testdroid Cloud, taken screenshots are always stored
        // under /test-screenshots/ folder and this ensures those screenshots
        // be shown under Test Results
        String path =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/test-screenshots/" + name + ".png";
        OutputStream out = null;

        try {
            View scrView = getActivityInstance().getWindow().getDecorView().getRootView();
            scrView.setDrawingCacheEnabled(true);
            Bitmap bm = scrView.getDrawingCache();
            Bitmap bitmap = Bitmap.createBitmap(bm);
            scrView.setDrawingCacheEnabled(false);

            File imageFile = new File(path);
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
        } catch (Exception ex) {
            // Ignore any exceptions as this is just for debugging purposes
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception ex) {
                // Nothing to do here
            }
        }
    }

    public static Activity getActivityInstance() {
        final Activity[] currentActivity = new Activity[1];
        getInstrumentation().waitForIdleSync();
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> iterator = resumedActivities.iterator();
                while (iterator.hasNext()) {
                    currentActivity[0] = (Activity) iterator.next();
                }
            }
        });
        return currentActivity[0];
    }

    public static void onLogin() {
        onDeviceWithScreenShot(withContentDescription("Email")).perform(click(), setText(PAYPAL_SANDBOX_USERNAME));
        onDeviceWithScreenShot().pressDPadDown().typeText(PAYPAL_SANDBOX_PASSWORD);
        onDeviceWithScreenShot(withText("Log In")).perform(click());
    }

    public static void onAgree() {
        try {
            onDeviceWithScreenShot(withText("Agree")).perform(click());
        } catch (RuntimeException ex) {
            // This may mean, he has already agreed to it before.
        }
    }
}
