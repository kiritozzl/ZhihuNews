package app.coolwhether.com.zhihudailynews.support;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by kirito on 2017.12.30.
 */

public class EnhanceWebView extends WebView {
    private onScrollChangeCallback callback;

    public EnhanceWebView(Context context) {
        super(context);
    }

    public EnhanceWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EnhanceWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (callback != null){
            callback.onScroll(l-oldl,t-oldt);
        }
    }

    public onScrollChangeCallback getOnScrollChangeCallback(){
        return callback;
    }

    public void setScrollChangeCallback(onScrollChangeCallback callback){
        this.callback = callback;
    }

    //设置回调借口，获取webview滑动的上下，左右距离差
    public static interface onScrollChangeCallback{
        public void onScroll(int dx,int dy);
    }
}
