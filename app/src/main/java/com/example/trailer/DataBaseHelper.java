package com.example.trailer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String dbName = "users.db";
    public static final String userTable = "userTable";
    public static final String col1 = "Email";
    public static final String col2 = "LoginStatus";

    DataBaseHelper myDB;

    public DataBaseHelper(Context context)
    {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + userTable + " (Email String NOT NULL, LoginStatus INTEGER NOT NULL, PRIMARY KEY (Email)) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + userTable);
    }

    public boolean insertData(String email, int loginStatus)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col1, email);
        contentValues.put(col2, loginStatus);

        long result = db.insert(userTable, null, contentValues);

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean updateData(String email, int loginStatus)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col1, email);
        contentValues.put(col2, loginStatus);
        db.update(userTable, contentValues, "Email = ?", new String[] {email});
        return true;
    }


    public void deleteDateData(String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(userTable, "Email = ?", new String[] {email});
    }

    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from userTable", null);
        return cursor;
    }

}
