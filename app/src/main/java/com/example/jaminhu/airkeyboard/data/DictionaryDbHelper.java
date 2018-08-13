package com.example.jaminhu.airkeyboard.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.jaminhu.airkeyboard.data.DictionaryContract.DictionaryEntry;

public class DictionaryDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dictionary.db";

    public DictionaryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WORDS_TABLE = "CREATE TABLE " + DictionaryEntry.TABLE_NAME + " (" +
                DictionaryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DictionaryEntry.WORD_COLUMN + " TEXT NOT NULL, " +
                DictionaryEntry.FINSEQ_COLUMN + " TEXT NOT NULL, " +
                DictionaryEntry.SIBLING_COLUMN + " INTEGER DEFAULT 0" +
                ");";

        db.execSQL(CREATE_WORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
