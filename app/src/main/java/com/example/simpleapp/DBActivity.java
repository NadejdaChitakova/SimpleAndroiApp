package com.example.simpleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

public class DBActivity extends AppCompatActivity {
    //protected String dbFile = getFilesDir().getPath()+"Contacts.db"; //comment this
    protected  interface OnQuerySuccess{
        public void onSuccess();
    }
    protected  interface OnSelectSuccess{
        public void onElementSelected(String id, String name, String phoneNum, String email);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void SelectSQL(String selectQ, String[] args, OnSelectSuccess onSelectSuccess)throws Exception{
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath()+"/CONTACTS.db",null);
        Cursor cursor = db.rawQuery(selectQ, args);

        while(cursor.moveToNext()){
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("ID"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("NAME"));
            @SuppressLint("Range") String phoneNum = cursor.getString(cursor.getColumnIndex("PHONE_NUM"));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("EMAIL"));
            onSelectSuccess.onElementSelected(id, name, phoneNum, email);
        }
        db.close();
    }
    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess onQuerySuccess) throws Exception{
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath()+"/CONTACTS.db",null);
        if (args != null)
            db.execSQL(SQL, args);
        else
            db.execSQL(SQL);
        db.close();
        onQuerySuccess.onSuccess();
    }
    protected  void InitDB() throws Exception{
        ExecSQL("CREATE TABLE IF NOT EXISTS CONTACTS(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT NOT NULL," +
                "PHONE_NUM TEXT NOT NULL," +
                "EMAIL TEXT NOT NULL," +
                "UNIQUE(NAME, PHONE_NUM))",null, () ->Toast.makeText(getApplicationContext(),"DB Init successful", Toast.LENGTH_LONG).show());
    }
}
