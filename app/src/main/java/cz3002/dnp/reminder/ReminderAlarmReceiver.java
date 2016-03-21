package cz3002.dnp.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ReminderAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "cz3002.dnp.reminder.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ReminderAlarmReceiver", "onReceive");
        Intent i = new Intent(context, ReminderService.class);

        context.startService(i);
    }
}
