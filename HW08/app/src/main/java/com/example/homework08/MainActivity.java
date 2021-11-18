package com.example.homework08;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;

public class MainActivity extends FragmentActivity implements com.example.homework08.MainCallbacks {
    FragmentTransaction ft;
    SQLiteDatabase database;
    String myDatabasePath;
    com.example.homework08.FragmentRed redFragment;
    com.example.homework08.FragmentBlue blueFragment;
    com.example.homework08.Student[] students;
    com.example.homework08.Class[] classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File storagePath = getApplication().getFilesDir();
        myDatabasePath = storagePath + "/" + "myDatabase";

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        ft = getSupportFragmentManager().beginTransaction();
        blueFragment = com.example.homework08.FragmentBlue.newInstance("first-blue");
        ft.replace(R.id.main_holder_blue, blueFragment);
        ft.commit();

        ft = getSupportFragmentManager().beginTransaction();
        redFragment = com.example.homework08.FragmentRed.newInstance("first-red");
        ft.replace(R.id.main_holder_red, redFragment);
        ft.commit();

        blueFragment.getDataFromDatabase();
        students = blueFragment.get_students();
        classes = blueFragment.get_classes();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createDatabase();

                //Toast.makeText(MainActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMsgFromFragToMain(String sender, int position) {
        if(sender.equals("RED")) {
            try {
                redFragment.onMsgFromMainToFragment(position);
                blueFragment.onMsgFromMainToFragment(position);
            }
            catch (Exception e) {
                Log.e("ERROR", "onStringFromFragToMain" + e.getMessage());
            }
        }
        if (sender.equals("BLUE")) {
            try {
                redFragment.onMsgFromMainToFragment(position);
            }
            catch (Exception e) {
                Log.e("ERROR", "onStringFromFragToMain" + e.getMessage());
            }
        }
    }

    private void createDatabase() {

        try {
            database = SQLiteDatabase.openDatabase(myDatabasePath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            createTables();
            insertTables();
            queryDatabase();
            database.close();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        };
    }

    private void createTables() {
        try {
            database.execSQL("DROP TABLE IF EXISTS HOCSINH;");
            database.execSQL("DROP TABLE IF EXISTS LOPHOC;");
            database.execSQL("CREATE TABLE HOCSINH ( "
                    + "STUDENTID TEXT PRIMARY KEY, "
                    + "NAME TEXT, "
                    + "CLASSID INTEGER, "
                    + "AVG FLOAT);");
            database.execSQL("CREATE TABLE LOPHOC ( "
                    + "CLASSID INTEGER PRIMARY KEY, "
                    + "CLASSNAME TEXT);");
            //Toast.makeText(this, "Create Tables Completed", Toast.LENGTH_SHORT).show();

        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    private void insertTables() {
        try {
            database.execSQL("INSERT INTO LOPHOC VALUES (0, '19_3')");
            database.execSQL("INSERT INTO HOCSINH VALUES ('19120302','Doan Thu Ngan',0,10)");
            database.execSQL("INSERT INTO HOCSINH VALUES ('19120383','Huynh Tan Tho',0,10)");
            database.execSQL("INSERT INTO HOCSINH VALUES ('19120426','Phan Dang Diem Uyen',0,10)");
            database.execSQL("INSERT INTO HOCSINH VALUES ('19120469','Su Nhat Dang',0,10)");
            database.execSQL("INSERT INTO HOCSINH VALUES ('19120492','Do Thai Duy',0,10)");
            //Toast.makeText(this, "Insert Completed", Toast.LENGTH_SHORT).show();
        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
    private void queryDatabase() {
        Cursor cursor = database.rawQuery("select count(*) as 'SLSV' from HOCSINH", null);
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            Toast.makeText(this, count + " HS", Toast.LENGTH_SHORT).show();
        }
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public String getMyDatabasePath() {
        return myDatabasePath;
    }
}