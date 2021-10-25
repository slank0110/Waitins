package logicreat.waitins;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        MyApp.setCurActivity(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(MyApp.width/2,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar, null), params);
        actionBar.setDisplayHomeAsUpEnabled(true);

        WebView webView = (WebView) findViewById(R.id.payment_webview);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                //super.onReceivedSslError(view, handler, error);

                //super.onReceivedSslError(view, handler, error);
                String message = "SSL Certificate error";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                }

                message += " Do you want to continue anyway?";

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MyApp.getCurActivity());
                mBuilder.setMessage(message)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.proceed();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });

        final String promotionId = getIntent().getStringExtra("promotionId");

        String postData = "promotionId=" + promotionId;
        final String url = "https://logicreat.ca/waitinsApp/transaction/purchasePageLead.php";

        if (!MyApp.getOurInstance().getSessionCookie().equals("")) {
            CookieManager.getInstance().setCookie(url, MyApp.getOurInstance().getSessionCookie());
        }else{
        }
        webView.postUrl(url, Charset.forName("UTF-8").encode(postData).array());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
