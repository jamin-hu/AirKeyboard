package com.example.jaminhu.airkeyboard;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.jaminhu.airkeyboard.data.DictionaryContract.DictionaryEntry;

import org.w3c.dom.Text;

public class DictionaryCursorAdapter extends CursorAdapter {

    public DictionaryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View emptyListItem = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        // what is are these "false" doing here---------------------------------------^^^^^^------------^^^^
        return emptyListItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameView = view.findViewById(R.id.word_view);
        nameView.setText(cursor.getString(cursor.getColumnIndex(DictionaryEntry.WORD_COLUMN)));

        TextView priceView = view.findViewById(R.id.freq_view);
        priceView.setText(cursor.getString(cursor.getColumnIndex(DictionaryEntry._ID)));

        TextView quantityView = view.findViewById(R.id.finseq_view);
        quantityView.setText(cursor.getString(cursor.getColumnIndex(DictionaryEntry.FINSEQ_COLUMN)));

        TextView siblingsView = view.findViewById(R.id.siblings_view);
        siblingsView.setText(cursor.getString(cursor.getColumnIndex(DictionaryEntry.SIBLING_COLUMN)));

    }
}
