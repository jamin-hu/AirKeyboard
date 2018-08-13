package com.example.jaminhu.airkeyboard.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jaminhu.airkeyboard.data.DictionaryDbHelper;
import com.example.jaminhu.airkeyboard.data.DictionaryContract.DictionaryEntry;

import java.util.Dictionary;

public class DictionaryProvider extends ContentProvider {

    public static final String LOG_TAG = DictionaryProvider.class.getSimpleName();

    private static final int INVENTORY_ID = 100;

    private static final int ITEM_ID = 101;

    private DictionaryDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        sUriMatcher.addURI(DictionaryContract.CONTENT_AUTHORITY, DictionaryContract.INVENTORY_PATH , INVENTORY_ID);
        sUriMatcher.addURI(DictionaryContract.CONTENT_AUTHORITY, DictionaryContract.INVENTORY_PATH + "/#", ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DictionaryDbHelper(getContext());
        //Why doesnt typing "this" as context ^ work here?
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match){
            case (INVENTORY_ID):
                cursor = db.query(DictionaryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case (ITEM_ID):
                selection = DictionaryEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(DictionaryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        /*
        this is important to do man... remember always! otherwise the other these:
            getContext().getContentResolver().notifyChange(uri, null);
        will not work
         */
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
/*
still not sure what this does exactly... and why necessary... yet...
 */
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY_ID:
                return DictionaryEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return DictionaryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //wait so this-------------^ is the uri of the table? how is this different from InventoryEntry.CONTENT_URI;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY_ID:
                return insertItem(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertItem(Uri uri, ContentValues values){

        String word = values.getAsString(DictionaryEntry.WORD_COLUMN);
        if (word == null) {
            throw new IllegalArgumentException("Word requires a word");
        }

        String finseq = values.getAsString(DictionaryEntry.FINSEQ_COLUMN);
        if (finseq == null) {
            throw new IllegalArgumentException("Word requires a valid finseq");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        long id = db.insert(DictionaryEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY_ID:
                rowsDeleted = db.delete(DictionaryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                rowsDeleted = db.delete(DictionaryEntry.TABLE_NAME, DictionaryEntry._ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {

            case ITEM_ID:
                selection = DictionaryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(DictionaryEntry.FINSEQ_COLUMN)) {
            String finseq = values.getAsString(DictionaryEntry.FINSEQ_COLUMN);
            if (finseq == null) {
                throw new IllegalArgumentException("Word requires valid finseq");
            }
        }

        if(values.size() == 0){
            return 0;
        } else {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            int numOfRows = db.update(DictionaryEntry.TABLE_NAME, values, selection, selectionArgs);

            if (numOfRows !=0){
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return numOfRows;
        }

    }
}
