package app.coolwhether.com.zhihudailynews.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import app.coolwhether.com.zhihudailynews.R;
import app.coolwhether.com.zhihudailynews.adapter.NewsAdapter;
import app.coolwhether.com.zhihudailynews.support.Utility;
import app.coolwhether.com.zhihudailynews.task.LoadNewsTask;

/**
 * Created by Administrator on 2016/7/28.
 */
public class SingleNewsLists extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
        ,AdapterView.OnItemClickListener{
    private ListView lv;
    private SwipeRefreshLayout refreshLayout;
    private boolean isConnected;
    private long id;
    private NewsAdapter adapter;
    private String time;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //下面两句是在bar添加返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setContentView(R.layout.list_fragment_layout);
        time = getIntent().getStringExtra("time");
        setTitle(time);

        isConnected = Utility.checkNetworkConnection(this);
        lv = (ListView) findViewById(R.id.lv);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        id = getIntent().getLongExtra("id",0);


        adapter = new NewsAdapter(this, R.layout.listview_item);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        if (isConnected){
            new LoadNewsTask(adapter).execute(id);
        }
        else Utility.noNetworkAlert(this);
    }

    @Override
    public void onRefresh() {
        if (isConnected) {
            new LoadNewsTask(adapter, new LoadNewsTask.onFinishListener() {
                @Override
                public void afterTaskFinish() {
                    refreshLayout.setRefreshing(false);
//                Toast.makeText(MainActivity.this, "Refresh success", Toast.LENGTH_SHORT).show();
                }
            }).execute(id);
        } else {
            Utility.noNetworkAlert(this);
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsDetailActivity.startActivity(this, adapter.getItem(position));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //返回按钮的点击事件，添加效果：返回
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
