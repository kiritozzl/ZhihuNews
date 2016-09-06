package app.coolwhether.com.zhihudailynews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
public class FavoriteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
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

        favotiteList = DbFavNews.getInstance(this).loadFavorite();
        adapter = new NewsAdapter(this,R.layout.listview_item,favotiteList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsDetailActivity.startActivity(this, adapter.getItem(position));
    }
}
