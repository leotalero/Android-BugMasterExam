package com.google.developer.bugmaster;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.developer.bugmaster.data.BugsContract;
import com.google.developer.bugmaster.data.Insect;
import com.google.developer.bugmaster.data.InsectRecyclerAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class InsectDetailsActivity extends AppCompatActivity {

    private Insect insect;
    private TextView nameInsect;
    private TextView secondInsect;
    private TextView categoryInsect;
    private Integer dangerInsect;
    private ImageView img_insect;
    private AssetManager assetManager;
    private RatingBar danger_insect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Implement layout and display insect details

        setContentView(R.layout.activity_detail_activity);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Intent intentThatStartedThisActivity = getIntent();
        assetManager = this.getAssets();
        nameInsect=(TextView)findViewById(R.id.name_insect);
        secondInsect=(TextView)findViewById(R.id.secondName_insect);
        categoryInsect=(TextView)findViewById(R.id.class_insect);
        img_insect=(ImageView)findViewById(R.id.img_insect);
        danger_insect=(RatingBar)findViewById(R.id.danger_insect);
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("insect")) {
                insect = intentThatStartedThisActivity.getExtras().getParcelable("insect");
                fillData(insect);
            }
        }
    }

    private void fillData(Insect insect) {
        nameInsect.setText(insect.name);
        secondInsect.setText(insect.scientificName);
        categoryInsect.setText(getResources().getString(R.string.caterory_text)+" "+insect.classification);

        InputStream is = null;
        try {
            is = assetManager.open(insect.imageAsset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        img_insect.setImageBitmap(bitmap);
        danger_insect.setRating(insect.dangerLevel);
        img_insect.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }
}
