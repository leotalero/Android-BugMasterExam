package com.google.developer.bugmaster;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.developer.bugmaster.reminders.AlarmReceiver;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        public Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        this.context=getActivity();
       // PreferenceManager.getDefaultSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        String keyReminder = context.getString(R.string.pref_key_reminder);
        //if(sharedPreferences.getBoolean(keyReminder,false)==true){


        AlarmReceiver receiver= new AlarmReceiver();
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_BOOT_COMPLETED);
        receiver.onReceive(context,intent);
        //}else{

        //}


    }




}
