PayPal Android SDK release notes
================================

2.0.0
-----
* Add Future Payment with PayPal support. Users can now authenticate and consent within an app using the SDK.  A user no longer needs to repeatedly enter credentials.
	* Introduce a `PayPalFuturePaymentActivity`, which returns a `PayPalAuthorization` object.
* Changes to payment feature:
	* Change backend to use PayPal's new REST APIs for all SDK functions.  Now there is a single way to verify both credit card and PayPal payments.
	* Introduce `PayPalPaymentDetails` to support payment details, including line-item subtotal amount, tax amount, and shipping amount.
	* Single payments now include a payment`intent` to distinguish between:
		1. immediate payment processing
		2. payment authorization only, with subsequent payment capture from the merchant's server.
* Use `PayPalConfiguration` object for common configuration across both single payment and future payment use cases.
* For API errors, logging will now provide additional information, including a PayPal Debug-ID for MTS investigations.
* Add support for directional controller interaction (for set-top boxes, game consoles, etc.).
* Resolves issues with PayPal user passwords containing special characters.

1.2.6
-----
* Fix issue where PaymentActivity.EXTRA_DEFAULT_USER_EMAIL was not being handled properly in all cases [47](https://github.com/paypal/PayPal-Android-SDK/issues/47)

1.2.5
-----
* Refactor code to eliminate spurious error message in LogCat [40](https://github.com/paypal/PayPal-Android-SDK/issues/40)
* Fix validation of PayPalPayment.shortDescription where some credit card payments caused incorrect JSONException [41](https://github.com/paypal/PayPal-Android-SDK/issues/41)
* Eliminate source of potential NPE [37](https://github.com/paypal/PayPal-Android-SDK/issues/37)
* Update card.io lib to 3.1.4 (includes fixes for potential NPEs)

1.2.4
-----
* Fix NumberFormatException on some payments in locales that use comma for decimal separator [34](https://github.com/paypal/PayPal-Android-SDK/issues/34)

1.2.3
-----
* Fix issue where invalid currency amounts could be sent to the PayPal server.

1.2.2
-----
* Fix login page layout issue [20](https://github.com/paypal/PayPal-Android-SDK/issues/20)
* Add some login error logging

1.2.1
-----
* Update card.io library to 3.1.3
* Fix issue [#11: release/debug UI differences](https://github.com/paypal/PayPal-Android-SDK/issues/11)

1.2.0
-----
* Eliminate the final "Complete" screen
* Fix Hebrew phone settings detection bug
* Update card.io library to 3.1.2

1.1.2
-----
* Re-add `Version` class
* Update card.io library to 3.1.1
* Support all currencies that are curently accepted by the REST APIs.  See [README](README.md) for details.
* Fix various localizations
* Additional localization: ko (Korean)
* Minor UI cleanup (including [issue 4](https://github.com/paypal/PayPal-Android-SDK/issues/4))

1.1.0
-----
* Bug fixes
* Update card.io to 3.1.0
* Add translations of all strings into ~20 languages, in addition to American English.
    - Translation choice is controlled by EXTRA_LANGUAGE_OR_LOCALE in PaymentActivity 
    - The translations that a few developers had previously created for their own apps will no longer be used by the SDK.
    - NOTE: Default language, if not set by your app, will now be based upon the device's current language setting.


1.0.3
-----
* Bug fixes

1.0.2
-----
* Several small fixes & improvements


1.0.1
-----
* Update card.io to 3.0.5
* Minor UI improvements
* Other small fixes


1.0.0
-----
* First release!

