package app.coolwhether.com.zhihudailynews.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.coolwhether.com.zhihudailynews.R;
import app.coolwhether.com.zhihudailynews.db.DbFavNews;
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
    private static String images;
    private static String titles;
    private boolean isFavorite = false;

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

    public static void getShareData(String url,String image,String title){
        share_url = url;
        images = image;
        titles = title;
    }

    private void share(String content, Uri uri){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        if(uri!=null){
            //uri 是图片的地址
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/*");
            //当用户选择短信时使用sms_body取得文字
            shareIntent.putExtra("sms_body", content);
        }else{
            shareIntent.setType("text/plain");
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);
        //自定义选择框的标题
        startActivity(Intent.createChooser(shareIntent, "share link!"));
        //系统默认标题
        //startActivity(shareIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting){
            /*Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
            share.putExtra(Intent.EXTRA_TEXT, share_url);

            startActivity(Intent.createChooser(share, "Share link!"));*/
            initShareIntent("zhihu");
            //share(titles,Uri.parse(images));
        }else if (item.getItemId() == R.id.favourite_menu_news){
            DbFavNews dbFavNews = DbFavNews.getInstance(NewsDetailActivity.this);
            isFavorite = dbFavNews.isFavorite(news);
            if (isFavorite){
                dbFavNews.deleteFavorite(news);
                Toast.makeText(NewsDetailActivity.this,"该日报消息已从收藏夹中移除",Toast.LENGTH_LONG).show();
            }else {
                dbFavNews.saveFavorite(news);
                Toast.makeText(NewsDetailActivity.this,"该日报已消息收藏",Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }

    //定向使用某个应用分享
    private void initShareIntent(String type) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                //Log.e(TAG, "initShareIntent: package name---"+ info.activityInfo.packageName);
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type) ) {
                    share.putExtra(Intent.EXTRA_SUBJECT,  "subject");
                    share.putExtra(Intent.EXTRA_TEXT,     share_url);
                    //share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(myPath)) ); // Optional, just if you wanna share an image.
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return;

            startActivity(Intent.createChooser(share, "Select"));
        }
    }
}
