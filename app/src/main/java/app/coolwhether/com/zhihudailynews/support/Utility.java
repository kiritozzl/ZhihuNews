package app.coolwhether.com.zhihudailynews.support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by mac on 15-3-5.
 */
public class Utility {

    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activityNetwork = cm.getActiveNetworkInfo();
        return activityNetwork != null && activityNetwork.isConnected();
    }

    public static void noNetworkAlert(Context context) {
        Toast.makeText(context, "No Network", Toast.LENGTH_SHORT).show();
    }

    public static Calendar resetDate(String date){
        String year,month,day;
        Calendar calendar = null;
        if (!date.isEmpty()) {
            year = date.substring(0, 4);
            month = date.substring(4, 6);
            day = date.substring(6, date.length());

            calendar = Calendar.getInstance();
            //根据输入日期重置当前calender日期
            calendar.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
        }
        return calendar;
    }

    public static long dayPlus(Calendar calendar){
        calendar.add(Calendar.DAY_OF_YEAR, 1);//为了使id比日期+1
        String time = Constants.Dates.simpleDateFormat.format(calendar.getTime());
        //t为所选取的日期，当天知乎日报新闻的id

        return Long.parseLong(time);
    }
}
