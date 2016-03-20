package cz3002.dnp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;

import java.util.Calendar;

import cz3002.dnp.reminder.ReminderAlarmReceiver;


public class MainActivity extends AppCompatActivity {
    private static MainActivity activity;
    public static MainActivity getActivity(){
        return activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scheduleAlarm();

        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new LoginFragment()).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        MainActivity.getActivity().getSupportFragmentManager().popBackStack();
    }

    // Setup a recurring alarm at 6am everyday
    public void scheduleAlarm() {
        Log.d("scheduleAlarm", "schedule Alarm");
        // Construct an intent that will execute the ReminderAlarmReceiver
        Intent intent = new Intent(getApplicationContext(), ReminderAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this,
                ReminderAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 6am
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20); // For 6AM
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0); // to be more precise

        // create an alarm that will first go of at the set calendar time
        // and keep repeating after 1 day
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);
    }

}
