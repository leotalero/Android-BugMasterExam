package com.google.developer.bugmaster.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

public final class Insect implements Parcelable {
    private static final String TAG = Insect.class.getSimpleName();

    //Common name
    public final String name;
    //Latin scientific name
    public final String scientificName;
    //Classification order
    public final String classification;
    //Path to image resource
    public final String imageAsset;
    //1-10 scale danger to humans
    public final int dangerLevel;

    /**
     * Create a new Insect from discrete values
     */
    public Insect(String name, String scientificName, String classification, String imageAsset, int dangerLevel) {
        this.name = name;
        this.scientificName = scientificName;
        this.classification = classification;
        this.imageAsset = imageAsset;
        this.dangerLevel = dangerLevel;
    }

    /**
     * Create a new Insect from a database Cursor
     */
    public Insect(Cursor cursor) {
        //TODO: Create a new insect from cursor

        this.name = cursor.getString(cursor.getColumnIndex(BugsContract.BugsEntry.COLUMN_friendlyName));
        this.scientificName = cursor.getString(cursor.getColumnIndex(BugsContract.BugsEntry.COLUMN_scientificName));
        this.classification = cursor.getString(cursor.getColumnIndex(BugsContract.BugsEntry.COLUMN_classification));
        this.imageAsset = cursor.getString(cursor.getColumnIndex(BugsContract.BugsEntry.COLUMN_imageAsset));
        this.dangerLevel = cursor.getInt(cursor.getColumnIndex(BugsContract.BugsEntry.COLUMN_dangerLevel));





    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.scientificName);
        dest.writeString(this.classification);
        dest.writeString(this.imageAsset);
        dest.writeInt(this.dangerLevel);
    }

    protected Insect(Parcel in) {
        this.name = in.readString();
        this.scientificName = in.readString();
        this.classification = in.readString();
        this.imageAsset = in.readString();
        this.dangerLevel = in.readInt();
    }

    public static final Creator<Insect> CREATOR = new Creator<Insect>() {
        @Override
        public Insect createFromParcel(Parcel source) {
            return new Insect(source);
        }

        @Override
        public Insect[] newArray(int size) {
            return new Insect[size];
        }
    };
}
