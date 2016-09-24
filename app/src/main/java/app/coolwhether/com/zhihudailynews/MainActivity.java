package app.coolwhether.com.zhihudailynews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.coolwhether.com.zhihudailynews.activity.CalendarActivity;
import app.coolwhether.com.zhihudailynews.activity.FavoriteActivity;
import app.coolwhether.com.zhihudailynews.entity.NewsItemListFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPager pager;
    private static final int PAGE_COUNT = 7;
    private PagerAdapter adapter;
    private PagerSlidingTabStrip tabs;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.viewpager);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager.setOffscreenPageLimit(PAGE_COUNT);
        adapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        /*FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_pick_date);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //load custom menu
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


    /**
     * add menu item select listener
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.calendar_menu){
            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.favourite_menu){
            Intent intent = new Intent(this, FavoriteActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private class PagerAdapter extends FragmentPagerAdapter{

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //目前时间减position天
            c.add(Calendar.DAY_OF_YEAR, -position);
            String time = sdf.format(c.getTime());
            long t = Long.parseLong(time);
            //Log.e(TAG, "getItem: t---"+t );

            return NewsItemListFragment.newInstance(t,position == 0);
        }

        /**
         * set pager title with date
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            Calendar displayDate = Calendar.getInstance();
            displayDate.add(Calendar.DAY_OF_YEAR, -position);

            return (position == 0 ? getString(R.string.zhihu_daily_today) + " " : "")
                    + DateFormat.getDateInstance().format(displayDate.getTime());
        }

    }

}
