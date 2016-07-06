Supplemental Errors
===================

The PayPal REST API errors are documented [here](https://developer.paypal.com/docs/api/#errors). The errors listed on this payge may also be encountered when making payments:

- **PAYMENT_CREATION_ERROR**
	- There was a problem setting up the payment. This error is usually related the payment methods available in the users account and the merchant requirements.
- **PAYMENT_CREATION_ERROR_EXPIRED_PAYMENT_CARD**
	- There was a problem setting up the payment. The only card available in the user's account is expired.
- **PAYMENT_CREATION_ERROR_INSTANT_PAYMENT_REQUIRED**
	- There was a problem setting up the payment. The payment requires an instant payment but this is not available in the user's account.
- **PAYMENT_CREATION_ERROR_NEED_CONFIRMED_CARD**
	- There was a problem setting up the payment. The payment requires a confirmed card but one is not available in the user's account.
- **PAYMENT_CREATION_ERROR_NEED_PHONE_NUMBER**
	- There was a problem setting up the payment. The payment requires a phone number but one is not available in the user's account.
- **PAYMENT_CREATION_ERROR_NEED_VALID_FUNDING_INSTRUMENT**
	- There was a problem setting up the payment. The user's account does not contain a valid way to make this payment.
- **PAYMENT_CREATION_ERROR_NEGATIVE_BALANCE**
	- There was a problem setting up the payment. The user's account balance is negative and no other viable options are present.
- **PAYMENT_CREATION_ERROR_SENDING_LIMIT_REACHED**
	- There was a problem setting up the payment. The user's sending limit has been reached at this time.
