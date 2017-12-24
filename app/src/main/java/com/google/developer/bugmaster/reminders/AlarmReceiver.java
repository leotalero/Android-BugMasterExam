package com.google.developer.bugmaster.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.developer.bugmaster.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();




    @Override
    public void onReceive(Context context, Intent intent) {
        //Schedule alarm on BOOT_COMPLETED
        String a = Intent.ACTION_BOOT_COMPLETED;
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            scheduleAlarm(context);
        }
    }

    /* Schedule the alarm based on user preferences */
    public static void scheduleAlarm(Context context) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        String keyReminder = context.getString(R.string.pref_key_reminder);
        String keyAlarm = context.getString(R.string.pref_key_alarm);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean enabled = preferences.getBoolean(keyReminder, false);

        //Intent to trigger
        Intent intent = new Intent(context, ReminderService.class);
        PendingIntent operation = PendingIntent
                .getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (enabled) {
            //Gather the time preference
            Calendar startTime = Calendar.getInstance();
            startTime.setTimeInMillis(new Date().getTime());  //here your time in miliseconds
            String alarmPref = preferences.getString(keyAlarm, "12:00");
            //SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String horas = alarmPref.substring(0, 2);

            startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(horas));
            startTime.set(Calendar.MINUTE, Integer.valueOf(0));
            startTime.set(Calendar.SECOND, 0);
            //Date hora = format.parse(alarmPref);
            //hora.setYear(startTime.YEAR);
            //hora.setMonth(startTime.MONTH);
            //hora.setDate(startTime.DATE);
            Calendar cl = Calendar.getInstance();

            Log.i(TAG, String.valueOf(startTime.getTime()));

            //Start at the preferred time
            //If that time has passed today, set for tomorrow
            if (Calendar.getInstance().after(startTime)) {
                startTime.add(Calendar.DATE, 1);
            }

            Log.d(TAG, "Scheduling quiz reminder alarm");
            manager.setExact(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), operation);
        } else {
            Log.d(TAG, "Disabling quiz reminder alarm");
            manager.cancel(operation);
        }
    }

}
