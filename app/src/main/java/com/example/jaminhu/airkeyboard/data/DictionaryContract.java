package com.example.jaminhu.airkeyboard.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DictionaryContract {

    public static final String CONTENT_AUTHORITY = "com.example.jaminhu.airkeyboard";
    //So this needs to match the android manifests provider authority? What does this really mean?d
    //And why don't I, for example, need to put a ".data" after this

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String INVENTORY_PATH = "words";

    public static class DictionaryEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + INVENTORY_PATH;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + INVENTORY_PATH;


        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, INVENTORY_PATH);
        public static final String TABLE_NAME = "words";


        public static final String _ID = BaseColumns._ID;
        public static final String WORD_COLUMN = "name";
        public static final String FINSEQ_COLUMN = "finseq";
        public static final String SIBLING_COLUMN = "siblings";
    }
}
