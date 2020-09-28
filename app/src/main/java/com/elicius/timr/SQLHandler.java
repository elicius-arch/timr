package com.elicius.timr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class SQLHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLHandler.class.getSimpleName();
    private static final String DATABASE_NAME = "timr.db";
    private static final int DATABASE_VERSION = 1;

    //  Attribute
    private static final String TABLE_NAME_TIMR = "timrTable";
    private static final String _ID = "_id";
    private static final String SECOND = "second";
    private static final String MINUTE = "minute";
    private static final String HOUR = "hour";


//SQL-Anweisungen:
    //  Tabelle erstellen
    private static final String CREATE_TIMR_TABLE = "CREATE TABLE " + TABLE_NAME_TIMR + " (" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SECOND + " INTEGER, " + MINUTE + " INTEGER, "
            + HOUR + " INTEGER);";
    //  Tabelle löschen
    private static final String DROP_TIMR_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_TIMR;


    public SQLHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TIMR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TIMR_TABLE);
        onCreate(db);
    }

    public void insertOne(Timer timer) {
        long rowId = -1;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SECOND, timer.getSeconds());
            values.put(MINUTE, timer.getMinutes());
            values.put(HOUR, timer.getHours());

            rowId = db.insert(TABLE_NAME_TIMR, null, values);
        } catch (SQLiteException e) {
            Log.e(TAG, "insert()", e);
        } finally {
            Log.d(TAG, "insert(): rowID = " + rowId);
        }
    }

    public Timer[] selectAll() {
        Timer[] timers = null;
        try {
            SQLiteDatabase db = getWritableDatabase();
            String[] colums = {SECOND, MINUTE, HOUR};
            Cursor cursor = db.query(true,
                    TABLE_NAME_TIMR,
                    colums,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            int secondColIndex = cursor.getColumnIndex(SECOND);
            int minuteColIndex = cursor.getColumnIndex(MINUTE);
            int hourColIndex = cursor.getColumnIndex(HOUR);
            timers = new Timer[cursor.getCount()];
            int i = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int s = cursor.getInt(secondColIndex);
                int m = cursor.getInt(minuteColIndex);
                int h = cursor.getInt(hourColIndex);
                timers[i] = new Timer(s, m, h);
                cursor.moveToNext();
                i++;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return timers;
        }
    }
}
