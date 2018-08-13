package com.example.jaminhu.airkeyboard;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaminhu.airkeyboard.data.DictionaryContract.DictionaryEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1;

    private DictionaryCursorAdapter mCursorAdapter;

    private String[] threeColumnProjection = {
            DictionaryEntry._ID,
            DictionaryEntry.WORD_COLUMN,
            DictionaryEntry.FINSEQ_COLUMN};

    Button initializer;

    Button tool;

    LinearLayout catalogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        catalogView = findViewById(R.id.catalog_view);
        catalogView.setKeepScreenOn(true);

        initializer = findViewById(R.id.initilize);

        initializer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayOnHoldMessage();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        makeFinseqDatabase();
                        insertSiblingSize();
                    }
                }, 100);
            }
        });

        tool = findViewById(R.id.tool);
        tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this, ToolActivity.class);
                startActivity(intent);
            }
        });

        mCursorAdapter = new DictionaryCursorAdapter(this, null);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, CatalogActivity.this);

    }

    private void makeFinseqDatabase() {

        InputStream is = getResources().openRawResource(R.raw.kilgariff5000);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        Uri newUri = null;
        try {
            while ((line = br.readLine()) != null) { // <--------- place readLine() inside loop

                ContentValues values = new ContentValues();

                values.put(DictionaryEntry.WORD_COLUMN, line);
                values.put(DictionaryEntry.FINSEQ_COLUMN, MyPlayground.getFingerSequence(line));

                newUri = getContentResolver().insert(DictionaryEntry.CONTENT_URI, values);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        Toast.makeText(this, String.valueOf(ContentUris.parseId(newUri)), Toast.LENGTH_LONG).show();
    }



    private int howManyShareThisFinseq(String currentFinseq){

        String[] args = {currentFinseq};

        Cursor cursor = getContentResolver().query(
                DictionaryEntry.CONTENT_URI,
                threeColumnProjection,
                DictionaryEntry.FINSEQ_COLUMN + "=?",
                args,
                null);

        int finseqSiblings = cursor.getCount();

        cursor.close();

        return finseqSiblings;
    }


    private void insertSiblingSize(){

        Cursor cursor = getContentResolver().query(
                DictionaryEntry.CONTENT_URI,
                threeColumnProjection,
                null,
                null,
                null);

        while (cursor.moveToNext()){

            String currentFinseq = cursor.getString(cursor.getColumnIndex(DictionaryEntry.FINSEQ_COLUMN));
            int currentID = cursor.getInt(cursor.getColumnIndex(DictionaryEntry._ID));
            ContentValues values = new ContentValues();

            values.put(DictionaryEntry.SIBLING_COLUMN, howManyShareThisFinseq(currentFinseq));

            Uri currentUri = ContentUris.withAppendedId(DictionaryEntry.CONTENT_URI, currentID);

            getContentResolver().update(currentUri, values, null, null);
        }

        cursor.close();
    }

    public void displayOnHoldMessage(){
        initializer.setText("Just wait a bit, up to a few minutes... Don't let the screen darken though!");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                DictionaryEntry._ID,
                DictionaryEntry.WORD_COLUMN,
                DictionaryEntry.FINSEQ_COLUMN,
                DictionaryEntry.SIBLING_COLUMN};

        return new CursorLoader(this,
                //Wait, how is CursorLoader and Loader<Cursor> the same thing?
                DictionaryEntry.CONTENT_URI,
                //So here ^ it wants the address of the database
                projection,
                null,
                null,
                DictionaryEntry.SIBLING_COLUMN + " DESC, " + DictionaryEntry.FINSEQ_COLUMN + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

        if (mCursorAdapter.isEmpty()){
            initializer.setVisibility(VISIBLE);
        } else {
            initializer.setVisibility(GONE);
        }

        catalogView.setKeepScreenOn(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }


}
