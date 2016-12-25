package cn.zhihu.daily.zhihu_daily.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by tommy on 12/10/16.
 */

public class CommonUtil {
    private View rootView;

    public CommonUtil(View rootView) {
        this.rootView = rootView;
    }

    public void promptMsg(String text) {
        Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT).show();
    }

    static public String getMD5(String message) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        BigInteger bigInteger = new BigInteger(1, messageDigest.digest(message.getBytes()));
        return bigInteger.toString(16);
    }

    static public String getCurrentFormatDate(Calendar calendar) {
        return Integer.toString(calendar.get(Calendar.YEAR)) +
                (calendar.get(Calendar.MONTH) + 1 < 10 ? "0":"") +
                Integer.toString(calendar.get(Calendar.MONTH) + 1) +
                (calendar.get(Calendar.DATE) < 10 ? "0":"") +
                Integer.toString(calendar.get(Calendar.DATE));
    }

    static public Calendar formatDateToCalendar(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    static public String StreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            total.append(line).append('\n');
        }
        return total.toString();
    }

}
