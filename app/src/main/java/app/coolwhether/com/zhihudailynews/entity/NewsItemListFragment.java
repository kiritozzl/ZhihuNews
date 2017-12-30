package app.coolwhether.com.zhihudailynews.entity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import app.coolwhether.com.zhihudailynews.R;
import app.coolwhether.com.zhihudailynews.activity.NewsDetailActivity;
import app.coolwhether.com.zhihudailynews.adapter.NewsAdapter;
import app.coolwhether.com.zhihudailynews.support.MyApplication;
import app.coolwhether.com.zhihudailynews.support.Utility;
import app.coolwhether.com.zhihudailynews.task.LoadNewsTask;

/**
 * Created by Administrator on 2016/7/27.
 */
public class NewsItemListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener{
    private ListView lv;
    private SwipeRefreshLayout refreshLayout;
    private NewsAdapter adapter;
    private boolean isConnected;
    private Context context;
    private long id;
    private boolean isToday;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = MyApplication.getContext();
        isConnected = Utility.checkNetworkConnection(context);
        id = getArguments().getLong("id");
        isToday = getArguments().getBoolean("isToday");
    }

    /**
     * 获取NewsItemListFragment实例
     * @param id
     * @param isToday
     * @RETURN
     */
    public static NewsItemListFragment newInstance(long id,boolean isToday){
        NewsItemListFragment fragment = new NewsItemListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id",id+1);
        bundle.putBoolean("isToday",isToday);
        fragment.setArguments(bundle);

        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment_layout,container,false);

        lv = (ListView) view.findViewById(R.id.lv);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        adapter = new NewsAdapter(context, R.layout.listview_item);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        if (isConnected){
            if (isToday){
                new LoadNewsTask(adapter).execute(0l);
            }
            else {
                new LoadNewsTask(adapter).execute(id);
            }
        }
        else Utility.noNetworkAlert(context);
        return view;
    }

    /**
     * SwipeRefreshLayout 添加刷新监听
     * 通过为LoadNewsTask创建新的构造方法，实现内容加载完成后，停止刷新效果
     */
    @Override
    public void onRefresh() {
        if (isConnected) {
            if (isToday){
                new LoadNewsTask(adapter, new LoadNewsTask.onFinishListener() {
                    @Override
                    public void afterTaskFinish() {
                        refreshLayout.setRefreshing(false);
                    }
                }).execute(0l);
            }
            else {
                new LoadNewsTask(adapter, new LoadNewsTask.onFinishListener() {
                    @Override
                    public void afterTaskFinish() {
                        refreshLayout.setRefreshing(false);
                    }
                }).execute(id);
            }
        } else {
            Utility.noNetworkAlert(context);
            refreshLayout.setRefreshing(false);
        }
    }

    /**
     * 在listview的item里添加点击事件
     * 点击打开NewsDetailActivity，并且传递参数id
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsDetailActivity.startActivity(context, adapter.getItem(position));
    }
}
