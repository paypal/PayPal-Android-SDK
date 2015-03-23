個人設定共有のためのモバイルの統合
==================================

ここでは、PayPalアカウントの個人設定を共有するためにユーザーの同意を得る方法を説明します。

_まだ実行していない場合は、プロジェクトにSDKを追加するための基本的な概要と手順の[README](README.md)を参照してください。_


概要
--------

PayPalアカウントの情報を共有するため、[お客さまの同意を得る](#obtain-customer-consent)必要があります。これは以下のように実行されます。

* PayPalデベロッパーサイトで
    1. お客さまに共有を求める情報を指定します。
* PayPal Android SDKは
    1. ユーザーがPayPalアカウントの使用を承認するためのUIを表示します。
    2. PayPalを使用して個人設定を共有するための[OAuthアクセストークンスコープ](http://tools.ietf.org/html/rfc6749#page-23)に対する同意をユーザーに求めます。
    3. アプリに、OAuth2の認可コードを返します。
* アプリは
    1. SDKのリクエストで必要なスコープを指定します。
    2. SDKからOAuth2の認可コードを受け取ります。
    3. サーバーに認可コードを送ります。サーバーは[コードをOAuth2のアクセストークンおよびリフレッシュトークンと交換](profile_sharing_server.md#obtain-oauth2-tokens)します。
    4. サーバーは、[OAuth2のトークンを使用してPayPalから該当する顧客情報を要求](profile_sharing_server.md)します。

**注:**  
1. PayPal Android SDKで使用できるスコープ値の完全なリストは`PayPalOAuthScopes`を参照してください。  
2. 使用できるスコープ属性の完全なリストは、[PayPalユーザー属性によるログイン](https://developer.paypal.com/docs/integration/direct/identity/attributes/)を参照してください。


共有する情報の指定
---------------------------------------

1. [PayPalデベロッパーサイト](https://developer.paypal.com)にログインします。
2. [アプリ](https://developer.paypal.com/webapps/developer/applications/myapps)を選択します。
3. `APP CAPABILITIES`の下で、`Log In with PayPal`を選択して`Advanced options`をクリックします。
4. `Information requested from customers`の下で、共有する必要がある項目(スコープ属性)を選択します。
5. `Links shown on customer consent page`の下にプライバシーポリシーおよびユーザー規約のURLを入力すると、[`PayPalConfiguration`オブジェクト](#obtain-customer-consent)に入力する対応URLが無効になります。


お客さまの同意を得る
-----------------------

サンプルアプリには、より完全な例が用意されています。ただし、最低限以下を行う必要があります。

1. `AndroidManifest.xml`ファイルに許可を追加します。
    ```xml   
    <!-- for most things, including card.io & paypal -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    ```
    
1. `AndroidManifest.xml`ファイルの`<application>`タグでSDKのサービスとアクティビティを宣言します。
    ```xml
    <service android:name="com.paypal.android.sdk.payments.PayPalService"
            android:exported="false" />
        
    <activity android:name="com.paypal.android.sdk.payments.LoginActivity" />
    <activity android:name="com.paypal.android.sdk.payments.PayPalProfileSharingActivity" />
    <activity android:name="com.paypal.android.sdk.payments.ProfileSharingConsentActivity" />
    ```

1. `PayPalConfiguration`オブジェクトを作成します。このオブジェクトにより、SDKのさまざまな側面を設定できます。

	```java
	private static PayPalConfiguration config = new PayPalConfiguration()

			// モック環境で開始します。準備完了後、sandbox (ENVIRONMENT_SANDBOX)
			// または本番 (ENVIRONMENT_PRODUCTION)環境に切り替えます。
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("<YOUR_CLIENT_ID>")
            
            // 最低限3つのマーチャント情報プロパティを設定する必要があります。
    		// これらは、アプリを登録した際にPayPalに提供した値と同じである必要があります。
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

3. PayPalの個人設定の共有アクティビティを、たとえばボタンが押された場合などに起動します。

    ```java
    public void onProfileSharingPressed(View pressed) {
        Intent intent = new Intent(SampleActivity.this, PayPalProfileSharingActivity.class);
        intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());
        startActivityForResult(intent, REQUEST_CODE_PROFILE_SHARING);
    }
    ```
    
    一連のスコープ名で`PayPalOAuthScopes`が初期化されます。
    
    ```java
    private PayPalOAuthScopes getOauthScopes() {
        /* 必要なスコープセットの作成
         * 注: このアプリで選択する属性とここで必要なスコープ間のマッピングについては、
         * PayPalデベロッパーサイトのhttps://developer.paypal.com/docs/integration/direct/identity/attributes/を参照してください。
         */
        Set<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
        return new PayPalOAuthScopes(scopes);
    }
    ```

4. `onActivityResult()`を実装します。

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
                    Log.e("ProfileSharingExample", "非常にまれなエラーが発生しました: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("ProfileSharingExample", "ユーザーがキャンセルしました。");
        } else if (resultCode == PayPalProfileSharingActivity.RESULT_EXTRAS_INVALID) {
            Log.i("ProfileSharingExample",
                    "PayPalServiceを前に開始するための処理で無効なPayPalConfigurationが含まれていた可能性があります。ドキュメントを参照してください。");
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
        // サーバーはこれらのトークンを保管するので、サーバーコードは、これを使用して、
        // 今後ユーザーの個人設定データを取得できます。
        
    }
    ```


次の手順
----------
[個人設定共有のためのサーバー側の統合](profile_sharing_server.md)を読んで、認可コードとOAuth2のトークンを交換して、PayPalから顧客情報を取り出します。
