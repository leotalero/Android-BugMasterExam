package com.google.developer.bugmaster.reminders;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.developer.bugmaster.QuizActivity;
import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.data.Insect;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.google.developer.bugmaster.reminders.AlarmReceiver.scheduleAlarm;

public class ReminderService extends IntentService {

    private static final String TAG = ReminderService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;
    public static final String EXTRA_INSECTS = "insectList";
    public static final String EXTRA_ANSWER = "selectedInsect";
    private ArrayList<Insect> randomList;

    public ReminderService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Quiz reminder event triggered");

        //Present a notification to the user
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Create action intent
        Intent action = new Intent(this, QuizActivity.class);
        SharedPreferences sharedPref = this.getSharedPreferences("preferences",Context.MODE_PRIVATE);
        String json = sharedPref.getString("list", null);
        Type type = new TypeToken<ArrayList<Insect>>(){}.getType();
        randomList= new Gson().fromJson(json, type);

        action.putParcelableArrayListExtra(EXTRA_INSECTS,randomList );
        action.putExtra(EXTRA_ANSWER, randomList.get(new Random().nextInt(randomList.size())));
        //TODO: Add data elements to quiz launch

        PendingIntent operation =
                PendingIntent.getActivity(this, 0, action, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification note = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_bug_empty)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();

        manager.notify(NOTIFICATION_ID, note);

        scheduleAlarm(this);

    }
}
