package app.coolwhether.com.zhihudailynews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.coolwhether.com.zhihudailynews.R;
import app.coolwhether.com.zhihudailynews.support.Constants;
import app.coolwhether.com.zhihudailynews.support.Utility;

/**
 * Created by kirito on 2017.12.31.
 */

public class SearchClass extends AppCompatActivity {
    private Button btn_search;
    private EditText et_search;
    private String date;

    private static String regEx = "[\u4e00-\u9fa5]";
    private static Pattern pat = Pattern.compile(regEx);
    private static final String TAG = "SearchClass";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        setTitle("搜索过往日报消息");
        btn_search = (Button) findViewById(R.id.btn_search);
        et_search = (EditText) findViewById(R.id.et_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year,month,day;
                date = et_search.getText().toString();
                if (isContainsChinese(date)){
                    Toast.makeText(SearchClass.this, "不能输入汉字！", Toast.LENGTH_LONG).show();
                }else if (Long.valueOf(date) < 20131119){//知乎日报的生日为 2013 年 5 月 19 日，若 before 后数字小于 20130520 ，只会接收到空消息
                    Toast.makeText(SearchClass.this, "日期不能小于20131119", Toast.LENGTH_LONG).show();
                }else if (!date.isEmpty()){
                    Calendar calendar = Utility.resetDate(date);
                    Date date = calendar.getTime();
                    //times为设置消息列表的title做准备
                    SimpleDateFormat sdf1 = new SimpleDateFormat(getString(R.string.date_formate));
                    String times = sdf1.format(date);

                    Intent intent = new Intent(SearchClass.this,SingleNewsLists.class);
                    intent.putExtra("id",Utility.dayPlus(calendar));
                    intent.putExtra("time",times);
                    startActivity(intent);
                }
            }
        });
    }

    //判断输入字符串是否包含汉字
    private static boolean isContainsChinese(String str)
    {
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find())    {
            flg = true;
        }
        return flg;
    }
}
