package cn.zhihu.daily.zhihu_daily.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.zhihu.daily.zhihu_daily.global.Constant;
import cn.zhihu.daily.zhihu_daily.model.DailyNews;
import cn.zhihu.daily.zhihu_daily.model.Summary;

/**
 * Created by tommy on 12/23/16.
 */
public class DBNewsBefore {

    private DatabaseHandler databaseHandler;
    private SQLiteDatabase writableDB, readableDB;

    public DBNewsBefore(Context context) {
        databaseHandler = new DatabaseHandler(context);
        writableDB = databaseHandler.getWritableDatabase();
        readableDB = databaseHandler.getReadableDatabase();
    }

    public DailyNews getBeforeNewsAt(String date) {
        //  store the json corresponded to the date,
        //  while date is actually late 1 day to the json in the API
        Calendar calendar = Calendar.getInstance();
        calendar.set(
               Integer.parseInt(date.substring(0, 4)),
               Integer.parseInt(date.substring(4, 6)) - 1,
               Integer.parseInt(date.substring(6))
        );
        calendar.add(Calendar.DATE, -1);
        date = Integer.toString(calendar.get(Calendar.YEAR)) +
                Integer.toString(calendar.get(Calendar.MONTH) + 1) +
                Integer.toString(calendar.get(Calendar.DATE));

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
        writableDB.execSQL(SQLInsert, new String[] {date, json});
    }

    private class DatabaseHandler extends SQLiteOpenHelper {

        DatabaseHandler(Context context) {
            super(context, Constant.DatabaseName, null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String SQLCreateTable =
                    "CREATE TABLE IF NOT EXISTS news_before (" +
                            "date CHAR(8), " +
                            "json TEXT, " +
                            "PRIMARY KEY(date) " +
                            ")";
            sqLiteDatabase.execSQL(SQLCreateTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
    }
}
