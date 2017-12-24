package com.google.developer.bugmaster.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.data.BugsContract.BugsEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Database helper class to facilitate creating and updating
 * the database from the chosen schema.
 */
public class BugsDbHelper extends SQLiteOpenHelper {
    private static final String TAG = BugsDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "insects.db";
    private static final int DATABASE_VERSION = 4;

    //Used to read data from res/ and assets/
    private Resources mResources;
    private int rowsInserted=0;

    public BugsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mResources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO: Create and fill the database
        // Create a table to hold waitlist data
        final String SQL_CREATE_TABLE = "CREATE TABLE " + BugsEntry.TABLE_NAME + " (" +
                BugsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BugsEntry.COLUMN_TIMESTAMP+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                BugsEntry.COLUMN_friendlyName + " TEXT NOT NULL, " +
                BugsEntry.COLUMN_scientificName + " TEXT NOT NULL, " +
                BugsEntry.COLUMN_classification+ " TEXT NOT NULL, " +
                BugsEntry.COLUMN_imageAsset+ " TEXT NOT NULL , " +
                BugsEntry.COLUMN_dangerLevel+ " DOUBLE " +
                "); ";

        db.execSQL(SQL_CREATE_TABLE);

        try {
            readInsectsFromResources(db);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Handle database version upgrades
        db.execSQL("DROP TABLE IF EXISTS " + BugsEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Streams the JSON data from insect.json, parses it, and inserts it into the
     * provided {@link SQLiteDatabase}.
     *
     * @param db Database where objects should be inserted.
     * @throws IOException
     * @throws JSONException
     */
    private void readInsectsFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.insects);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();
        //TODO: Parse JSON data and insert into the provided database instance
        JSONObject json = new JSONObject(rawJson);
        JSONArray array = json.getJSONArray("insects");

        ArrayList<ContentValues> values=new ArrayList<ContentValues>();
        //String name, String scientificName, String classification, String imageAsset, int dangerLevel

        for (int i = 0; i < array.length(); i++) {
            JSONObject a=new JSONObject(array.get(i).toString());
            Insect mInsect = new Insect(a.getString(BugsEntry.COLUMN_friendlyName),
                    a.getString(BugsEntry.COLUMN_scientificName),
                    a.getString(BugsEntry.COLUMN_classification),
                    a.getString(BugsEntry.COLUMN_imageAsset),
                    a.getInt(BugsEntry.COLUMN_dangerLevel));
            ContentValues contentvalues=new ContentValues();
            contentvalues.put(BugsEntry.COLUMN_friendlyName,mInsect.name);
            contentvalues.put(BugsEntry.COLUMN_scientificName,mInsect.scientificName);
            contentvalues.put(BugsEntry.COLUMN_classification,mInsect.classification);
            contentvalues.put(BugsEntry.COLUMN_imageAsset,mInsect.imageAsset);
            contentvalues.put(BugsEntry.COLUMN_dangerLevel,mInsect.dangerLevel);

            values.add(contentvalues);

        }



        try {
            for (ContentValues value : values) {

                long _id = db.insert(BugsEntry.TABLE_NAME, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }
            }
           // db.setTransactionSuccessful();
        } finally {
           // db.endTransaction();
            Log.i(TAG,"rows inserted :"+rowsInserted);
        }


    }
}
