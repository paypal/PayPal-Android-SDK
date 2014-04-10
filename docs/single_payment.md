Single Payment
==============

Receive a single, immediate payment from your customer, via either PayPal or payment card with [card.io](https://www.card.io/).

_If you haven't already, see the [README](../README.md) for an initial overview and instructions for adding the SDK to your project._

Overview
--------

* The PayPal Android SDK...
    1. Presents UI to gather payment information from the user.
    2. Coordinates payment with PayPal.
    3. Returns a proof of payment to your app.
* Your code...
    1. Receives proof of payment from the PayPal Android SDK.
    2. [Sends proof of payment to your servers for verification](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/).
    3. Provides the user their goods or services.


Sample Code
-----------

The sample app provides a more complete example. However, at minimum, you must:

1. Add permissions to your `AndroidManifest.xml` file:
    ```xml
    <!-- for card.io card scanning -->
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.VIBRATE" />
    
    <uses-feature android:name="android.hardware.camera" android:required="false" />
    <uses-feature android:name="android.hardware.camera.autofocus" android:required="false" />

    <!-- for most things, including card.io & paypal -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    ```
    
1. Declare SDK service and activities in your `AndroidManifest.xml` file:
    ```xml
    <service android:name="com.paypal.android.sdk.payments.PayPalService"
            android:exported="false" />
        
    <activity android:name="com.paypal.android.sdk.payments.PaymentActivity" />
    <activity android:name="com.paypal.android.sdk.payments.LoginActivity" />
    <activity android:name="com.paypal.android.sdk.payments.PaymentMethodActivity" />
    <activity android:name="com.paypal.android.sdk.payments.PaymentConfirmActivity" />
    <activity android:name="com.paypal.android.sdk.payments.PayPalFuturePaymentActivity" />
    <activity android:name="com.paypal.android.sdk.payments.FuturePaymentConsentActivity" />
    <activity android:name="com.paypal.android.sdk.payments.FuturePaymentInfoActivity" />
    <activity android:name="io.card.payment.CardIOActivity"
              android:configChanges="keyboardHidden|orientation" />
    <activity android:name="io.card.payment.DataEntryActivity" />
    ```

1. Create a `PayPalConfiguration` object
    ```java
    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("<YOUR_CLIENT_ID>");
    ```

2. Start `PayPalService` when your activity is created and stop it upon destruction:

    ```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    ```

3. Create the payment and launch the payment intent, for example, when a button is pressed:

    ```java
    public void onBuyPressed(View pressed) {
        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to PAYMENT_INTENT_AUTHORIZE to only authorize payment and 
        // capture funds later.

        PayPalPayment payment = new PayPalPayment(new BigDecimal("1.75"), "USD", "hipster jeans",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }
    ```

4. Implement `onActivityResult()`:

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
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
    ```

5. [Send the proof of payment to your servers for verification](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/),
   as well as any other processing required for your business, such as fulfillment.

   **Tip:** At this point, the payment has been completed, and the user
   has been charged. If you can't reach your server, it is important that you save the proof
   of payment and try again later.

### Hint

Mobile networks are unreliable. Save the proof of payment to make sure it eventually reaches your server.

Next Steps
----------

**Avoid fraud!** Be sure to [verify the proof of payment](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/).
