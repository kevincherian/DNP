package cz3002.dnp.reminder;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class ReminderService extends IntentService {
    public ReminderService() {
        super("Reminder Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Send the notification here
        Log.i("Reminder Service", "Service running");
    }
}


