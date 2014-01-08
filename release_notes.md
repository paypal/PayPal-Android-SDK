PayPal Android SDK release notes
================================

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

