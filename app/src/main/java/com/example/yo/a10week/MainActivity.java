package com.example.yo.a10week;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText et;
    Button btn1;
    WebView webView;
    ProgressDialog dialog;
    Animation top;
    LinearLayout linear;
    ArrayList<Data> arraylist = new ArrayList<Data>();
    CustomAdapter adapter;
    ListView list_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linear = (LinearLayout)findViewById(R.id.linear);
        et = (EditText)findViewById(R.id.et);
        btn1 = (Button)findViewById(R.id.btn1);
        webView = (WebView)findViewById(R.id.webview);
        webView.addJavascriptInterface(new JavaScriptMethods(), "MyApp");
        dialog = new ProgressDialog(this);
        setListView();

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);


        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog.setMessage("Loading...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                et.setText(url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100) dialog.dismiss();
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }
        });
        webView.loadUrl("http://www.naver.com");

        top = AnimationUtils.loadAnimation(this, R.anim.translate_top);
        top.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                linear.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0, "즐겨찾기추가");
        menu.add(0,2,0 ,"즐겨찾기목록");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == 1){
            webView.loadUrl("file:///android_asset/www/img/urladd.html");
            webView.setVisibility(View.VISIBLE);
            list_item.setVisibility(View.GONE);
            top.start();
            linear.setAnimation(top);
        }
        if(item.getItemId() == 2){
            list_item.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            top.start();
            linear.setAnimation(top);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        if(v.getId() == R.id.btn1){
            webView.loadUrl(et.getText().toString());
        }
    }
    Handler handler = new Handler();
    class JavaScriptMethods{

//        @JavascriptInterface
//        public void displayToast(){
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
//                    dlg.setTitle("그림변경")
//                            .setMessage("그림을 변경하시겠습니까?")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    webView.loadUrl("javascript:changeImage()");
//                                }
//                            })
//                            .setNegativeButton("Cancal",null)
//                            .show();
//                }
//            });
//        }
        @JavascriptInterface
        public void showlist(){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    linear.setVisibility(View.VISIBLE);
                }
            });
        }

        @JavascriptInterface
        public void siteaddlist(final String sitename , final String url){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(adapter.getCount() == 0) {
                        adapter.addItem(sitename, url);
                        Toast.makeText(getApplicationContext(), "사이트가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                        webView.loadUrl("javascript:blank()");
                    }
                    else{
                        if(!adapter.checkitem(sitename,url)){
                            adapter.addItem(sitename,url);
                            Toast.makeText(getApplicationContext(), "사이트가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                            webView.loadUrl("javascript:blank()");
                        }
                        else{
                            webView.loadUrl("javascript:displayMsg()");
                        }
                    }
                }
            });
        }
    }

    public void setListView() {
        adapter = new CustomAdapter(arraylist);

        list_item = (ListView)findViewById(R.id.list_item);

        list_item.setAdapter(adapter);

        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                linear.setVisibility(View.VISIBLE);
                list_item.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(arraylist.get(position).getUrl());
            }
        });

        list_item.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("삭제확인");
                dlg.setMessage("선택한 사이트를 정말 삭제하시겠습니까?");
                dlg.setNegativeButton("닫기", null);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.getremove(position);

                    }
                });
                dlg.show();
                return true;
            }
        });
    }
}
