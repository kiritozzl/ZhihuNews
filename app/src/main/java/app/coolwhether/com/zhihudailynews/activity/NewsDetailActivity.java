package app.coolwhether.com.zhihudailynews.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.List;

import app.coolwhether.com.zhihudailynews.R;
import app.coolwhether.com.zhihudailynews.db.DbFavNews;
import app.coolwhether.com.zhihudailynews.entity.News;
import app.coolwhether.com.zhihudailynews.entity.NewsDetail;
import app.coolwhether.com.zhihudailynews.http.Http;
import app.coolwhether.com.zhihudailynews.http.JsonHelper;
import app.coolwhether.com.zhihudailynews.support.ScrollWebView;
import app.coolwhether.com.zhihudailynews.support.Utility;
import app.coolwhether.com.zhihudailynews.task.LoadNewsDetailTask;


/**
 * Created by mac on 15-2-17.
 */
public class NewsDetailActivity extends AppCompatActivity {
    private ScrollWebView mWebView;
    private News news;
    private static String share_url;
    private static String images;
    private static String titles;
    private boolean isFavorite = false;

    private static Tencent mTencent;
    private static final String mAppid = "1105642919";

    private float webHeight;
    private static final String TAG = "NewsDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);

        mWebView = (ScrollWebView) findViewById(R.id.webview);
        setWebView(mWebView);

        news = (News) getIntent().getSerializableExtra("news");
        new LoadNewsDetailTask(mWebView).execute(news.getId());

        mWebView.setListener(new ScrollWebView.OnScrollListener() {
            @Override
            public void onScrollUp() {
                getSupportActionBar().show();
            }

            @Override
            public void onScrollDown() {
                getSupportActionBar().hide();
            }
        });

        if (mTencent == null){
            mTencent = Tencent.createInstance(mAppid,getApplicationContext());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    //View.SYSTEM_UI_FLAG_LAYOUT_STABLE //隐藏actionBar
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);//使得即使后面触动屏幕状态栏也不会弹出，保持隐藏
        }
    }

    //创建qq分享链接
    public void shareToQq(){
        final Bundle  params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,share_url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,"zhi hu daily news");
        params.putString(QQShare.SHARE_TO_QQ_TITLE,titles);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,images);
        doShareToqq(params);
    }

    private void doShareToqq(final Bundle bundle){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTencent != null){
                    mTencent.shareToQQ(NewsDetailActivity.this,bundle,qqListener);
                }
            }
        });
    }

    IUiListener qqListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {

        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(NewsDetailActivity.this,"share error" + uiError.errorMessage,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,qqListener);
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

            //share(titles,Uri.parse(images));
            shareToQq();
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
        }/*else if (item.getItemId() == R.id.share_to_dudu){//分享到读读日报
            initShareIntent("com.zhihu.circlely");
        }*/
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
