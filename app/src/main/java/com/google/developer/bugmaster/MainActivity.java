package com.google.developer.bugmaster;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amitshekhar.DebugDB;
import com.google.developer.bugmaster.data.DatabaseManager;
import com.google.developer.bugmaster.data.Insect;
import com.google.developer.bugmaster.data.InsectRecyclerAdapter;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    public static String TAG="MainActivity";
    ArrayList<Insect> insects= new ArrayList<Insect>();
    DatabaseManager databaseManager;
    private static final String NAME_SORT = "friendlyName";
    private static final String DANGER_SORT = "dangerLevel";
    public static final String EXTRA_INSECTS = "insectList";
    public static final String EXTRA_ANSWER = "selectedInsect";
    private String sortOrder;
    private InsectRecyclerAdapter adapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Insect> randomList= new ArrayList<Insect>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DebugDB.getAddressLog();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        //////create database and fill data
        databaseManager=DatabaseManager.getInstance(this);

        if(savedInstanceState == null || !savedInstanceState.containsKey("sortOrder") ) {
            sortOrder=NAME_SORT;

        }
        else {
            sortOrder = savedInstanceState.getString("sortOrder");
        }
        SharedPreferences sharedPref = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        sortOrder = sharedPref.getString("sortOrder", null);


        if(sortOrder==null)
            sortOrder=NAME_SORT;
        getSupportLoaderManager().initLoader(1, null, this);

        //cursor=databaseManager.queryAllInsects(NAME_SORT);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter= new InsectRecyclerAdapter(null,this);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                //TODO: Implement the sort action
                if(sortOrder==NAME_SORT){
                    sortOrder=DANGER_SORT;

                }else{
                    sortOrder=NAME_SORT;
                }
                //Cursor cursor=databaseManager.queryAllInsects(sortOrder);
                //adapter.notifyDataSetChanged();
                getSupportLoaderManager().initLoader(1, null, this).forceLoad();

                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Click events in Floating Action Button */
    @Override
    public void onClick(View v) {
        //TODO: Launch the quiz activity
        Intent intentToStartQuizzActivity = new Intent(this, QuizActivity.class);
        intentToStartQuizzActivity.putParcelableArrayListExtra(EXTRA_INSECTS, randomList);
        intentToStartQuizzActivity.putExtra(EXTRA_ANSWER, randomList.get(new Random().nextInt(randomList.size())));

        this.startActivity(intentToStartQuizzActivity);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new android.support.v4.content.AsyncTaskLoader<Cursor>(this) {

            public Cursor mDataCache = null;
            @Override
            protected void onStartLoading() {
                if (mDataCache != null) {
                    deliverResult(mDataCache);
                } else {
                    //mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }


            }

            @Override
            public Cursor loadInBackground() {
                Cursor cursor = databaseManager.queryAllInsects(sortOrder);
                return cursor;
            }

            @Override
            public void deliverResult(Cursor data) {
                mDataCache = data;
                super.deliverResult(data);
            }

        };


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data!=null){
            adapter.swapCursor(data);
            randomInsect(data);
            SharedPreferences sharedPref = this.getSharedPreferences("preferences",this.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            String json = randomList == null ? null : new Gson().toJson(randomList);

            editor.putString("list", json).apply();
            editor.putString("sortOrder", sortOrder).apply();
            editor.commit();
        }



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("sortOrder", sortOrder);
        super.onSaveInstanceState(outState);

    }


 public List<Insect> randomInsect(Cursor cursor){
     ArrayList<Insect> listInsects= new ArrayList<Insect>();
     randomList.clear();

     while (cursor.moveToNext()) {
         Insect insect=new Insect(cursor);
         listInsects.add(insect);
     }

     randomList= (ArrayList<Insect>) shuffleList(listInsects);


     return randomList;
 }

    public static List<Insect>  shuffleList(ArrayList<Insect> a) {
        int n = a.size();
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }
        return a;
    }

    private static void swap(ArrayList<Insect> a, int i, int change) {
        Insect helper = a.get(i);
        a.set(i, a.get(change));
        a.set(change, helper);
    }



}
