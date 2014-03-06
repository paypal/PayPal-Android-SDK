PayPal Android SDK
==================

The PayPal Android SDK makes it easy to add PayPal and credit card payments to mobile apps.

## Contents

- [Use Cases](#use-cases)
- [Requirements](#requirements)
- [Add the SDK to Your Project](#add-the-sdk-to-your-project)
- [Credentials](#credentials)
- [International Support](#international-support)
- [Testing](#testing)
- [Documentation](#documentation)
- [Usability](#usability)
- [Moving to PayPal Android SDK 2.0](#moving-to-paypal-android-sdk-20)
- [Next Steps](#next-steps)


## Use Cases

The SDK supports two use cases: **Single Payment** and **Future Payments**.


### Single Payment

Receive immediate payment from a customer's PayPal account or payment card (scanned with [card.io](https://www.card.io/)):

1. [Accept a Single Payment](docs/single_payment.md) and receive back a proof of payment.
2. On your server, [Verify the Payment](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/) (PayPal Developer site) using PayPal's API.


### Future Payments

Your customer logs in to PayPal just one time and consents to future payments:

1. [Obtain Customer Consent](docs/future_payments_mobile.md#obtain-customer-consent) to receive an authorization code.
2. On your server, use this authorization code to [Obtain OAuth2 Tokens](docs/future_payments_server.md#obtain-oauth2-tokens).

Later, when that customer initiates a payment:

1. [Obtain an Application Correlation ID](docs/future_payments_mobile.md#obtain-an-application-correlation-id) that you'll pass to your server.
2. On your server, [Create a Payment](docs/future_payments_server.md#create-a-payment) using your OAuth2 tokens, the Application Correlation ID, and PayPal's API.



## Requirements

* Android 2.2 or later
* card.io card scanning available only on armv7 devices
* Phone or tablet


## Initial setup

1. Download or clone this repo. The SDK includes a .jar, static libraries, release notes, and license acknowledgements. It also includes a sample app.
2. Copy the contents of the SDK `libs` directory into your project's `libs` directory. The path to these files is important; if it is not exactly correct, the SDK will not work.
3. Add the open source license acknowledgments from `acknowledgments.md` to your app's acknowledgments.


## Credentials

Your mobile integration requires different `client_id` values for each environment: Live and Test (Sandbox).

Your server integrations for verifying or creating payments will also require the corresponding `client_secret` for each `client_id`.

You can obtain these PayPal API credentials by visiting the [Applications page on the PayPal Developer site](https://developer.paypal.com/webapps/developer/applications) and logging in with your PayPal account.

### Sandbox

Once logged in on this Applications page, you will be assigned **test credentials**, including Client ID, which will let you test your Android integration against the PayPal Sandbox.

While testing your app, when logging in to PayPal in the SDK's UI you should use a *personal* Sandbox account email and password. I.e., not your Sandbox *business* credentials.

You can create both business and personal Sandbox accounts on the [Sandbox accounts](https://developer.paypal.com/webapps/developer/applications/accounts) page.

### Live

To obtain your **live** credentials, you will need to have a business account. If you don't yet have a business account, there is a link at the bottom of that same Applications page that will get you started.


## International Support

### Localizations

The SDK has built-in translations for many languages and locales. See [javadoc](http://htmlpreview.github.io/?https://github.com/paypal/PayPal-Android-SDK/blob/gh-pages/index.html) files for a complete list.

### Currencies

The SDK supports multiple currencies. See [the REST API country and currency documentation](https://developer.paypal.com/webapps/developer/docs/integration/direct/rest_api_payment_country_currency_support/) for a complete, up-to-date list.

Note that currency support differs for credit card versus PayPal payments. Unless you disable credit card acceptance (via the `PaymentActivity.EXTRA_SKIP_CREDIT_CARD` intent extra), **we recommend limiting transactions to currencies supported by both payment types.** Currently these are: USD, GBP, CAD, EUR, JPY.

If your app initiates a transaction with a currency that turns out to be unsupported for the user's selected payment type, then the SDK will display an error to the user and write a message to the console log.


## Testing

During development, use `environment()` in the `PayPalConfiguration` object to change the environment.  Set it to either `ENVIRONMENT_NO_NETWORK` or `ENVIRONMENT_SANDBOX` to avoid moving real money.


## Documentation

* These docs in the SDK, which include an overview of usage, step-by-step integration instructions, and sample code.
* The sample app included in this SDK.
* There are [javadocs](http://htmlpreview.github.io/?https://github.com/paypal/PayPal-Android-SDK/blob/gh-pages/index.html) available.
* The [PayPal Developer Docs](https://developer.paypal.com/docs), which cover error codes and server-side integration instructions.


## Usability

User interface appearance and behavior is set within the library itself. For the sake of usability and user experience consistency, apps should not attempt to modify the SDK's behavior beyond the documented methods.


## Moving to PayPal Android SDK 2.0


### Upgrade from 1.x

As a major version change, the API introduced in 2.0 is not backward compatible with 1.x integrations. However, the SDK still supports all previous single payment functionality. Upgrading is straightforward.

* Most of the non-payment-specific extras of `PayPalPaymentActivity` have been moved to the `PayPalConfiguration` class, and the service startup has changed to take such a configuration object.



### Older Libraries

PayPal is in the process of replacing the older "Mobile Payments Libraries" (MPL) with the new PayPal Android and iOS SDKs.
The new Mobile SDKs are based on the PayPal REST API, while the older MPL uses the Adaptive Payments API.

Until features such as third-party, parallel, and chained payments are available, if needed, you can use MPL:

 - [MPL on GitHub](https://github.com/paypal/sdk-packages/tree/gh-pages/MPL)
 - [MPL Documentation](https://developer.paypal.com/webapps/developer/docs/classic/mobile/gs_MPL/)

Issues related to MPL should be filed in the [sdk-packages repo](https://github.com/paypal/sdk-packages/).

Developers with existing Express Checkout integrations or who want additional features may wish to use [Mobile Express Checkout](https://developer.paypal.com/webapps/developer/docs/classic/mobile/gs_MEC/)
in a webview.


## Next Steps

Depending on your use case, you can now:

* [Accept a single payment](docs/single_payment.md)
* [Obtain user consent](docs/future_payments_mobile.md) to [create future payments](docs/future_payments_server.md).
