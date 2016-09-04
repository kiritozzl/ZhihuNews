package app.coolwhether.com.zhihudailynews.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import app.coolwhether.com.zhihudailynews.R;
import app.coolwhether.com.zhihudailynews.entity.News;
import app.coolwhether.com.zhihudailynews.entity.NewsDetail;
import app.coolwhether.com.zhihudailynews.http.Http;
import app.coolwhether.com.zhihudailynews.http.JsonHelper;
import app.coolwhether.com.zhihudailynews.support.Utility;
import app.coolwhether.com.zhihudailynews.task.LoadNewsDetailTask;


/**
 * Created by mac on 15-2-17.
 */
public class NewsDetailActivity extends AppCompatActivity {
    private WebView mWebView;
    private News news;
    private static String share_url;
    private static final String TAG = "NewsDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);

        mWebView = (WebView) findViewById(R.id.webview);
        setWebView(mWebView);

        news = (News) getIntent().getSerializableExtra("news");
        new LoadNewsDetailTask(mWebView).execute(news.getId());
    }

    private void setWebView(WebView mWebView) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
    }

    public static void startActivity(Context context, News news) {
        if (Utility.checkNetworkConnection(context)) {
            Intent i = new Intent(context, NewsDetailActivity.class);
            i.putExtra("news", news);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Utility.noNetworkAlert(context);
        }
    }

    public static void getShareUrl(String url){
        share_url = url;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting){
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
            share.putExtra(Intent.EXTRA_TEXT, share_url);

            startActivity(Intent.createChooser(share, "Share link!"));
        }
        return true;
    }
}
