package cn.shineiot.base.utils;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.shineiot.base.BaseApplication;

/**
 * @author GF63
 */
public class TimeUtil {

    /**
     * 根据时间戳获取*月*日
     */
    public static String getMonthDay(long time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        return simpleDateFormat.format(new Date(time));
    }

    /**
     * 根据时间戳获取*年*月*日
     */
    public static String getYearMonthDay(long time) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date(time));
    }

    /**
     * 星期几
     */
    public static String getWeekly(long time) throws ParseException {
        String[] arr = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

        String ymd = getYearMonthDay(time);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = simpleDateFormat.parse(ymd);

        Calendar calendar = Calendar.getInstance();

        assert date != null;
        calendar.setTime(date);

        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        if (index < 0) {
            index = 0;
        }
        return arr[index];
    }

    /**
     * 上午/下午/夜间
     */
    public static String getPeriod() {
        String period = null;

        Calendar mCalendar = Calendar.getInstance();
        int hours = mCalendar.get(Calendar.HOUR_OF_DAY);

        //是否是24小时制
        boolean bl = DateFormat.is24HourFormat(BaseApplication.context());

        if (hours < 5) {
            period = "夜间";
        } else if (hours < 11) {
            period = "上午";
        } else if (hours < 13) {
            period = "中午";
        } else if (hours < 18) {
            period = "下午";
        } else {
            period = "晚上";
        }
        return period;
    }
}
