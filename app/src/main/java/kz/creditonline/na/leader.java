package kz.creditonline.na;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;


public class leader extends AppCompatActivity {
    private WebView webView;
    private LinearLayout scroll_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_leader);

        MyButton[] bts = this.getAssetJsonData(getApplicationContext());

        webView = findViewById(R.id.web_view);
        scroll_menu = findViewById(R.id.scroll_menu);

        this.generateButtonList(bts);

        WebSettings webSettings = webView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setSupportZoom(false);

        webSettings.getJavaScriptEnabled();
        webSettings.getJavaScriptCanOpenWindowsAutomatically();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl("https://vitrina.herokuapp.com/policy");
        webView.setWebViewClient(new WebViewClient());


    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public static MyButton[] getAssetJsonData(Context context) {
        String json = "[]";
        try {
            InputStream is = context.getAssets().open("offers.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            new Gson().fromJson(json, new TypeToken<MyButton[]>(){}.getType());
        }

        return new Gson().fromJson(json, new TypeToken<MyButton[]>(){}.getType());
    }

    private void generateButtonList(MyButton[] bts) {
//        ArrayList<Button> buttons;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 3, 10, 3);

        for(int i = 0; i < bts.length; i++){
            if (bts[i].isDisable() == true) {
                continue;
            }
            Button b = new Button(getApplicationContext());
            b.setText(bts[i].getText());
            b.setLayoutParams(params);
            b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(bts[i].getBackground())));
            b.setTextColor(Color.parseColor(bts[i].getColor()));
            int finalI = i;
            int finalI1 = i;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webView.loadUrl(bts[finalI].getUrl());
                    scroll_menu.setBackgroundColor(Color.parseColor(bts[finalI1].getBackground()));
                }
            });

            scroll_menu.addView(b);
        }
    }
}