package com.example.nicko.turuntanganmalangapps.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by nicko on 7/2/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SQLiteDatabase.db";

    public static final String TABLE_NAME = "NOTIFICATION_LOG";
    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String BODY = "BODY";
    public static final String MESSAGE = "MESSAGE";
    public static final String MESSAGE_TYPE = "MESSAGE_TYPE";
    public static final String INTENT = "INTENT";
    public static final String ID_TARGET = "ID_TARGET";

    private SQLiteDatabase database;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table " + TABLE_NAME + " ( "
                        + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + TITLE + " VARCHAR, "
                        + BODY + " VARCHAR, "
                        + MESSAGE + " VARCHAR, "
                        + MESSAGE_TYPE + " VARCHAR, "
                        + INTENT + " VARCHAR, "
                        + ID_TARGET + " VARCHAR);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertRecord(NotificationModel notifModel) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, notifModel.getTitle());
        contentValues.put(BODY, notifModel.getBody());
        contentValues.put(MESSAGE, notifModel.getMessage());
        contentValues.put(MESSAGE_TYPE, notifModel.getMessage_type());
        contentValues.put(INTENT, notifModel.getIntent());
        contentValues.put(ID_TARGET, notifModel.getId_target());
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public ArrayList<NotificationModel> getAllRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<NotificationModel> notif = new ArrayList<NotificationModel>();
        NotificationModel notifModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                notifModel = new NotificationModel();
                notifModel.setId(cursor.getString(0));
                notifModel.setTitle(cursor.getString(1));
                notifModel.setBody(cursor.getString(2));
                notifModel.setMessage(cursor.getString(3));
                notifModel.setMessage_type(cursor.getString(4));
                notifModel.setIntent(cursor.getString(5));
                notifModel.setId_target(cursor.getString(6));

                notif.add(notifModel);
            }
        }
        cursor.close();
        database.close();

        return notif;
    }

    public void updateRecord(NotificationModel notifModel) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, notifModel.getTitle());
        contentValues.put(BODY, notifModel.getBody());
        contentValues.put(MESSAGE, notifModel.getMessage());
        contentValues.put(MESSAGE_TYPE, notifModel.getMessage_type());
        contentValues.put(INTENT, notifModel.getIntent());
        contentValues.put(ID_TARGET, notifModel.getId_target());
        database.update(TABLE_NAME, contentValues, ID + " = ?", new String[]{notifModel.getId()});
        database.close();
    }

    public void deleteAllRecords() {
        database = this.getReadableDatabase();
        database.delete(TABLE_NAME, null, null);
        database.close();
    }

    public void deleteRecord(NotificationModel notifModel) {
        database = this.getReadableDatabase();
        database.delete(TABLE_NAME, ID + " = ?", new String[]{notifModel.getId()});
        database.close();
    }

    public ArrayList<String> getAllTableName() {
        database = this.getReadableDatabase();
        ArrayList<String> allTableNames = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                allTableNames.add(cursor.getString(cursor.getColumnIndex("name")));
            }
        }
        cursor.close();
        database.close();
        return allTableNames;
    }
}
