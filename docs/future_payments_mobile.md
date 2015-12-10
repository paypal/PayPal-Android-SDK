Future Payments Mobile Integration
==================================

This section will show how to obtain a user's authorized consent for future payments using their PayPal account.

_If you haven't already, see the [README](../README.md) for an initial overview and instructions for adding the SDK to your project._


Overview
--------

Your app will use the mobile SDK to obtain two pieces of information at different times.

First, you must [obtain customer consent](#obtain-customer-consent) to take payments from their PayPal account. How this works:

* The PayPal Android SDK...
    1. Presents UI for your user to authenticate using their PayPal account.
    2. Asks your user to consent to [OAuth access token scope](http://tools.ietf.org/html/rfc6749#page-23) to use PayPal for future payments.
    3. Returns an OAuth2 authorization code to your app.
* Your app...
    1. Receives an OAuth2 authorization code from the SDK.
    2. Sends the authorization code to your server, which then [exchanges the code for OAuth2 access and refresh tokens](future_payments_server.md#obtain-oauth2-tokens).

Later, when initiating a pre-consented payment, you must [obtain a Client Metadata ID](#obtain-an-application-correlation-id). How this works:

* The PayPal Android SDK...
    * Provides a Cient Metadata ID.
* Your app...
    * Sends the client metadata ID and transaction information to your server.
    * Your server then [uses its OAuth2 tokens, Client Metadata ID, and transaction info to create a payment](future_payments_server.md).



Obtain Customer Consent
-----------------------

The sample app provides a more complete example. However, at minimum, you must:

1. Add PayPal Android SDK dependency to your `build.gradle` file as shown in README.md

1. Create a `PayPalConfiguration` object.  This object allows you to configure various aspects of the SDK.

	```java
	private static PayPalConfiguration config = new PayPalConfiguration()

			// Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
			// or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("<YOUR_CLIENT_ID>")
            
            // Minimally, you will need to set three merchant information properties.
    		// These should be the same values that you provided to PayPal when you registered your app.
            .merchantName("Example Store")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
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

3. Launch the PayPal Future Payment activity, for example, when a button is pressed:

    ```java
    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(SampleActivity.this, PayPalFuturePaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        
        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }
    ```

4. Implement `onActivityResult()`:

    ```java
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PayPalAuthorization auth = data
                    .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
            if (auth != null) {
                try {
                    String authorization_code = auth.getAuthorizationCode();

                    sendAuthorizationToServer(auth);

                } catch (JSONException e) {
                    Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("FuturePaymentExample", "The user canceled.");
        } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("FuturePaymentExample",
                    "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
        }
    }
    ```

5. Send the authorization response to your server in order to complete the process.

    ```java
    private void sendAuthorizationToServer(PayPalAuthorization authorization) {
        
        // TODO:
        // Send the authorization response to your server, where it can exchange the authorization code
        // for OAuth access and refresh tokens.
        //
        // Your server must then store these tokens, so that your server code can execute payments
        // for this user in the future.
        
    }
    ```

Obtain a Client Metadata ID
-----------------------

When initiating a pre-consented payment (a "future payment") from a mobile device, your mobile application must obtain a Client Metadata Id from the Mobile SDK to pass to your server. Your server must include this Client Metadata ID in its payment request sent to PayPal.

PayPal uses this Client Metadata ID to verify that the payment is originating from a valid, user-consented device+application. This helps reduce fraud and decrease declines. **PayPal does not provide any loss protection for transactions that do not correctly supply a Client Metadata ID.**

**Do not cache or store this value.**

Example:

```java
public void onFuturePaymentPurchasePressed(View pressed) {
// Get the Client Metadata ID from the SDK
String metadataId = PayPalConfiguration.getClientMetadataId(this);
        
// TODO: Send metadataId and transaction details to your server for processing with
// PayPal...
}
```


When your server makes its payment request to PayPal, it must include a `PayPal-Client-Metadata-Id` HTTP header with this Client Metadata ID value obtained from the SDK.


Next Steps
----------

Read [Future Payments Server-Side Integration](future_payments_server.md) to exchange the authorization code for OAuth2 tokens and create payments with an access token and a Metadata ID.

