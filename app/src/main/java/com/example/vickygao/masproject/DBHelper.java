package com.example.vickygao.masproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    String createUserTable = "create table user_info(_id int auto_increment,username char(20),"
            + "password char(20),primary key('_id'));";
    String createGameTable = "create table game_info(_id int auto_increment,username char(20),gameid int,step int,fin int,setter char(20),xval double,yval double,hint TEXT,primary key('_id'));";


    String insertStr1 = "insert into user_info(_id,username,password) values(?,?,?)";
    String insertStr2 = "insert into game_info(_id,username,gameid,step,fin,setter,xval,yval,hint) values(?,?,?,?,?,?,?,?,?)";
    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
// TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
// TODO Auto-generated method stub
        int _id = 0;
        Log.d("yume", "This is Debug.");
        db.execSQL(createUserTable);
        db.execSQL(createGameTable);
        Log.d("yume", "This is Debug.");
// �������data
        String[] insertValue1 = { "0", "system", "admin" };
        String[] insertValue2 = { "0", "system","1","0","1","system","0","0","the original place." };
        db.execSQL(insertStr1, insertValue1);
        db.execSQL(insertStr2, insertValue2);
        Log.d("yume", "This is Debug.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// TODO Auto-generated method stub
    }
}