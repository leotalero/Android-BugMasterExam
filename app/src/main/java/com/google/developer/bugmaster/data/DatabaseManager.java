package com.google.developer.bugmaster.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Singleton that controls access to the SQLiteDatabase instance
 * for this application.
 */
public class DatabaseManager {
    private static DatabaseManager sInstance;
    private static final String NAME_SORT = "friendlyName";
    private static final String DANGER_SORT = "dangerLevel";
    private static  String[] projection= {
            BugsContract.BugsEntry.COLUMN_ID,
            BugsContract.BugsEntry.COLUMN_friendlyName,
            BugsContract.BugsEntry.COLUMN_scientificName,
            BugsContract.BugsEntry.COLUMN_classification,
            BugsContract.BugsEntry.COLUMN_imageAsset,
            BugsContract.BugsEntry.COLUMN_dangerLevel
    };


    public static synchronized DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseManager(context.getApplicationContext());
        }

        return sInstance;
    }

    private BugsDbHelper mBugsDbHelper;

    private DatabaseManager(Context context) {
        mBugsDbHelper = new BugsDbHelper(context);
    }

    /**
     * Return a {@link Cursor} that contains every insect in the database.
     *
     * @param sortOrder Optional sort order string for the query, can be null
     * @return {@link Cursor} containing all insect results.
     */
    public Cursor queryAllInsects(String sortOrder) {
        //TODO: Implement the query
        Cursor retCursor = null;

        if(mBugsDbHelper!=null){
            SQLiteDatabase db = mBugsDbHelper.getReadableDatabase();
            switch (sortOrder){

                case NAME_SORT:
                    retCursor = db.query(
                            BugsContract.BugsEntry.TABLE_NAME,
                            projection,
                            null,
                            null,
                            null,
                            null,
                            sortOrder +" ASC");
                    break;
                case DANGER_SORT:
                    retCursor = db.query(
                            BugsContract.BugsEntry.TABLE_NAME,
                            projection,
                            null,
                            null,
                            null,
                            null,
                            sortOrder +" DESC");
                    break;
            }





            return retCursor;
        }else{
            return null;
        }


    }

    /**
     * Return a {@link Cursor} that contains a single insect for the given unique id.
     *
     * @param id Unique identifier for the insect record.
     * @return {@link Cursor} containing the insect result.
     */
    public Cursor queryInsectsById(int id) {
        //TODO: Implement the query
        Cursor retCursor;


        SQLiteDatabase db = mBugsDbHelper.getReadableDatabase();

        retCursor = mBugsDbHelper.getReadableDatabase().query(
                BugsContract.BugsEntry.TABLE_NAME,
                projection,
                BugsContract.BugsEntry._ID+ " = ?",
                new String[] {String.valueOf((id))},
                null,
                null,
                null);


        return retCursor;
    }



}
