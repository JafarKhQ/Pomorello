package me.memleak.pomorello.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TrelloApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import butterknife.Bind;
import me.memleak.pomorello.R;

/**
 * Created by jafar_qaddoumi on 9/7/15.
 * <p/>
 * Copyright (c) 2015 EPAM Systems. All rights reserved.
 */
public class TrelloOuthActivity extends BaseActivity {
    private static final String CALLBACK = "callback://trello";
    public static final String EXTRA_API_KEY = "extra_api_key";
    public static final String EXTRA_API_SECRET = "extra_api_secret";
    public static final String EXTRA_ACCESS_TOKEN = "extra_access_token";
    public static final String EXTRA_ACCESS_SECRET = "extra_access_secret";
    public static final String EXTRA_ERROR_MESSAGE = "extra_error_message";

    public static final int REQUEST_CODE_TRELLO_AUTH = 2776;

    public static void startActivityForResult(Activity activity, String apiKey, String apiSecret) {
        Intent intent = new Intent(activity, TrelloOuthActivity.class);
        intent.putExtra(EXTRA_API_KEY, apiKey);
        intent.putExtra(EXTRA_API_SECRET, apiSecret);
        activity.startActivityForResult(intent, REQUEST_CODE_TRELLO_AUTH);
    }

    private Token requestToken;
    private OAuthService oAuthService;

    @Bind(R.id.trello_outh_wbv_page)
    WebView wbvPage;
    @Bind(R.id.trello_outh_prg_loading)
    ProgressBar prgLoading;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_trello_outh;
    }

    @Override
    protected void setupViews() {
        setResult(RESULT_CANCELED);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        WebSettings webSettings = wbvPage.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);

        wbvPage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }

                return false;
            }
        });
        wbvPage.setWebViewClient(webViewClient);

        final String apiKey = getIntent().getStringExtra(EXTRA_API_KEY);
        final String apiSecret = getIntent().getStringExtra(EXTRA_API_SECRET);

        oAuthService = new ServiceBuilder()
                .provider(TrelloApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback(CALLBACK)
                .scope("read")
                .scope("write")
                .build();

        new GetRequestToken().execute();
    }

    private void showLoading() {
        prgLoading.setVisibility(View.VISIBLE);
        wbvPage.setVisibility(View.GONE);
    }

    private void hideLoading() {
        prgLoading.setVisibility(View.GONE);
        wbvPage.setVisibility(View.VISIBLE);
    }

    private void outhDone(String accessToken, String accessSecret) {
        if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(accessSecret)) {
            setResult(RESULT_CANCELED);
            mActivity.finish();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_ACCESS_TOKEN, accessToken);
        data.putExtra(EXTRA_ACCESS_SECRET, accessSecret);
        setResult(RESULT_OK, data);

        mActivity.finish();
    }

    private class GetRequestToken extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showLoading();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                requestToken = oAuthService.getRequestToken();

                //noinspection StringBufferReplaceableByString
                StringBuilder stringBuilder = new StringBuilder(oAuthService.getAuthorizationUrl(requestToken));
                // hack to add app name
                stringBuilder.append("&name=");
                stringBuilder.append(getString(R.string.app_name));
                stringBuilder.append("&expiration=never");
                return stringBuilder.toString();
            } catch (Exception ex) {
                Log.e(TAG, "GetRequestToken - doInBackground: " + ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (null == result) {
                Intent data = new Intent();
                data.putExtra(EXTRA_ERROR_MESSAGE, getString(R.string.error_get_auth_url));
                setResult(RESULT_CANCELED, data);

                mActivity.finish();
            } else {
                hideLoading();
                wbvPage.loadUrl(result);
            }
        }
    }

    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (null != url && url.startsWith(CALLBACK)) {
                Uri uri = Uri.parse(url);
                String verifier = uri.getQueryParameter("oauth_verifier");
                if (null == verifier) {
                    String problem = uri.getQueryParameter("oauth_problem");
                    if (TextUtils.isEmpty(problem)) {
                        problem = getString(R.string.error_empty_oauth_problem);
                    }

                    Intent data = new Intent();
                    data.putExtra(EXTRA_ERROR_MESSAGE, problem);
                    setResult(RESULT_CANCELED, data);

                    mActivity.finish();
                    return true;
                }

                final Verifier v = new Verifier(verifier);
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // save this token for practical use.
                        Token token = oAuthService.getAccessToken(requestToken, v);
                        outhDone(token.getToken(), token.getSecret());
                    }
                });
                thread.start();

                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    };
}
