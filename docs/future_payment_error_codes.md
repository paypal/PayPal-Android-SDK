# Error Codes related to Future Payments

The following error codes supplement the [REST Payment API error codes](https://developer.paypal.com/webapps/developer/docs/api/#errors).

##### invalid_request

**Internal error**

This payment cannot be completed through PayPal.

_or_

**Invalid access token**

Access token is invalid or expired. The access token needs to be refreshed using the original request token.

_or_

**Invalid refresh token**

Refresh token is invalid or expired. Obtain consent from the user and get a new refresh token to replace your expired or invalid refresh token.

##### REQUIRED_SCOPE_MISSING

**Access token does not have required scope**

Obtain user consent using the correct scope required for this type of request.

##### INSUFFICIENT_FUNDS

**Buyer cannot pay - insufficient funds**

Buyer needs to add a valid funding instrument (e.g. credit card or bank account) to their PayPal account.

##### TRANSACTION_REFUSED_PAYEE_PREFERENCE

**Merchant profile preference is set to automatically deny certain transactions**

The merchant account preferences are set to deny this particular kind of transaction.

##### INVALID_FACILITATOR_CONFIGURATION

**This transaction cannot be processed due to an invalid facilitator configuration.**

You must have the right account configuration to process this kind of transaction.

