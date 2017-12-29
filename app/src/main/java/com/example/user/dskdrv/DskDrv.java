package com.example.user.dskdrv;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.net.URL;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DskDrv extends AppCompatActivity {


    Button bt;
    TextView tv;
    View view;
    LayoutInflater li;
    WebView  wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsk_drv);

        bt= (Button)findViewById(R.id.button3);
        tv=(TextView)findViewById(R.id.textView);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



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


        li= LayoutInflater.from(DskDrv.this);
        view=li.inflate(R.layout.alert_premission,null);
        final AlertDialog.Builder ad_b=new AlertDialog.Builder(DskDrv.this);
        ad_b.setView(view);

         wv=(WebView) findViewById(R.id.webview);




         final String[] s = new String[1];

/*                    wv.setWebViewClient(new WebViewClient(){
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            if (url.startsWith("dskdrv://")) {
                                s[0] =url.toString();
                                wv.destroy();
                                ad_b.setCancelable(true);

                    return true;
                }
                return false;
            }
        });



*/
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


        AlertDialog alert = ad_b.create();
        alert.show();

    }
}
