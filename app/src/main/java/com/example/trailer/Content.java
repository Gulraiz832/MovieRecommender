package com.example.trailer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Content extends ContentProvider {
    static UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static UriMatcher uriMatcher2=new UriMatcher(UriMatcher.NO_MATCH);
    DataBaseHelper dataBaseHandler;
    SQLiteDatabase db;
    static{
        uriMatcher.addURI("com.example.Trailer.Content","userTable",1);

    }
    @Override
    public boolean onCreate() {
        dataBaseHandler =new DataBaseHelper(getContext());
        db=dataBaseHandler.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor=null;
        if(uriMatcher.match(uri)==1) {
            // strings/projection represent which thing has to be selected
            // selection  represents name LIKE ?,date ==? etc in Where clause
            // strings selectionArgs reresents the arguments in WHere clause
            // s1 reprsents sorting order
            cursor = db.query("userTable",strings,s,strings1,null,null,s1);

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db2=dataBaseHandler.getWritableDatabase();
        if(uriMatcher.match(uri)==1) {
             db2.insertWithOnConflict("userTable",null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
             return uri;

        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db2=dataBaseHandler.getWritableDatabase();
        if(uriMatcher.match(uri)==1) {
            db2.delete("userTable",s,strings);
            return 1;

        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db2=dataBaseHandler.getWritableDatabase();
        if(uriMatcher.match(uri)==1) {
            db2.update("userTable",contentValues,s,strings);
            return 1;

        }
        return 0;
    }
}
