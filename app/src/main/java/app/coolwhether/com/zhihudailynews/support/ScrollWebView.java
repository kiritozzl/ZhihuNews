package app.coolwhether.com.zhihudailynews.support;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

/**
 * 自定义WebView,通过回调实现监听Scroll滑动隐藏，显示ActionBar
 * Created by kirito on 2017.02.12.
 */

public class ScrollWebView extends WebView {
    private static final String TAG = "ScrollWebView";
    public OnScrollListener listener;
    private static final int THRESHOLD_OFFSET = 20;
    private int scrollDistance = 0;
    private boolean controlVisible = true;

    public ScrollWebView(Context context) {
        this(context,null);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int dy = t - oldt;  //手指上滑即Scroll向下滚动的时候，dy为正

        if (listener != null){

            if (controlVisible && scrollDistance > THRESHOLD_OFFSET){
                listener.onScrollDown();
                //重置使下面的if语句起作用
                scrollDistance = 0;
                controlVisible = false;
            }else if (!controlVisible && scrollDistance < -THRESHOLD_OFFSET){
                listener.onScrollUp();
                scrollDistance = 0;
                controlVisible = true;
            }

            //当scrollDistance累计到隐藏（显示)ActionBar之后，如果Scroll向下（向上）滚动，则停止对scrollDistance的累加
            //直到Scroll开始往反方向滚动，再次启动scrollDistance的累加
            if ((controlVisible && dy > 0) || (!controlVisible && dy < 0)){
                scrollDistance += dy;
            }
        }
    }

    public void setListener(OnScrollListener listener){
        this.listener = listener;
    }

    public interface OnScrollListener{
        void onScrollUp();
        void onScrollDown();

    }
}
