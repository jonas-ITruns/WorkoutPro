package com.developer.workoutpro.itruns.workoutpro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jan on 24.03.2018.
 */

public class ScoutLogDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "scout.db";
    //Versionsnummer
    public static final int DB_VERSION = 1;
    //Tabellen und Spaltennamen
    public static final String TABLE_SCOUTLOG ="scoutlog";
    //Eigenschaften als Spalten der Tabelle
    public static final String COLUMN_ID = "id";
    //Spalten für Überschrift und INhalt
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_IMAGE = "image";

    public static final String SQL_CREATE =
            "create table " + TABLE_SCOUTLOG + "(" +
                    //Angaben zu den einzelnen Spalten
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_TITLE + " text not null, " +
                    COLUMN_CONTENT + " text" +
                    COLUMN_IMAGE + " text) ;";


    public ScoutLogDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
