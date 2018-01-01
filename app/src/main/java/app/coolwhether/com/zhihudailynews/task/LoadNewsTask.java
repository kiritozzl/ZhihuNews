package app.coolwhether.com.zhihudailynews.task;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import app.coolwhether.com.zhihudailynews.adapter.NewsAdapter;
import app.coolwhether.com.zhihudailynews.entity.News;
import app.coolwhether.com.zhihudailynews.http.Http;
import app.coolwhether.com.zhihudailynews.http.JsonHelper;

/**
 * Created by mac on 15-2-3.
 */
public class LoadNewsTask extends AsyncTask<Long, Void, List<News>> {
    private NewsAdapter adapter;
    private onFinishListener listener;

    public LoadNewsTask(NewsAdapter adapter) {
        super();
        this.adapter = adapter;
    }

    public LoadNewsTask(NewsAdapter adapter, onFinishListener listener) {
        super();
        this.adapter = adapter;
        this.listener = listener;
    }

    @Override
    protected List<News> doInBackground(Long... params) {
        List<News> newsList = null;
        try {
            if (params[0] == 0){
                newsList = JsonHelper.parseJsonToList(Http.getLastNewsList());
            }
            else {
                newsList = JsonHelper.parseJsonToList(Http.getNewsDetail(params[0]));
            }
        } catch (IOException | JSONException e) {

        } finally {
            return newsList;
        }
    }

    @Override
    protected void onPostExecute(List<News> newsList) {
        if (newsList != null)
        adapter.refreshNewsList(newsList);
        if (listener != null) {
            listener.afterTaskFinish();
        }

    }

    public interface onFinishListener {
        public void afterTaskFinish();
    }
}
