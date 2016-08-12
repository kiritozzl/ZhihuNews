package app.coolwhether.com.zhihudailynews.support;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/7/27.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
