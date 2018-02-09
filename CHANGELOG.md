PayPal Android SDK release notes
================================

2.16.0
------
* Return transactionId on success [#402](https://github.com/paypal/PayPal-Android-SDK/issues/402).
* Update translations.
* Update SSL pinning cerificates.

2.15.3
------
* Update risk-component to 3.5.7.

2.15.2
------
* Add mandatory res folder in aar during release [#383](https://github.com/paypal/PayPal-Android-SDK/issues/383).
* Updated card.io to 5.5.0.
* Updated okhttp to 3.6.0.

2.15.1
------
* Removed trustall trustmanager to resolve google play security issue [#364](https://github.com/paypal/PayPal-Android-SDK/issues/364).
* Shows amount properly in all devices [#357](https://github.com/paypal/PayPal-Android-SDK/issues/357).

2.15.0
------
* Add support for third-party receivers [iOS #140](https://github.com/paypal/PayPal-iOS-SDK/issues/140). Available as an optional property, `PayPalPayment.payeeEmail()`. This property is only available for PayPal payments, not Direct Credit Card (DCC) payments.
* Direct Credit Card (DCC) payments are now deprecated in this SDK.  Please use [Braintree Payments](https://www.braintreepayments.com/), a PayPal Company, which is the easiest way to accept PayPal, credit cards, and many other payment methods. All new integrations should [disable direct credit card payments](README.md#disabling-direct-credit-card-payments).
* Update card.io to 5.4.2.

2.14.6
------
* Fix issue where okhttp cannot find the `TrustManager` [#341](https://github.com/paypal/PayPal-Android-SDK/issues/341).
* Update card.io to 5.4.1.

2.14.5
------
* Update okhttp dependency to 3.4.1.
* Fix crash when app does not have READ_PHONE_STATE permission [#321](https://github.com/paypal/PayPal-Android-SDK/issues/321).

2.14.4
------
* Minor bug fixes.
* Updated gradle version to 2.14.
* Include `org.json.*` exceptions in default proguard file [#299](https://github.com/paypal/PayPal-Android-SDK/issues/299).

2.14.3
------
* Update card.io to 5.4.0.
* Update okhttp dependency to 3.3.1.

2.14.2
------
* Added a new Kotlin SampleApp!
* Update card.io to 5.3.4.
* Update build tools.

2.14.1
------
* Update card.io to 5.3.2.
* Add proguard config to aar file.
* Minor bug fixes.

2.14.0
------
* Update `minSdkVersion` to 16.  This is the minimum Android version to communicate over TLSv1.2, which is required to support [a Payment Card Industry (PCI) Council mandate](http://blog.pcisecuritystandards.org/migrating-from-ssl-and-early-tls). All organizations that handle credit card information are required to comply with this standard. As part of this obligation, [PayPal is updating its services](https://github.com/paypal/tls-update) to require TLSv1.2 for all HTTPS connections. To override the minSdkVersion, please see [the readme](README.md#override-minsdkversion).
* Update okhttp dependency to 3.2.0.
* Fix issue related to non-ascii characters in user agent [#271](https://github.com/paypal/PayPal-Android-SDK/issues/271).

2.13.3
------
* Update okhttp dependency to 3.1.2.
* Really fixes issue related to okhttp 3.1.2 [#258](https://github.com/paypal/PayPal-Android-SDK/issues/258).

2.13.2
------
* Fixes issue related to okhttp 3.1.2 [#258](https://github.com/paypal/PayPal-Android-SDK/issues/258).

2.13.1
------
* Fix issue preventing the SDK from app-switching to newer versions of the PayPal App.

2.13.0
------
* Fix sandbox pinning issue [#228](https://github.com/paypal/PayPal-Android-SDK/issues/228).
* Allow complete removal of card.io dependency, effectively disabling direct credit card payments [#226](https://github.com/paypal/PayPal-Android-SDK/issues/226) & [#234](https://github.com/paypal/PayPal-Android-SDK/issues/234).
* Update okhttp dependency to 3.0.1.
* Update card.io to 5.3.0.

2.12.5
------
* Update okhttp dependency to 2.7.2.
* Clean up manifest permissions [#233](https://github.com/paypal/PayPal-Android-SDK/issues/233).
* Minor bug fixes.

2.12.4
------
* Documentation Updates for Maven based integrations.
* Disabled `allowBackup` in Manifest.

2.12.3
------
* SDK is now available at Maven Central [#14](https://github.com/paypal/PayPal-Android-SDK/issues/14) & [#208](https://github.com/paypal/PayPal-Android-SDK/issues/208).
	* `com.paypal.sdk:paypal-android-sdk:2.12.3`.

2.12.2
------
* Enable aar packaging.
* Ability to push to maven.

2.12.1
------
* Fix bug introduced in 2.12.0 that caused older devices to fail prematurely.

2.12.0
------
* Allow TLSv1.2 for API 16-19 devices, and add a special error message if PayPal requires TLSv1.2 but the device cannot comply.
* Add a LogCat warning during PayPalService initialization when an Android version < API 16 (JELLY_BEAN) is detected.

2.11.2
------
* Minor bug fixes.
* Update card.io to 5.1.2.

2.11.1
------
* Fix crash in `PayPalService.onDestroy()` [#212](https://github.com/paypal/PayPal-Android-SDK/issues/212).
* Fix case where shipping address selections were inadvertently displayed to the user [#213](https://github.com/paypal/PayPal-Android-SDK/issues/213).
* Decrease minSdkVersion back down to 10 (GINGERBREAD_MR1).
* Convert SampleApp to only use Gradle builds.

2.11.0
------
* Target Android 23 (M).
* Update card.io to 5.1.1 (supports new Android 23 Permissions). Note: this version of card.io contains support for more processor architectures.  Please be sure to grab the entire contents of this SDK, including all up-to-date card.io `*.so` files within the `/libs` folder to ensure that card.io will continue to work on these architectures.
* Increase minSdkVersion to 11 (HONEYCOMB).
* Update all networking to use HttpURLConnection instead of Apache's HTTP Client.
* Set all obfuscated classes to use lower case as a workaround for [an Android Tools issue](https://code.google.com/p/android/issues/detail?id=187210).

2.10.1
------
* Minor bug fixes.

2.10.0
------
* Fixed behavior where `retrieve_shipping_address ` is disabled and no `shipping address` is provided from the app, so it will not default to the PayPal account `shipping address`.

2.9.11
------
* Minor bug fixes.

2.9.10
------
* Minor bug fixes.

2.9.9
-----
* Fix rare crash when making a single payment [#179](https://github.com/paypal/PayPal-Android-SDK/issues/179).
* Minor bug fixes.

2.9.8
-----
* Fix rare crash in `PayPalService` [#166](https://github.com/paypal/PayPal-Android-SDK/issues/166).
* Minor bug fixes.

2.9.7
-----
* Fix rare instance of PAYMENT_CREATION_ERROR.
* Fix rare crashing issue on some devices when checking permissions [#167](https://github.com/paypal/PayPal-Android-SDK/issues/167).
* Upgrade build tools.

2.9.6
-----
* Fix issue where card.io compatibility was being checked even if credit cards are disabled [#173](https://github.com/paypal/PayPal-Android-SDK/issues/173).
* Update card.io to 5.0.1.

2.9.5
-----
* Update sample app build tools version to 1.2.2, compileSdkVersion to 22, and build tools version to 22.0.1.
* Minor bug fixes to the consent screen.

2.9.4
-----
* Fix rare NPE issue [#163](https://github.com/paypal/PayPal-Android-SDK/issues/163).

2.9.3
-----
* Remove dependency on the [Android Support Library](http://developer.android.com/tools/support-library/index.html).
* Fix NoClassDefFoundError issue with AdvertisingIdClient [#157](https://github.com/paypal/PayPal-Android-SDK/issues/157).
* Fix ClassCastException issue with GsmCellLocation [#160](https://github.com/paypal/PayPal-Android-SDK/issues/160).
* Minor bug fixes.

2.9.2
-----
* Minor bug fixes.

2.9.1
-----
* Fix crashing issue on Android `2.3.*` devices [#159](https://github.com/paypal/PayPal-Android-SDK/issues/159).
* Fix `PayPalService.clearAllUserData()` to clear all environments when `PayPalService` is not running [#155](https://github.com/paypal/PayPal-Android-SDK/issues/155).  If `PayPalService` is running, the v4 support library is still required to clear the current user data.
* Fix issue with `PayPalService.clearAllUserData()` not actually clearing the current user [#156](https://github.com/paypal/PayPal-Android-SDK/issues/156).
* Add some helpful logging to `PayPalService.clearAllUserData()`.
* Minor bug fixes.

2.9.0
-----
* Add `PayPalService.clearAllUserData(Context)` [#88](https://github.com/paypal/PayPal-Android-SDK/issues/88).  Note: this method requires the use of the [Android Support Library](http://developer.android.com/tools/support-library/index.html), due to its use of `LocalBroadcastManager`.  This SDK can still be used without the support library, but this method will not function.
* Fix validation of `PayPalItem`: `sku` is no longer required, currencies and amounts have more strict validation [#153](https://github.com/paypal/PayPal-Android-SDK/issues/153).
* Fix issue where too many profile sharing attributes were shown to the user.
* Change name of `ENVIRONMENT_NO_NETWORK` merchant.
* Minor bug fixes.

2.8.8
-----
* Change "Send Payment" button to "Pay". (see https://github.com/paypal/PayPal-iOS-SDK/issues/174).
* Minor bug fixes.
* Update card.io to 5.0.0.

2.8.7
-----
* Enforce required permissions: `ACCESS_NETWORK_STATE` and `INTERNET`.
* Gracefully handle devices that do not return network state.
* Better null handling.

2.8.6
-----
* Update card.io to 4.0.1.
* Add button allowing user to create a new PayPal account.
* Add more public logging.
* Minor UI tweaks.

2.8.5
-----
* Update card.io to 4.0.0.
* Update sample app build tools version to 1.0.1.
* Minor bug fixes.

2.8.4
-----
* Fix issue where resource is not properly closed [#125](https://github.com/paypal/PayPal-Android-SDK/issues/125).
* Add even better error messages in all locales.

2.8.3
-----
* Fix ClassNotFoundException issue in Parcelable classes.
* Add better error messages for some common errors.
* Minor bug fixes.
* Fix several docs and SampleApp issues.

2.8.2
-----
* Fix consent privacy policy and user agreement links, and update the mock link urls.
* Fix issue where the service would not properly restart after being backgrounded [#117](https://github.com/paypal/PayPal-Android-SDK/issues/117).
  It is now recommended that developers pass the `PayPalConfiguration` object into the PayPal activities, in addition to `PayPalService`.
  Update the documentation and sample app with the suggested modifications.
* Add documentation on correct `httpcomponents` dependencies, which addresses [#113](https://github.com/paypal/PayPal-Android-SDK/issues/113).
* Update Visa branding.
* Minor bug fixes.

2.8.1
-----
* Fix issue where consent was not working for new users (bug introduced in 2.8.0).
* Remove unneeded ACCESS_WIFI_STATE from integration docs.
* Update sample app build tools version to 1.0.0.

2.8.0
-----
* Allow login by users who have enabled two-factor authentication on their PayPal accounts.
* Update `Paypal-Application-Correlation-Id` header to `PayPal-Client-Metadata-Id`,
  and deprecate `PayPalConfiguration.getApplicationCorrelationId()` in favor
  of `PayPalConfiguration.getClientMetadataId()`.
* Update sample app build tools version.
* Minor bug fixes.

2.7.3
-----
* Fix single payments issue affecting cross-app integrations.

2.7.2
-----
* Add additional logging for exceptions on threads.
* Update sample app gradle wrapper to 2.2.1.
* Update sample app build tools version.

2.7.1
-----
* Fix validation issue related to android:process [#114](https://github.com/paypal/PayPal-Android-SDK/issues/114).
* Minor bug fixes.
* Update sample app build tools version.
* Remove some old files.

2.7.0
-----
* Add support for Russian Rubles.
* Add gradle files to Sample App.
* Minor fixes to the Consent Activity.

2.6.1
-----
* Minor improvements relevant only to select partners.
* Minor bug fixes.

2.6.0
-----
* Streamline Profile Sharing for cases where a user has previously authorized sharing.
* Update card.io library to 3.2.0.
* Update all targets to android-21.
* Minor bug fixes.

2.5.6
-----
* Minor bug fixes.

2.5.5
-----
* Fix issue with some PayPal Credit funding instruments [#97](https://github.com/paypal/PayPal-Android-SDK/issues/97).

2.5.4
-----
* Fix issue that kept some apps from authorizing properly.
* The SDK now rejects fractional amounts for HUF, JPY, TWD currencies (previously fractional amounts were rounded).


2.5.3
-----
* Update card.io library (adds Diners Club and China UnionPay support).
* Fix Cursor not closing problem reported in Strict mode [#87](https://github.com/paypal/PayPal-Android-SDK/issues/87).
* Minor bug fixes.

2.5.2
-----
* Update Sample App icons.
* Minor bug fixes.

2.5.1
-----
* Fix layout xml issue in SampleApp [#89](https://github.com/paypal/PayPal-Android-SDK/issues/89).

2.5.0
-----
* Add support for payment intent value `order` to create a payment for later authorization and capture via server calls.
* For single payments, an individual `PayPalItem` may be negative (for discounts, coupons, etc.).
* Add `invoiceNumber`, `custom`, and `softDescriptor` as optional properties on `PayPalPayment`.

2.4.0
-----
* Add [Profile Sharing](https://github.com/paypal/PayPal-Android-SDK/blob/master/docs/profile_sharing_mobile.md) feature.
	* Developer selects requested account profile attributes.
	* User may consent to sharing the requested profile data.
* Fix sluggish performance in Payment Method selection on devices with slower cameras (Nexus 10).
* Fix issue [#77: Invalid path on some devices](https://github.com/paypal/PayPal-Android-SDK/issues/77).

2.3.5
----
* Hotfix for issue on live/sandbox environment introduced in 2.3.4.

2.3.4
----
* Fix issue [#83: App freezes when calling startService](https://github.com/paypal/PayPal-Android-SDK/issues/83) for realsies this time.
* Restrict phone/pin login where appropriate.

2.3.3
----
* Fix issue [#83: App freezes when calling startService](https://github.com/paypal/PayPal-Android-SDK/issues/83).
* Minor bug fixes.

2.3.2
----
* Re-add Version class for Cordova backward compatibility.

2.3.1
----
* Support display of Pay After Delivery funding options.
* Minor bug fixes.

2.3.0
----
* Add user funding option selection.
* Add app-controlled user shipping address selection (including support for the app to add a new shipping address).
* Rename zh-Hant_HK -> zh-Hant so that HK is chosen by default for other regions.

2.2.2
-----
* Add translations for Thai.
* Fix bnCode validation to accept underscores and hyphens.

2.2.1
-----
* Fix SampleApp code related to multiple item support.
* Add instructions for using SDK in Gradle projects.
* Fix issues associated with how the PayPalService was being managed.

2.2.0
-----
* Add support for multiple items per payment.
* Update PayPal logo.
* Update card.io library to 3.1.5.

2.1.0
-----
* Add integration with PayPal Wallet App (available only on the Samsung app store).
	* In live environment, if the newly released PayPal Wallet app with authenticator is present on a user's device, the PayPal Wallet app will log the user in to the SDK.
* Fix issue where some email addresses would not be accepted.
* Fix some Spanish translations.
* Fix possible NPE in payment confirmation flow.

2.0.3
-----
* Add return of `authorization_id` to SDK's payment response when payment intent is authorization.  The `authorization_id` will be used to capture funds via server calls.
* Add `PayPalConfig.getLibraryVersion()`.
* Add support for Arabic and Malay languages.
* Add proper handling of right-to-left languages (Hebrew and Arabic).
* Improve user experience when user must log in again (informational dialog is displayed).

2.0.2
-----
* Minor bug fixes.

2.0.1
-----
* Fix values for product_name and build_time returned by SDK.

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
* Fix issue where PaymentActivity.EXTRA_DEFAULT_USER_EMAIL was not being handled properly in all cases [47](https://github.com/paypal/PayPal-Android-SDK/issues/47).

1.2.5
-----
* Refactor code to eliminate spurious error message in LogCat [40](https://github.com/paypal/PayPal-Android-SDK/issues/40).
* Fix validation of PayPalPayment.shortDescription where some credit card payments caused incorrect JSONException [41](https://github.com/paypal/PayPal-Android-SDK/issues/41).
* Eliminate source of potential NPE [37](https://github.com/paypal/PayPal-Android-SDK/issues/37).
* Update card.io lib to 3.1.4 (includes fixes for potential NPEs).

1.2.4
-----
* Fix NumberFormatException on some payments in locales that use comma for decimal separator [34](https://github.com/paypal/PayPal-Android-SDK/issues/34).

1.2.3
-----
* Fix issue where invalid currency amounts could be sent to the PayPal server.

1.2.2
-----
* Fix login page layout issue [20](https://github.com/paypal/PayPal-Android-SDK/issues/20).
* Add some login error logging.

1.2.1
-----
* Update card.io library to 3.1.3.
* Fix issue [#11: release/debug UI differences](https://github.com/paypal/PayPal-Android-SDK/issues/11).

1.2.0
-----
* Eliminate the final "Complete" screen.
* Fix Hebrew phone settings detection bug.
* Update card.io library to 3.1.2.

1.1.2
-----
* Re-add `Version` class.
* Update card.io library to 3.1.1.
* Support all currencies that are currently accepted by the REST APIs.  See [README](README.md) for details.
* Fix various localizations.
* Additional localization: ko (Korean).
* Minor UI cleanup (including [issue 4](https://github.com/paypal/PayPal-Android-SDK/issues/4)).

1.1.1
-----
* Skipped.

1.1.0
-----
* Bug fixes.
* Update card.io to 3.1.0.
* Add translations of all strings into ~20 languages, in addition to American English.
    - Translation choice is controlled by `EXTRA_LANGUAGE_OR_LOCALE` in PaymentActivity.
    - The translations that a few developers had previously created for their own apps will no longer be used by the SDK.
    - NOTE: Default language, if not set by your app, will now be based upon the device's current language setting.

1.0.3
-----
* Bug fixes.

1.0.2
-----
* Several small fixes & improvements.

1.0.1
-----
* Update card.io to 3.0.5.
* Minor UI improvements.
* Other small fixes.

1.0.0
-----
* First release!

