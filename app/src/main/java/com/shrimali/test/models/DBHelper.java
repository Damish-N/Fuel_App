package com.shrimali.test.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Fuel.db";

    public DBHelper(Context context) {
        super(context, "Fuel.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //1-for vehicle 0-for fuel station
        sqLiteDatabase.execSQL("create Table users(username TEXT primary key, password TEXT,district TEXT,type TEXT,fType TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String userName, String password, String type,String district,String fType) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", userName);
        contentValues.put("password", password);
        contentValues.put("type", type);
        contentValues.put("district",district);
        contentValues.put("fType",fType);
        long result = sqLiteDatabase.insert("users", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    public Boolean checkUserNamePassword(String username, String password,String type){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ? and type = ?", new String[] {username,password,type});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public String getTypeOfFuel(String username){
        String column1 = "null";
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor c = MyDB.rawQuery("Select fType from users where username = ?", new String[] {username});
        if (c.moveToFirst()){
            do {
                // Passing values
                column1 = c.getString(0);
                // Do something Here with values
            } while(c.moveToNext());
        }
        c.close();
        return column1;

    }

}
