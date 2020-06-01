package com.dasturchi.app.vadiybazar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper{
    public DbHelper(Context context) {
        super(context, "baza", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE savol(id INTEGER PRIMARY KEY AUTOINCREMENT,savolNomeri VARCHAR(20))");
        db.execSQL("CREATE TABLE login(id INTEGER PRIMARY KEY AUTOINCREMENT,log VARCHAR(20),par VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Cursor getSavolNomeri(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM savol",null);
        return cursor;
    }
    public void setSavolNomeri(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("savolNomeri","0");
        db.insert("savol",null,cv);
    }
    public void updateSavolNomeri(String i){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("savolNomeri",i);
        db.update("savol",cv,"id=1",null);
    }
    public void setLogin(String log,String par){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM login",null);
        ContentValues cv = new ContentValues();
        cv.put("log",log);
        cv.put("par",par);
        if (cursor.getCount()>0){
            db.update("login",cv,"id=1",null);
        }else{
            db.insert("login",null,cv);
        }
    }
    public ArrayList<String> getLogin(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM login",null);
        ArrayList<String> list = new ArrayList<>();
        if (cursor.getCount()>0){
           while (cursor.moveToNext()){
               list.add(cursor.getString(1));
               list.add(cursor.getString(2));
           }
        }
        return list;
    }


}
