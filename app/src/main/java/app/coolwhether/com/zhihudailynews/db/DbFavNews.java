package app.coolwhether.com.zhihudailynews.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import app.coolwhether.com.zhihudailynews.entity.News;

/**
 * Created by kirito on 2016/9/6.
 */
public class DbFavNews  {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private static DbFavNews dbFavNews;

    public DbFavNews(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public static synchronized DbFavNews getInstance(Context context){
        if (dbFavNews == null){
            dbFavNews = new DbFavNews(context);
        }
        return dbFavNews;
    }

    public void saveFavorite(News news){
        if (news != null){
            ContentValues values = new ContentValues();
            values.put(DbHelper.COLUMN_NEWS_ID,news.getId());
            values.put(DbHelper.COLUMN_NEWS_IMG,news.getImage());
            values.put(DbHelper.COLUMN_NEWS_TITLE,news.getTitle());
            db.insert(DbHelper.TABLE_NAME,null,values);
        }
    }

    public List<News> loadFavorite(){
        List<News> favoriteList = new ArrayList<>();
        Cursor cursor = db.query(DbHelper.TABLE_NAME,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                News news = new News();
                news.setId(cursor.getInt(1));
                news.setImage(cursor.getString(2));
                news.setTitle(cursor.getString(3));
                favoriteList.add(news);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return favoriteList;
    }

    public boolean isFavorite(News news){
        Cursor cursor = db.query(DbHelper.TABLE_NAME,null,DbHelper.COLUMN_NEWS_ID + "= ?"
                ,new String[]{news.getId() + ""},null,null,null);
        if (cursor.moveToNext()){
            cursor.close();
            return true;
        }else {
            return false;
        }
    }

    public void deleteFavorite(News news){
        if (news != null){
            db.delete(DbHelper.TABLE_NAME,DbHelper.COLUMN_NEWS_ID + "= ?",new String[]{news.getId() + ""});
        }
    }
}
