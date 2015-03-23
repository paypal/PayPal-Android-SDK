今後の支払い(Future Payments)のためのモバイルの統合
==================================

ここでは、PayPalアカウントを使用して今後の支払いを行うためにユーザーの同意を得る方法を説明します。

_まだ実行していない場合は、プロジェクトにSDKを追加するための基本的な概要と手順の[README](README.md)を参照してください。_


概要
--------

ご使用のアプリは、モバイルSDKを使って異なるタイミングで2つの情報を取得します。

最初に、PayPalアカウントからの支払いを受け取るため、[お客さまの同意を得る](#obtain-customer-consent)必要があります。これは以下のように実行されます。

* PayPal Android SDKは
    1. ユーザーがPayPalアカウントの使用を承認するためのUIを表示します。
    2. 今後の支払いでPayPalを使用するための[OAuthアクセストークンスコープ](http://tools.ietf.org/html/rfc6749#page-23)に対する同意をユーザーに求めます。
    3. アプリに、OAuth2の認可コードを返します。
* アプリは
    1. SDKからOAuth2の認可コードを受け取ります。
    2. サーバーに認可コードを送ります。サーバーは[コードをOAuth2のアクセストークンおよびリフレッシュトークンと交換](future_payments_server.md#obtain-oauth2-tokens)します。

後に、事前承認された支払いの開始時に、[アプリケーション相関IDを取得する](#obtain-an-application-correlation-id)必要があります。これは以下のように実行されます。

* PayPal Android SDKは
    * アプリケーション相関IDを提供します。
* アプリは
    * 相関IDと取引情報をサーバーに送ります。
    * サーバーは、[OAuth2のトークン、アプリケーション相関ID、および取引情報を使用して支払いを作成](future_payments_server.md)します。



お客さまの同意を得る
-----------------------

サンプルアプリには、より完全な例が用意されています。ただし、最低限以下を行う必要があります。

1. `AndroidManifest.xml`ファイルに許可を追加します。
    ```xml   
    <!-- for most things, including card.io & paypal -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    ```
    
1. `AndroidManifest.xml`ファイルの`<application>`タグでSDKのサービスとアクティビティを宣言します。
    ```xml
    <service android:name="com.paypal.android.sdk.payments.PayPalService"
            android:exported="false" />
        
    <activity android:name="com.paypal.android.sdk.payments.LoginActivity" />
    <activity android:name="com.paypal.android.sdk.payments.PayPalFuturePaymentActivity" />
    <activity android:name="com.paypal.android.sdk.payments.FuturePaymentConsentActivity" />
    <activity android:name="com.paypal.android.sdk.payments.FuturePaymentInfoActivity" />
    ```

1. `PayPalConfiguration`オブジェクトを作成します。このオブジェクトにより、SDKのさまざまな側面を設定できます。

	```java
	private static PayPalConfiguration config = new PayPalConfiguration()

			// モック環境で開始します。準備完了後、sandbox (ENVIRONMENT_SANDBOX)
			// または本番 (ENVIRONMENT_PRODUCTION)環境に切り替えます。
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("<YOUR_CLIENT_ID>")
            
            // 最低限3つのマーチャント情報プロパティを設定する必要があります。
    		 //  これらは、アプリを登録した際にPayPalに提供した値と同じである必要があります。
            .merchantName("Example Store")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
	```

2. アクティビティの作成時に`PayPalService`を開始し、破棄の際に停止します。

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

3. PayPalの今後の支払いアクティビティを、たとえばボタンが押された場合などに起動します。

    ```java
    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(SampleActivity.this, PayPalFuturePaymentActivity.class);
        
        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }
    ```

4. `onActivityResult()`を実装します。

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
                    Log.e("FuturePaymentExample", "非常にまれなエラーが発生しました: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("FuturePaymentExample", "ユーザーがキャンセルしました。");
        } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("FuturePaymentExample",
                    "PayPalServiceを前に開始するための処理で、無効なPayPalConfigurationが含まれていた可能性があります。ドキュメントを参照してください。");
        }
    }
    ```

5. プロセスを完了するため、サーバーに認可応答を送信します。

    ```java
    private void sendAuthorizationToServer(PayPalAuthorization authorization) {
        
        // TODO:
        // 認可応答をサーバーに送信します。サーバーは認可コードを
        // OAuthのアクセストークンおよびリフレッシュトークンと交換できます。
        //
        // サーバーはこれらのトークンを保管するので、サーバーコードは、今後このユーザーの
        // 支払いを実行できます。
        
    }
    ```

アプリケーション相関IDの取得
-----------------------

モバイル端末から事前承認された支払い(「今後の支払い」)を開始する場合、モバイルアプリは、アプリケーション相関IDをモバイルSDKから取得し、サーバーに渡す必要があります。サーバーは、PayPalに送信する支払いリクエストにこのアプリケーション相関IDを含める必要があります。

PayPalは、このアプリケーション相関IDを使用して、ユーザーが同意した有効な端末およびアプリから支払いが行われていることを認証します。これは、不正取引や拒否を減らすことに役立ちます。**PayPalは、適切にアプリケーション相関IDを提供しない取引での損失についてはいっさい保護しません。**

**この値をキャッシュまたは格納しないでください。**

例

```java
public void onFuturePaymentPurchasePressed(View pressed) {
// SDKからアプリケーション相関IDを取得します。
String metadataId = PayPalConfiguration.getClientMetadataId(this);
        
// TODO: PayPalでの処理のため、相関IDと取引の詳細をサーバーに
// 送信します。
}
```


サーバーは、PayPalへの支払いリクエストを作成する際、HTTPヘッダー`PayPal-Client-Metadata-Id`に、SDKから取得したこのアプリケーション相関IDの値を含める必要があります。


次の手順
----------

[今後の支払いのためのサーバー側の統合](future_payments_server.md)を読み、認可コードとOAuth2のトークンを交換して、アクセストークンと相関IDにより支払いを作成します。

