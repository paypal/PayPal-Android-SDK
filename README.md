PayPal Android SDK
==================

The PayPal Android SDK provides a software library that makes it easy for Android developers
to accept both credit cards and PayPal directly within native mobile apps.

For usage and integration instructions, see the
[PayPal Android SDK Integration Guide](https://developer.paypal.com/webapps/developer/docs/integration/mobile/android-integration-guide/).

For detailed reference, see the [SDK reference](http://paypal.github.io/PayPal-Android-SDK/).


### A note about older libraries


PayPal is in the process of replacing the older "Mobile Payments Libraries" (MPL) with the new PayPal Android and iOS SDKs. 
The new Mobile SDKs are based on the PayPal REST API, while the older MPL uses the Adaptive Payments API.

Until features such as third-party, parallel, and chained payments are available, you can use MPL:

 - [MPL on GitHub](https://github.com/paypal/sdk-packages/tree/gh-pages/MPL)
 - [MPL Documentation](https://developer.paypal.com/webapps/developer/docs/classic/mobile/gs_MPL/)

Issues related to MPL should be filed in the [sdk-packages repo](https://github.com/paypal/sdk-packages/).

Developers with existing Express Checkout integrations or who want additional features such as 
authorization and capture, may wish to use [Mobile Express Checkout](https://developer.paypal.com/webapps/developer/docs/classic/mobile/gs_MEC/)
in a webview.


Integration
-----------


### Overview

* The PayPal Android SDK...
    1. Takes care of the UI to gather payment information from the user.
    2. Coordinates payment with PayPal.
    3. Returns to you a proof of payment.
* Your code...
    1. Receives proof of payment from the PayPal Android SDK.
    2. [Sends proof of payment to your servers for verification](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/).
    3. Provides the user their goods or services.


### Requirements

* Android 2.2 or later
* card.io card scanning available only on armv7 devices
* Phone or tablet


### Initial setup

1. Download or clone this repo. The SDK includes a .jar, static libraries, release notes, and license acknowledgements. It also includes a sample app.
2. Copy the contents of the SDK `libs` directory into your project's `libs` directory. The path to these files is important; if it is not exactly correct, the SDK will not work.
3. Add the open source license acknowledgments from `acknowledgments.md` to your app's acknowledgments.


### Credentials

Take note of these two identifiers:

  - `clientId`: Available on the [PayPal developer site](https://developer.paypal.com/).
  - `receiverEmail`: The email address on the PayPal account used to obtain the above `clientId`.

### Android Manifest

You will need to include the following in your project's `AndroidManifest.xml`.

1. Ensure your minimum SDK level is 8 or above. You should have an element like this in `<manifest>`:

    ```xml
    <uses-sdk android:minSdkVersion="8" android:targetSdkVersion="17" />
    ```

2. Request the required permissions, also in `<manifest>`:

    ```xml
    <!-- for card.io card scanning -->
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.VIBRATE" />

    <!-- for most things, including card.io and paypal -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />

    <!-- Camera features -->
    <uses-feature android:name="android.hardware.camera" android:required="false" />
    <uses-feature android:name="android.hardware.camera.autofocus" android:required="false" />
    <uses-feature android:name="android.hardware.camera.flash" android:required="false" />
    ```

3. Add the SDK's service and activities to your `<application>`:

    ```xml
    <service android:name="com.paypal.android.sdk.payments.PayPalService" android:exported="false"/>

    <activity android:name="com.paypal.android.sdk.payments.PaymentActivity" />
    <activity android:name="com.paypal.android.sdk.payments.LoginActivity" />
    <activity android:name="com.paypal.android.sdk.payments.PaymentMethodActivity" />
    <activity android:name="com.paypal.android.sdk.payments.PaymentConfirmActivity" />

    <activity
        android:name="io.card.payment.CardIOActivity"
        android:configChanges="keyboardHidden|orientation" />
    <activity android:name="io.card.payment.DataEntryActivity" />
    ```

  4. If you're using ProGuard, add the following to your ProGuard config (e.g. `proguard-project.txt`):
    ```
    @proguard-paypal.cnf
    ```


## Sample Code

The sample app provides a more complete example. However, at minimum, you must:

1. Start the payment service when your activity is created and stop it upon destruction:

    ```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, PayPalService.class);

        // live: don't put any environment extra
        // sandbox: use PaymentActivity.ENVIRONMENT_SANDBOX
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);

        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "<YOUR_CLIENT_ID>");

        startService(intent);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    ```

2. Create the payment and launch the payment intent, for example, when a button is pressed:

    ```java
    public void onBuyPressed(View pressed) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal("8.75"), "USD", "hipster jeans");

        Intent intent = new Intent(this, PaymentActivity.class);

        // comment this line out for live or set to PaymentActivity.ENVIRONMENT_SANDBOX for sandbox
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);

        // it's important to repeat the clientId here so that the SDK has it if Android restarts your
        // app midway through the payment UI flow.
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "credential-from-developer.paypal.com");

        // Provide a payerId that uniquely identifies a user within the scope of your system,
        // such as an email address or user ID.
        intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "<someuser@somedomain.com>");

        intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, "<YOUR_PAYPAL_EMAIL_ADDRESS>");
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }
    ```

3. Implement `onActivityResult()`:

    ```java
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
            Log.i("paymentExample", "An invalid payment was submitted. Please see the docs.");
        }
    }
    ```

4. [Send the proof of payment to your servers for verification](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/),
     as well as any other data required for your business, such as fulfillment information.


## Testing

During development, set the intent extra `EXTRA_PAYPAL_ENVIRONMENT` to `ENVIRONMENT_NO_NETWORK` or `ENVIRONMENT_SANDBOX` to avoid moving real money.

## International Support

### Localizations

The SDK has built-in translations for many languages and locales. See [javadocs](http://paypal.github.io/PayPal-Android-SDK/) files for a complete list.

### Currencies

The SDK supports multiple currencies. See the [REST API country and currency documentation](https://developer.paypal.com/webapps/developer/docs/integration/direct/rest_api_payment_country_currency_support/) for a complete, up-to-date list.

Note that currency support differs for credit card versus PayPal payments. Unless you disable credit card acceptance (via the `PaymentActivity.EXTRA_SKIP_CREDIT_CARD` intent extra), we recommend limiting transactions to currencies supported by both payment types. Currently these are: USD, GBP, CAD, EUR, JPY.

If your app initiates a transaction with a currency that turns out to be unsupported for the user's selected payment type, then the SDK will display an error to the user and write a message to the console log.

## Hints & Tips

* **Avoid fraud!** Be sure to [verify the proof of payment](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/).
* Mobile networks are unreliable. Save the proof of payment to make sure it eventually reaches your server.
* Refer to the [SDK reference](http://paypal.github.io/PayPal-Android-SDK/) as needed for extra details about any given property or parameter.
* Some tools (including IntelliJ) will require that you add `libs/PayPalSDK.jar` to their dependencies path.
