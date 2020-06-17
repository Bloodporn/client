package com.example.client;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.client.Files.FileData;

import java.util.ArrayList;

public class SqlService extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="filiki";
    public static final String TABLE_NAME = "files";
    public static final String COL_1 = "papka";
    public static final String COL_2 = "name";
    public static final String COL_3 = "time";
    public static final String COL_4 = "parent";
    public static final String COL_5 = "fav";
    public static final String COL_6 = "size";
    public static final String COL_7 = "truepath";



    public SqlService(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME + " (papka INT DEFAULT 0, name TEXT, time LONG DEFAULT 0, parent TEXT, fav INT DEFAULT 0, size LONG DEFAULT 0, truepath TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void insertData(ArrayList<FileData> files){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        for(FileData x : files){
            cv.put(COL_1,x.isPapka);
            cv.put(COL_2, x.name);
            cv.put(COL_3, x.time);
            //cv.put(COL_4, x.parent);
            //cv.put(COL_5, x.fav);
            cv.put(COL_6, x.size);
            //cv.put(COL_7, x.truePath);
            db.insert(TABLE_NAME , null,cv);
            cv.clear();
        }
    }

    public ArrayList<FileData> getAllData(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        ArrayList<FileData> fd = new ArrayList<>();
        while(cursor.moveToNext()){
            //fd.add(new FileData(cursor.getInt(0), cursor.getString(1),
            //        cursor.getLong(2),cursor.getString(3),cursor.getInt(4),
            //        cursor.getLong(5),cursor.getString(6)));
        }
        cursor.close();
        return fd;
    }
}
