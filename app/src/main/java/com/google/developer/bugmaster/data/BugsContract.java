package com.google.developer.bugmaster.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ltaleron on 12/23/17.
 */

public class BugsContract {

    public static final String CONTENT_AUTHORITY = "com.google.developer.bugmaster";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class BugsEntry implements BaseColumns {
        public static final String TABLE_NAME = "bugs";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TIMESTAMP="timestamp";
        public static final String COLUMN_friendlyName = "friendlyName";
        public static final String COLUMN_scientificName= "scientificName";
        public static final String COLUMN_classification= "classification";
        public static final String COLUMN_imageAsset = "imageAsset";
        public static final String COLUMN_dangerLevel = "dangerLevel";



        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // for building URIs on insertion
        public static Uri buildUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}
