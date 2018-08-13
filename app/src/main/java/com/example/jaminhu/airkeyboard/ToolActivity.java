package com.example.jaminhu.airkeyboard;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaminhu.airkeyboard.data.DictionaryContract.DictionaryEntry;
import com.example.jaminhu.airkeyboard.MyPlayground;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ToolActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LOADER_ID = 2;

    private String mCurrentFinseq;

    EditText inputView;
    Button calculator;
    TextView outputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool);

        inputView = findViewById(R.id.input_view);
        calculator = findViewById(R.id.calculate);
        outputView = findViewById(R.id.output_view);

        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = inputView.getText().toString();

                String[] inputWordArray = inputString.split(" ");

                StringBuilder siblingsStringBuilder = new StringBuilder();

                for (int i = 0; i<inputWordArray.length; i++){

                    String currentWord = inputWordArray[i];

                    mCurrentFinseq = MyPlayground.getFingerSequence(currentWord);

                    //query with different selection arg and find sibling words

                    String[] projection = {
                            DictionaryEntry._ID,
                            DictionaryEntry.WORD_COLUMN,
                            DictionaryEntry.FINSEQ_COLUMN};

                    String selection = DictionaryEntry.FINSEQ_COLUMN + "=?";

                    String[] selectionArgs = {mCurrentFinseq};

                    Cursor cursor = getContentResolver().query(DictionaryEntry.CONTENT_URI,
                            projection,
                            selection,
                            selectionArgs,
                            null);

                    //get sibling words from cursor

                    while (cursor.moveToNext()){
                        String currentSibling = cursor.getString(cursor.getColumnIndex(DictionaryEntry.WORD_COLUMN));


                        siblingsStringBuilder.append(currentSibling);
                        if (!cursor.isLast()) {
                            siblingsStringBuilder.append("/");
                        }
                    };

                    //add sibling words from cursor to outputStringBuilder with dashes

                    siblingsStringBuilder.append( " " );

                }

                outputView.setText(siblingsStringBuilder.toString());

            }
        });
    }

    private void calculate(){
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                DictionaryEntry._ID,
                DictionaryEntry.WORD_COLUMN,
                DictionaryEntry.FINSEQ_COLUMN};

        String selection = DictionaryEntry.FINSEQ_COLUMN + "=?";

        String[] selectionArgs = {mCurrentFinseq};

        return new CursorLoader(
                this,
                DictionaryEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
