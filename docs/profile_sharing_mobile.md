Profile Sharing Mobile Integration
==================================

This section will show how to obtain a user's authorized consent for profile sharing from their PayPal account.

_If you haven't already, see the [README](../README.md) for an initial overview and instructions for adding the SDK to your project._


Overview
--------

You must [obtain customer consent](#obtain-customer-consent) to share information from their PayPal account. How this works:

* On the PayPal Developer site...
    1. Specify the pieces of information that you wish your customers to share with you.
* The PayPal Android SDK...
    1. Presents UI for your user to authenticate using their PayPal account.
    2. Asks your user to consent to [OAuth access token scope](http://tools.ietf.org/html/rfc6749#page-23) to use PayPal for profile sharing.
    3. Returns an OAuth2 authorization code to your app.
* Your app...
    1. Specifies the required scopes in SDK request
    2. Receives an OAuth2 authorization code from the SDK.
    3. Sends the authorization code to your server, which then [exchanges the code for OAuth2 access and refresh tokens](profile_sharing_server.md#obtain-oauth2-tokens).
    4. Your server then [uses its OAuth2 tokens to request the relevant customer information from PayPal](profile_sharing_server.md).

**Notes:**  
1. See `PayPalOAuthScopes` for a complete list of scope-values available to the PayPal Android SDK.  
2. See [Log In with PayPal user attributes](https://developer.paypal.com/docs/integration/direct/identity/attributes/) for a complete list of available scope attributes.


Specify Desired Information for Sharing
---------------------------------------

1. Log in to the [PayPal Developer site](https://developer.paypal.com).
2. Select [your app](https://developer.paypal.com/webapps/developer/applications/myapps).
3. Under `APP CAPABILITIES` select `Log In with PayPal`, and click `Advanced options`.
4. Under `Information requested from customers` select the items ("scope attributes") you wish to have shared.
5. If you provide Privacy Policy and User Agreement URLs under `Links shown on customer consent page`, these will override the corresponding  URLs that you provide below in the [`PayPalConfiguration` object](#obtain-customer-consent).


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

3. Launch the PayPal Profile Sharing activity, for example, when a button is pressed:

    ```java
    public void onProfileSharingPressed(View pressed) {
        Intent intent = new Intent(SampleActivity.this, PayPalProfileSharingActivity.class);
        
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());
        
        startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING);
    }
    ```
    
    The `PayPalOAuthScopes` are initialized with a set of scope names:
    
    ```java
    private PayPalOAuthScopes getOauthScopes() {
        /* create the set of required scopes
         * Note: see https://developer.paypal.com/docs/integration/direct/identity/attributes/ for mapping between the
         * attributes you select for this app in the PayPal developer portal and the scopes required here.
         */
        Set<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
        return new PayPalOAuthScopes(scopes);
    }
    ```

4. Implement `onActivityResult()`:

    ```java
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PayPalAuthorization auth = data
                    .getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
            if (auth != null) {
                try {
                    String authorization_code = auth.getAuthorizationCode();

                    sendAuthorizationToServer(auth);

                } catch (JSONException e) {
                    Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("ProfileSharingExample", "The user canceled.");
        } else if (resultCode == PayPalProfileSharingActivity.RESULT_EXTRAS_INVALID) {
            Log.i("ProfileSharingExample",
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
        // Your server must then store these tokens, so that your server code can use it
        // for getting user profile data in the future.
        
    }
    ```


Next Steps
----------

Read [Profile Sharing Server-Side Integration](profile_sharing_server.md) to exchange the authorization code for OAuth2 tokens and retrieve the customer information from PayPal.
