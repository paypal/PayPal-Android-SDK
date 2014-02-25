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
        PayPalPayment payment = new PayPalPayment(new BigDecimal("8.75"), "USD", "hipster jeans");

        Intent intent = new Intent(this, PaymentActivity.class);

        // Provide a payerId that uniquely identifies a user within the scope of your system,
        // such as an email address or user ID.
        intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "<someuser@somedomain.com>");

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
        else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
            Log.i("paymentExample", "An invalid payment was submitted. Please see the docs.");
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
