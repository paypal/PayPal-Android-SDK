PayPal Android SDK
==================

PayPal Android SDKを使用すると、モバイルアプリにPayPalおよびクレジットカード支払いの機能を簡単に追加できます。

## 目次

- [ユースケース](#use-cases)
- [PayPalウォレットアプリとの統合](#integration-with-the-paypal-wallet-app)
- [要件](#requirements)
- [プロジェクトにSDKを追加する](#add-the-sdk-to-your-project)
- [認証情報](#credentials)
- [海外サポート](#international-support)
- [card.ioカードスキャンの無効化](#disabling-cardio-card-scanning)
- [テスト](#testing)
- [ドキュメント](#documentation)
- [ユーザビリティ](#usability)
- [PayPal Android SDK 2.0への移行](#moving-to-paypal-android-sdk-20)
- [次のステップ](#next-steps)


### 1件の支払い(Single Payment)

お客さまのPayPalアカウントまたは([card.io](https://www.card.io/)でスキャンされた)決済カードから即時支払いを受け取ります。

1. [1件の支払いを受諾](single_payment.md)して支払い証明を受け取ります。2. ご使用のサーバーで、PayPalのAPIを使用して[支払いを認証](https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/) (PayPalデベロッパーサイト)します。


### 今後の支払い(Future Payments)

お客さまは、一度だけPayPalにログインして今後の支払いに同意します。
1. [お客さまの同意を得て](future_payments_mobile.md#obtain-customer-consent)承認コードを受け取ります。2. ご使用のサーバーで、この承認コードを使用して[OAuth2トークンを取得](future_payments_server.md#obtain-oauth2-tokens)します。

この後、お客さまが支払いを開始すると以下のプロセスに進みます。 

1. サーバーに渡す[アプリケーション相関IDを取得](future_payments_mobile.md#obtain-an-application-correlation-id)します。
2. お使いのサーバーで、OAuth2トークン、アプリケーション相関ID、およびPayPalのAPIを使用して[支払いを作成](future_payments_server.md#create-a-payment)します。

###個人設定の共有

お客さまは、PayPalにログインして、PayPalが貴社と情報を共有することに同意します。

1. [お客さまの同意を得て](profile_sharing_mobile.md#obtain-customer-consent)承認コードを取得します。2. ご使用のサーバーで、この承認コードを使用して[OAuth2トークンを取得](profile_sharing_server.md#obtain-oauth2-tokens)します。3. ご使用のサーバーで、OAuth2トークンとPayPalのAPIを使用して、[顧客情報を検索](profile_sharing_server.md#retrieve-customer-information)します。


## PayPalウォレットアプリとの統合

SDKは、デバイスにPayPalウォレットアプリの最新バージョン([Samsung App Store](apps.samsung.com/mars/topApps/topAppsDetail.as?categoryId=G000019679&productId=000000794995)でのみ入手可能)がインストールされている場合はそれを使用して、お客さまのアカウントにログインするようになります。この機能を有効にするため、追加構成を行う必要はありません。この統合により、Galaxy S5の指紋認証ログインを含む端末固有のPayPal [FIDO](https://fidoalliance.org/)との統合が有効になります。さらに、PayPalウォレットアプリにログインして[ログインしたままにする]にチェックを入れたユーザーは、アプリでの支払い時に再度ログインする必要がありません。この機能についての詳細は、PayPalアーキテクトの[ブログポスト](http://www.embedded.com/design/real-world-applications/4430305/Implementing-Android-based-fingerprint-authentication-for-online-payments)を参照してください。

### 制限

* サポートされるアプリは、[Samsung App Store](apps.samsung.com/mars/topApps/topAppsDetail.as?categoryId=G000019679&productId=000000794995)でのみ入手できます。Google PlayストアのバージョンのPayPalウォレットアプリは、まだこの統合をサポートしていません。* ウォレットアプリはデベロッパーのテスト環境に対応して_いない_ため、統合は[テスト](#testing)モードでは無効です。

## 要件

* Android 2.2またはそれ以降
* card.ioカードスキャニング(armv7デバイスでのみ使用可能)
* 電話またはタブレット


## プロジェクトにSDKを追加する

1. このrepoをダウンロードまたはコピーします。SDKには、.jar、スタティックライブラリ、リリースノート、ライセンス許諾書が含まれています。サンプルアプリも含まれています。2. SDKの`libs`ディレクトリのコンテンツを自分のプロジェクトの`libs`ディレクトリにコピーします。これらのファイルへのパスは重要です。誤りがあるとSDKが機能しません。(_注:_ Gradleを使用している場合は、SDK jarファイルを自分のプロジェクトの`libs`ディレクトリにコピーして、プロジェクトに`ライブラリとして追加`し、最後に*.soファイルを含むSDKフォルダを`src/main/jniLibs`にコピーしてください)。
3. `acknowledgments.md`から、オープンソースライセンス許諾書を自分のアプリの許諾書に追加します。


## 認証情報

モバイル統合では、本番用とテスト用(Sandbox)にそれぞれ異なる`client_id`値が必要です。

支払いを認証または作成するためのサーバー統合では、各`client_id`に対応する`client_secret`も必要です。

PayPal API認証情報は、[PayPalデベロッパーサイトのアプリケーションページ](https://developer.paypal.com/webapps/developer/applications)を開いて、自分のPayPalアカウントでログインすると入手できます。

### Sandbox

このアプリケーションページにログインすると、**テスト用認証情報**が割り当てられます。これには、PayPal SandboxへのAndroidの統合をテストするためのクライアントIDが含まれています。

アプリのテスト中にSDKのUIでPayPalにログインする場合は、*パーソナル*Sandboxアカウントのメールアドレスとパスワードを使用する必要があります。Sandbox用*ビジネス*アカウントの認証情報は使いません。

[Sandboxアカウント](https://developer.paypal.com/webapps/developer/applications/accounts)ページで、Sandbox用のビジネスアカウントとパーソナルアカウントを作成できます。

### 本番環境

**本番用**認証情報を入手するには、ビジネスアカウントが必要です。ビジネスアカウントをまだお持ちでない場合は、同じアプリケーションページの下部にリンクがあり、ここからアカウントを開設できます。


## 海外サポート

### ローカライズ

SDKには、多数の言語およびロケールの翻訳が組み込まれています。完全なリストは、[javadoc](http://paypal.github.io/PayPal-Android-SDK/)ファイルで参照できます。

### 通貨

SDKでは複数の通貨に対応しています。完全な最新リストは、[REST APIの国と通貨に関するドキュメント](https://developer.paypal.com/webapps/developer/docs/integration/direct/rest_api_payment_country_currency_support/)で参照してください。

クレジットカードとPayPal支払いでは対応通貨が異なりますのでご注意ください。`PaymentActivity.EXTRA_SKIP_CREDIT_CARD` intent extraを使用してクレジットカード決済の受付けを無効にしない限り、**取引を、両方の支払いタイプで対応している通貨に限定することをおすすめします。** 現在両方で対応している通貨は、USD、GBP、CAD、EUR、JPYです。

ユーザーが選択した支払いタイプで対応していない通貨を使用してアプリが取引を開始した場合、SDKはユーザーにエラーを表示し、コンソールログにメッセージを出力します。


## card.ioによるカードスキャンの無効化

今後の支払いではcard.ioによるカードスキャンが不要なため、カメラスキャナライブラリを削除しておいたほうが安全です。`lib`ディレクトリの`armeabi`、`armeabi-v7a`、`mips`、および`x86`の各フォルダを削除します。

1件の支払いは、カードスキャンをせず、手動入力でクレジットカードに対応するよう設定できます。これを行うには、上記と同じライブラリを削除し、`AndroidManifest.xml`から`android.permission.CAMERA`と`android.permission.VIBRATE`の許可を削除してください。クレジットカードのサポートを完全に無効にする場合は、上記の手順で許可とSDKのフットプリントを減らし、`PayPalConfiguration`の初期化に以下を追加してください。
```
config.acceptCreditCards(false);
```

## テスト

開発中は、`PayPalConfiguration`オブジェクトで`environment()`を使用して環境を変更してください。実際に資金が移動されないよう、`ENVIRONMENT_NO_NETWORK`または`ENVIRONMENT_SANDBOX`に設定します。


## ドキュメント

* 使用方法の概要、統合手順をステップごとに記載した説明書、サンプルコードを含むドキュメントがSDKに用意されています。* このSDKにはサンプルアプリが含まれています。* [javadocs](http://paypal.github.io/PayPal-Android-SDK/)が用意されています。* エラーコードとサーバー側の統合手順が記載された[PayPalデベロッパードキュメント](https://developer.paypal.com/docs)。


## ユーザビリティ

ユーザーインターフェイスの外観と動作は、ライブラリ内で設定されます。ユーザビリティとユーザーエクスペリエンスの一貫性を保つため、ドキュメントに記載された方法以外でアプリがSDKの動作を変更することはおすすめしません。


## PayPal Android SDK 2.0への移行


### 1.xからのアップグレード

メジャーバージョンの変更として、2.0で導入されるAPIは、1.x統合との下位互換性がありません。ただし、SDKは、1件の支払いについて、これまでのすべての機能を引き続きサポートしています。アップグレードは簡単です。

* `PayPalPaymentActivity`の支払い固有のextra以外のextraの多くは`PayPalConfiguration`クラスに移動しています。サービススタートアップはそのような構成オブジェクトを取得するよう変更されています。



### 旧ライブラリ

PayPalは、これまでの「Mobile Payments Libraries」(MPL)から新しいPayPal AndroidおよびiOS SDKに移行中です。
新しいモバイルSDKはPayPal REST APIに基づいています。これまでのMPLはアダプティブペイメントAPIを使用しています。

第三者、並行型、チェーン型の支払いなどの機能が使えるようになるまでは、必要に応じてMPLを使用できます。

 - [GitHubのMPL](https://github.com/paypal/sdk-packages/tree/gh-pages/MPL)
 - [MPLに関するドキュメント](https://developer.paypal.com/webapps/developer/docs/classic/mobile/gs_MPL/)

MPLに関する問題は、[sdk-packages repo](https://github.com/paypal/sdk-packages/)に提出してください。

既存のエクスプレス チェックアウトを統合しているデベロッパーまたは追加機能が必要なデベロッパーは、WebViewで[モバイルエクスプレス チェックアウト](https://developer.paypal.com/webapps/developer/docs/classic/mobile/gs_MEC/)の使用を検討することができます。


## 次のステップ

ユースケースに応じて、以下が可能です:

* [1件の支払いを受ける](single_payment.md)
* [今後の支払いを作成](future_payments_server.md)ために[ユーザーの同意を得る](future_payments_mobile.md)
