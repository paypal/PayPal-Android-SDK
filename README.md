**Important**: PayPal Mobile SDKs are now Deprecated and only existing integrations are supported. For all new integrations, use [Braintree Direct](https://www.braintreepayments.com/products/braintree-direct) in [supported countries](https://www.braintreepayments.com/country-selection). In other countries, use [Express Checkout](https://developer.paypal.com/docs/accept-payments/express-checkout/ec-braintree-sdk/get-started/) and choose the Braintree SDK integration option.

PayPal Android SDK
==================

The PayPal Android SDK makes it easy to add PayPal payments to mobile apps.

*This documentation is available in Japanese: [日本語のドキュメント](docs/ja/README.md).*

## Contents

- [Use Cases](#use-cases)
- [Integration with the PayPal Wallet App](#integration-with-the-paypal-wallet-app)
- [Requirements](#requirements)
- [Add the SDK to Your Project](#add-the-sdk-to-your-project)
- [Credentials](#credentials)
- [International Support](#international-support)
- [Disabling Direct Credit Card Payments](#disabling-direct-credit-card-payments)
- [Override `minSdkVersion`](#override-minsdkversion)
- [Testing](#testing)
- [Documentation](#documentation)
- [Usability](#usability)
- [Next Steps](#next-steps)
- [Contributing](#contributing)
- [License](#license)

## Add the SDK to Your Project

The PayPal Android SDK is now available at [Maven Repository](https://repo1.maven.org/maven2/com/paypal/sdk/paypal-android-sdk/). The latest version is available via `mavenCentral()`:

```groovy
compile 'com.paypal.sdk:paypal-android-sdk:2.16.0'
```


## Use Cases

The SDK supports two use cases for making payments - **Single Payment** and **Future Payments** - and a third use case for obtaining information about the customer - **Profile Sharing**.


### Single Payment

Receive a one-time payment from a customer's PayPal account or payment card (scanned with [card.io](https://www.card.io/)). This can be either (1) an **immediate** payment which your servers should subsequently **verify**, or (2) an **authorization** for a payment which your servers must subsequently **capture**, or (3) a payment for an **order** which your servers must subsequently **authorize** and **capture**:

1. [Accept a Single Payment](docs/single_payment.md) and receive back a proof of payment.
2. On your server, [Verify the Payment](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/), [Capture the Payment](https://developer.paypal.com/webapps/developer/docs/integration/direct/capture-payment/#capture-the-payment), or [Process the Order](https://developer.paypal.com/webapps/developer/docs/integration/direct/create-process-order/) (PayPal Developer site) using PayPal's API.

*Note:* Direct Credit Card (DCC) payments are now deprecated in this SDK.  Please use [Braintree Payments](https://www.braintreepayments.com/), a PayPal Company, which is the easiest way to accept PayPal, credit cards, and many other payment methods.  All new integrations should [disable direct credit card payments](#disabling-direct-credit-card-payments).


### Future Payments

Your customer logs in to PayPal just one time and consents to future payments:

1. [Obtain Customer Consent](docs/future_payments_mobile.md#obtain-customer-consent) to receive an authorization code.
2. On your server, use this authorization code to [Obtain OAuth2 Tokens](docs/future_payments_server.md#obtain-oauth2-tokens).

Later, when that customer initiates a payment:

1. [Obtain a Client Metadata ID](docs/future_payments_mobile.md#obtain-an-application-correlation-id) that you'll pass to your server.
2. On your server, [Create a Payment](docs/future_payments_server.md#create-a-payment) using your OAuth2 tokens, the Client Metadata ID, and PayPal's API.

### Profile Sharing

Your customer logs in to PayPal and consents to PayPal sharing information with you:

1. [Obtain Customer Consent](docs/profile_sharing_mobile.md#obtain-customer-consent) to receive an authorization code.
2. On your server, use this authorization code to [Obtain OAuth2 Tokens](docs/profile_sharing_server.md#obtain-oauth2-tokens).
3. On your server, [Retrieve Customer Information](docs/profile_sharing_server.md#retrieve-customer-information) using your OAuth2 tokens and PayPal's API.


## Integration with the PayPal Wallet App

The SDK will now use the newest version of the PayPal Wallet App if present on the device to log in to a customer account.  No additional configuration is required to enable this feature.  This integration enables device-specific PayPal [FIDO](https://fidoalliance.org/) integrations, including login by fingerprint on the Galaxy S5.  In addition, a user who logged in to the PayPal Wallet App and checked "Keep me logged in" may not need to log-in again when paying with your app.  For more information on how this all works, please read the [blog post](http://www.embedded.com/design/real-world-applications/4430305/Implementing-Android-based-fingerprint-authentication-for-online-payments) from one of our architects.

### Limitations

* The integration will _not_ be enabled in any of the [testing](#testing) modes, as the Wallet app does not support this developer testing environment.

## Requirements

* Android 4.1.x (API 16) or later
* Phone or tablet

## Credentials

Your mobile integration requires different `client_id` values for each environment: Live and Test (Sandbox).

Your server integrations for verifying or creating payments will also require the corresponding `client_secret` for each `client_id`.

You can obtain these PayPal API credentials by visiting the [Applications page on the PayPal Developer site](https://developer.paypal.com/webapps/developer/applications) and logging in with your PayPal account.

### Sandbox

Once logged in on this Applications page, you will be assigned **test credentials**, including Client ID, which will let you test your Android integration against the PayPal Sandbox.

While testing your app, when logging in to PayPal in the SDK's UI you should use a *personal* Sandbox account email and password. I.e., not your Sandbox *business* credentials.

You can create both business and personal Sandbox accounts on the [Sandbox accounts](https://developer.paypal.com/webapps/developer/applications/accounts) page.

#### Sandbox and TLSv1.2

PayPal will be upgrading the endpoint that the PayPal Android SDK uses to communicate with PayPal servers on Jan 18th, 2016.  If you're testing on sandbox with a version of the PayPal Android SDK older than 2.13.0, then you'll start seeing communication failures when using Android devices >= API 16, and < API 20.  Please upgrade to a version [2.13.0](https://github.com/paypal/PayPal-Android-SDK/releases) or higher to fix these errors.

If you're testing on a device older than API 16, Android will not be able to communicate with PayPal, no matter what version of the SDK you use.

These TLS changes coincides with the TLSv1.2 security mandate outlined [here](https://www.paypal-knowledge.com/infocenter/index?page=content&widgetview=true&id=FAQ1914&viewlocale=en_US), and will be followed by a similar change to the Production endpoints at some later date.  For any questions or concerns, please [create an issue](https://github.com/paypal/PayPal-Android-SDK/issues/).

### Live

To obtain your **live** credentials, you will need to have a business account. If you don't yet have a business account, there is a link at the bottom of that same Applications page that will get you started.


## International Support

### Localizations

The SDK has built-in translations for many languages and locales. See [javadoc](http://paypal.github.io/PayPal-Android-SDK/) files for a complete list.

### Currencies

The SDK supports multiple currencies. See [the REST API country and currency documentation](https://developer.paypal.com/webapps/developer/docs/integration/direct/rest_api_payment_country_currency_support/) for a complete, up-to-date list.

## Disabling Direct Credit Card Payments

Disabling Direct Credit Card Payments is now preferred. To completely disable Direct Credit Card (DCC) payments, exclude the card.io library in your application `build.gradle`:
```groovy
dependencies {
    compile('com.paypal.sdk:paypal-android-sdk:2.16.0') {
        exclude group: 'io.card'
    }
}
```

## Override `minSdkVersion`

As of release `2.14.0`, the `minSdkVersion` has been increased to 16. If you prefer to have your app on a lower `minSdkVersion` and want to leverage the latest SDK, please disable PayPal for versions below API 16, add `xmlns:tools="http://schemas.android.com/tools` inside the manifest's xml declaration, and add the following snippet to your `AndroidManifest.xml`: 
   
```xml
<uses-sdk android:minSdkVersion="INSERT_YOUR_DESIRED_minSdkVersion_HERE" tools:overrideLibrary="com.paypal.android.sdk.payments"/>
```

See the [Android manifest merger docs](http://tools.android.com/tech-docs/new-build-system/user-guide/manifest-merger) for more information.

## Testing

During development, use `environment()` in the `PayPalConfiguration` object to change the environment.  Set it to either `ENVIRONMENT_NO_NETWORK` or `ENVIRONMENT_SANDBOX` to avoid moving real money.


## Documentation

* These docs in the SDK, which include an overview of usage, step-by-step integration instructions, and sample code.
* The sample app included in this SDK.
* There are [javadocs](http://paypal.github.io/PayPal-Android-SDK/) available.
* The [PayPal Developer Docs](https://developer.paypal.com/docs), which cover error codes and server-side integration instructions.


## Usability

User interface appearance and behavior is set within the library itself. For the sake of usability and user experience consistency, apps should not attempt to modify the SDK's behavior beyond the documented methods.


## Next Steps

Depending on your use case, you can now:

* [Accept a single payment](docs/single_payment.md)
* [Obtain user consent](docs/future_payments_mobile.md) to [create future payments](docs/future_payments_server.md).


## Contributing

Please read our [contributing guidelines](CONTRIBUTING.md) prior to submitting a Pull Request.

## License

Please refer to this repo's [license file](LICENSE).
