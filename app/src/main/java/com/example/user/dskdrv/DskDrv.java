package com.example.user.dskdrv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DskDrv extends AppCompatActivity {

// creat dev branches and push
    Button bt;
    TextView tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsk_drv);

        bt= (Button)findViewById(R.id.button3);
        tv=(TextView)findViewById(R.id.textView);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_premission();
                Log.i("onClk","+++");
            }
        });

    }

    String get_() throws IOException {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        OkHttpClient client=new OkHttpClient();
        Response resp=null;

        try {
            Request request=new Request.Builder().url("https://oauth.yandex.ru/authorize?"+
                    "response_type=code" +
                    "&client_id=fc3985e6de824b35a95e56b00dd21685" +
                    "&display=popup"+
                    "&login_hint=DskDrv" +
                    "&force_confirm=yes" +
                    "&state=true").build();
             resp=client.newCall(request).execute();

            tv.setText(String.valueOf(resp.code()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp.body().string();

    }

    void alert_premission(){

        LayoutInflater li= LayoutInflater.from(DskDrv.this);
        View view=li.inflate(R.layout.alert_premission,null);
        final AlertDialog.Builder ad_b=new AlertDialog.Builder(DskDrv.this);
        ad_b.setView(view);
        final AlertDialog alert = ad_b.create();

        WebView  wv=(WebView) view.findViewById(R.id.webview);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if( url.startsWith("http:") || url.startsWith("https:") ) {
                    return false;
                }

                // Otherwise allow the OS to handle things like tel, mailto, etc.
                String [] s= url.split("=");
                tv.setText(s[2]);
                post_for_token(s[2]);
                alert.cancel();
                return true;
            }
        });
                //setWebViewClient(new WebViewClient());

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setSaveFormData(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.loadUrl("https://oauth.yandex.ru/authorize?"+
                "response_type=code" +
                "&client_id=fc3985e6de824b35a95e56b00dd21685" +
                "&display=popup"+
                "&login_hint=DskDrv" +
                "&force_confirm=yes" +
                "&state=true");

        alert.show();

    }

    public void post_for_token(String code){

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String sjson = "";
        OkHttpClient client =new OkHttpClient();
        RequestBody reqestBody=new FormBody.Builder()
                .add("grant_type","authorization_code")
                .add("code",code)
                .add("client_id","fc3985e6de824b35a95e56b00dd21685")
                .add("client_secret","cf45926b4bfb4a21af0969c34c1ca1ed").build();
        Request reqest =new Request.Builder()
                .url("https://oauth.yandex.ru/token?")
                .post(reqestBody)
                .build();

        try {
            Response resp=client.newCall(reqest).execute();
            sjson=resp.body().string();
           // Log.i("DskDrv_post_for_token",sjson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject json=new JSONObject(sjson);
            String access_t=json.getString("access_token");
            String refrash_t=json.getString("refresh_token");
            tv.setText(access_t);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
