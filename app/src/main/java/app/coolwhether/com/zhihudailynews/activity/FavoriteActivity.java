package app.coolwhether.com.zhihudailynews.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import app.coolwhether.com.zhihudailynews.R;
import app.coolwhether.com.zhihudailynews.adapter.NewsAdapter;
import app.coolwhether.com.zhihudailynews.db.DbFavNews;
import app.coolwhether.com.zhihudailynews.entity.News;

/**
 * Created by kirito on 2016/9/6.
 */
public class FavoriteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener{
    private ListView lv;
    private SwipeRefreshLayout srl;
    private List<News> favotiteList;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_fragment_layout);

        setTitle("收藏夹");
        lv = (ListView) findViewById(R.id.lv);
        srl = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        srl.setOnRefreshListener(this);

        loadData();
        lv.setOnItemClickListener(this);
    }

    private void loadData(){
        favotiteList = DbFavNews.getInstance(this).loadFavorite();
        adapter = new NewsAdapter(this,R.layout.listview_item,favotiteList);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsDetailActivity.startActivity(this, adapter.getItem(position));
    }

    @Override
    public void onRefresh() {//加载收藏链表
        if (!DbFavNews.getInstance(this).isEmpty()){
            loadData();
        }
        srl.setRefreshing(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.trash){
            new AlertDialog.Builder(this).setTitle("是否清空收藏列表？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DbFavNews.getInstance(getApplicationContext()).clearAll();
                    loadData();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();

        }
        return true;
    }

}
