package cn.zhihu.daily.zhihu_daily.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import java.util.Calendar;

import cn.zhihu.daily.zhihu_daily.global.Constant;
import cn.zhihu.daily.zhihu_daily.model.DailyNews;
import cn.zhihu.daily.zhihu_daily.model.Detail;
import cn.zhihu.daily.zhihu_daily.util.CommonUtil;

/**
 * Created by tommy on 12/23/16.
 */
public class NewsDbHelper {

    private SQLiteDatabase writableDB, readableDB;

    public NewsDbHelper(Context context) {
        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        writableDB = databaseHandler.getWritableDatabase();
        readableDB = databaseHandler.getReadableDatabase();
    }

    public DailyNews getBeforeNewsAt(String date) {
        //  store the json corresponded to the date,
        //  while date is actually late 1 day to the json in the API
        Calendar calendar = CommonUtil.formatDateToCalendar(date);
        calendar.add(Calendar.DATE, -1);
        date = CommonUtil.getCurrentFormatDate(calendar);

        Log.d("Database", "Getting " + date);

        String SQLQueryAll =
                "select * from news_before where date = " + date;
        DailyNews result = null;
        try (Cursor sqlResult = readableDB.rawQuery(SQLQueryAll, null)) {
            if (sqlResult.moveToFirst()) {

                String json = sqlResult.getString(1);
                try {
                    result = JSON.parseObject(json, DailyNews.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public void setBeforeNewsAt(String date, String json) {
        String SQLInsert =
                "insert into news_before values (?, ?)";
        writableDB.execSQL(SQLInsert, new String[]{date, json});
    }

    public void setNewsDetail(int id, String json) {
        String SQLInsert = "insert into news_detail values (?, ?)";
        writableDB.execSQL(SQLInsert, new Object[]{id, json});
    }

    public Detail getNewsDetail(int id) {
        Log.d("Getting news detail", Integer.toString(id));
        String SQLQuery = "Select * from news_detail where id = " + Integer.toString(id);
        Detail detail = null;
        try (Cursor result = readableDB.rawQuery(SQLQuery, null)) {
            if (result.moveToFirst()) {
                String json = result.getString(1);
                try {
                    detail = JSON.parseObject(json, Detail.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return detail;
    }

    public void setLatestNews(String date, String json) {
        String SQLQuery = "select * from latest_news";
        try (Cursor sqlResult = readableDB.rawQuery(SQLQuery, null)) {
            if (sqlResult.moveToFirst()) {
                String SQLUpdate = "update latest_news SET date = ? , json = ?";
                writableDB.execSQL(SQLUpdate, new Object[]{date, json});
            } else {
                String SQLInsert = "insert into latest_news values (?, ?)";
                writableDB.execSQL(SQLInsert, new Object[]{date, json});
            }
        }
    }

    public DailyNews getLatestNews(String date) {
        String SQLQuery = "select * from latest_news where date = " + date;
        DailyNews result = null;
        try (Cursor sqlResult = readableDB.rawQuery(SQLQuery, null)) {
            if (sqlResult.moveToFirst()) {
                String json = sqlResult.getString(1);
                try {
                    result = JSON.parseObject(json, DailyNews.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private class DatabaseHandler extends SQLiteOpenHelper {

        DatabaseHandler(Context context) {
            super(context, Constant.DatabaseName, null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String SQLCreateBeforeNewsTable =
                    "CREATE TABLE IF NOT EXISTS news_before (" +
                            "date CHAR(8), " +
                            "json TEXT, " +
                            "PRIMARY KEY(date) " +
                            ");";
            String SQLCreateNewsDetailTable = "CREATE TABLE IF NOT EXISTS news_detail (" +
                            "id INT, " +
                            "json TEXT," +
                            "PRIMARY KEY(id) " +
                            ");";

            String SQLCreateLatestNewsTable = "CREATE TABLE IF NOT EXISTS latest_news (" +
                            "date CHAR(8), " +
                            "json TEXT);";
            sqLiteDatabase.execSQL(SQLCreateBeforeNewsTable);
            sqLiteDatabase.execSQL(SQLCreateNewsDetailTable);
            sqLiteDatabase.execSQL(SQLCreateLatestNewsTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
    }
}
