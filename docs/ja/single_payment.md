1件の支払い
==============

お客さまから、PayPalまたは[card.io](https://www.card.io/)を使用した決済カードによる1件の即時支払いを受け取ります。

_まだ実行していない場合は、プロジェクトにSDKを追加するための基本的な概要と手順の[README](README.md)を参照してください。_

概要
--------

* PayPal Android SDKは
    1. ユーザーから支払い情報を収集するためのUIを表示します。
    2. PayPalと支払いを調整します。
    3. アプリに支払い証明を返します。
* コードは
    1. PayPal Android SDKから支払い証明を受信します。
    2.[認証のため、サーバーに支払い証明を送信](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/)します。
    3. ユーザーに商品またはサービスを提供します。

_オプションとして、アプリで、PayPal Android SDKがユーザーに**配送先住所**を選ばせるように指定することもできます。_

* コードは
    1. PayPal Android SDKに、アプリで提供された配送先住所および/またはユーザーのPayPalアカウントに登録されている配送先住所を表示するよう命令します。
* PayPal Android SDKは
        1. ユーザーが、表示された配送先住所を確認して選択できるようにします。
    2. 選択された配送先住所を、PayPalのサーバーに送信される支払い情報に追加します。
* サーバーは
    1. 支払いを[認証](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/)または[回収](https://developer.paypal.com/webapps/developer/docs/integration/direct/capture-payment/#capture-the-payment)する際に、支払い情報から配送先住所を取得します。


サンプルコード
-----------

サンプルアプリには、より完全な例が用意されています。ただし、最低限以下を行う必要があります。
1. `AndroidManifest.xml`ファイルに許可を追加します。
    ```xml   
    <!-- for card.io card scanning -->
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.VIBRATE" />
    
    <uses-feature android:name="android.hardware.camera" android:required="false" />
    <uses-feature android:name="android.hardware.camera.autofocus" android:required="false" />

    <!-- for most things, including card.io & paypal -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    ```
    
1. `AndroidManifest.xml`ファイルの`<application>`タグでSDKのサービスとアクティビティを宣言します。
    ```xml
    <service android:name="com.paypal.android.sdk.payments.PayPalService"
            android:exported="false" />
        
    <activity android:name="com.paypal.android.sdk.payments.PaymentActivity" />
    <activity android:name="com.paypal.android.sdk.payments.LoginActivity" />
    <activity android:name="com.paypal.android.sdk.payments.PaymentMethodActivity" />
    <activity android:name="com.paypal.android.sdk.payments.PaymentConfirmActivity" />
    <activity android:name="io.card.payment.CardIOActivity"
              android:configChanges="keyboardHidden|orientation" />
    <activity android:name="io.card.payment.DataEntryActivity" />
    ```

1. `PayPalConfiguration`オブジェクトを作成します。
    ```java
    private static PayPalConfiguration config = new PayPalConfiguration()

            // モック環境で開始します。準備完了後、sandbox (ENVIRONMENT_SANDBOX)
            // または本番(ENVIRONMENT_PRODUCTION)環境に切り替えます。
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("<YOUR_CLIENT_ID>");
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

3.  支払いを作成して、支払いインテントを、たとえばボタンが押された場合などに起動します。

    ```java
    public void onBuyPressed(View pressed) {

        // PAYMENT_INTENT_SALEにより、ただちに支払いが完了します。
        // PAYMENT_INTENT_SALEを以下に変更します。
        //   - 支払い承認のみを行い後で資金回収を行うPAYMENT_INTENT_AUTHORIZE
        //   - 承認用に支払いを作成し後でサーバーからのコールにより回収する
        //     PAYMENT_INTENT_ORDER

        PayPalPayment payment = new PayPalPayment(new BigDecimal("1.75"), "USD", "sample item",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }
    ```
    _注: 支払いのために配送先住所を入力するには、サンプルアプリの**addAppProvidedShippingAddress(...)**を参照してください。ユーザーのPayPalアカウントから配送先住所を取得できるようにするには、サンプルアプリの**enableShippingAddressRetrieval(...)**を参照してください。_

4. `onActivityResult()`を実装します。

    ```java
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: 認証のため、サーバーに'confirm'を送信します。
                    // 詳細はhttps://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // を参照してください。

                } catch (JSONException e) {
                    Log.e("paymentExample", "非常にまれなエラーが発生しました: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "ユーザーがキャンセルしました。");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "無効な支払いまたはPayPalConfigurationが送信されました。ドキュメントを参照してください。");
        }
    }
    ```

5. 注文から発送までの処理(フルフィルメント)などの業務に必要な処理に加え、[認証のため、支払い証明をサーバーに送信](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/)します。
   

   **ヒント:** この時点で支払いは完了し、ユーザーには請求済みです。サーバーにアクセスできない場合は、支払い証明を保存し、しばらくしてから再実行してください。

### ヒント

モバイルネットワークの接続は安定していません。最終的に確実にサーバーに接続するため、支払い証明を保存しておいてください。

次の手順
----------

**不正の防止** 必ず[支払い証明を認証](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/)してください。
