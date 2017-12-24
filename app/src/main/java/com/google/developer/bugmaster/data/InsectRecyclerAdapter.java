package com.google.developer.bugmaster.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.developer.bugmaster.InsectDetailsActivity;
import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.views.DangerLevelView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter extended with project-specific required methods.
 */

public class InsectRecyclerAdapter extends
        RecyclerView.Adapter<InsectRecyclerAdapter.InsectHolder> {


    private final Resources mResources;
    private final AssetManager assetManager;
    private Cursor mCursor;
    private Context context;
    //private  List<Insect> mValues=new ArrayList<Insect>();
    public InsectRecyclerAdapter(Cursor cursor,Context context) {
        this.mCursor=cursor;
        this.context=context;
        this.mResources = context.getResources();
        this.assetManager = context.getAssets();

    }

    /* ViewHolder for each insect item */
    public class InsectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView viewName,viewId;
        public TextView viewSecondName;
        public TextView viewDanger;
        public DangerLevelView viewFoto;


        public InsectHolder(View itemView) {
            super(itemView);
            viewName = (TextView) itemView.findViewById(R.id.name_insect);
            viewSecondName = (TextView) itemView.findViewById(R.id.secondName_insect);
            viewDanger = (TextView) itemView.findViewById(R.id.danger_insect);
            viewFoto = (DangerLevelView) itemView.findViewById(R.id.level_insect);
            viewId=(TextView) itemView.findViewById(R.id.id_insect);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Log.i("click insect","click on insect id:"+getItem(getAdapterPosition()).name.toString());
            Intent intentToStartDetailActivity = new Intent(context, InsectDetailsActivity.class);
            Insect insect = getItem(getAdapterPosition());
            intentToStartDetailActivity.putExtra("insect" , getItem(getAdapterPosition()));
            context.startActivity(intentToStartDetailActivity);


        }
    }





    @Override
    public InsectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.insect_item, parent, false);
        return new InsectHolder(view);

    }

    @Override
    public void onBindViewHolder(InsectHolder holder, int position) {
        //Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.name);
        mCursor.moveToPosition(position);
        Integer id=mCursor.getInt(mCursor.getColumnIndex(BugsContract.BugsEntry.COLUMN_ID));
        Integer dangerlevel=mCursor.getInt(mCursor.getColumnIndex(BugsContract.BugsEntry.COLUMN_dangerLevel));
        holder.viewName.setText( mCursor.getString(mCursor.getColumnIndex(BugsContract.BugsEntry.COLUMN_friendlyName)));
        holder.viewSecondName.setText( mCursor.getString(mCursor.getColumnIndex(BugsContract.BugsEntry.COLUMN_scientificName)));
        holder.viewDanger.setText(dangerlevel.toString());
        holder.viewFoto.setText( mCursor.getString(mCursor.getColumnIndex(BugsContract.BugsEntry.COLUMN_dangerLevel)));
        holder.viewFoto.setStrokeWidth(0);
        String s0 = context.getResources().getString(0+R.color.colorPrimary);
        holder.viewFoto.setStrokeColor(s0);
        //holder.viewFoto.setSolidColor(context.getResources().getString(0+R.color.colorAccent));
        holder.viewFoto.setDangerLevel(dangerlevel);
        holder.viewFoto.setTextColor(context.getResources().getColor(R.color.white));
        holder.viewFoto.setPadding(10,10,10,10);
        String s=mCursor.getString(mCursor.getColumnIndex(BugsContract.BugsEntry.COLUMN_imageAsset));
        InputStream is = null;
        try {
            is = assetManager.open(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        holder.viewId.setText(id.toString());


    }

    @Override
    public int getItemCount() {
        if (mCursor != null)
            return mCursor.getCount();
        return 0;
    }

    /**
     * Return the {@link Insect} represented by this item in the adapter.
     *
     * @param position Adapter item position.
     *
     * @return A new {@link Insect} filled with this position's attributes
     *
     * @throws IllegalArgumentException if position is out of the adapter's bounds.
     */
    public Insect getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        } else if (mCursor.moveToPosition(position)) {
            return new Insect(mCursor);
        }
        return null;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

}
