package app.coolwhether.com.zhihudailynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.squareup.timessquare.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.coolwhether.com.zhihudailynews.R;
import app.coolwhether.com.zhihudailynews.support.Constants;

/**
 * Created by Administrator on 2016/7/27.
 */
public class CalendarActivity extends AppCompatActivity {
    private CalendarPickerView pickerView;
    private static final String TAG = "CalendarActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.calendar_layout);

        Calendar nextYear = Calendar.getInstance();
        //设置日期为当前日期的后一天
        nextYear.add(Calendar.DAY_OF_YEAR,1);
        pickerView = (CalendarPickerView) findViewById(R.id.calendar_view);
        pickerView.init(Constants.Dates.birthday,nextYear.getTime())
                .withSelectedDate(Calendar.getInstance().getTime());
        pickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Intent intent = new Intent(CalendarActivity.this,SingleNewsLists.class);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String time = Constants.Dates.simpleDateFormat.format(calendar.getTime());
                Log.e(TAG, "onDateSelected: time1---"+time );

                //t为所选取的日期，当天知乎日报新闻的id
                long t = Long.parseLong(time);
                intent.putExtra("id",t);

                //times为设置消息列表的title做准备
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                SimpleDateFormat sdf1 = new SimpleDateFormat(getString(R.string.date_formate));
                String times = sdf1.format(c.getTime());
                Log.e(TAG, "onDateSelected: ---times"+times );
                intent.putExtra("time",times);
                startActivity(intent);
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
