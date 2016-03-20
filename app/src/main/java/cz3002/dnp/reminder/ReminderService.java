package cz3002.dnp.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cz3002.dnp.R;

public class ReminderService extends IntentService {
    private static final int REMINDER_NOTIFICATION_ID = 777;
    NotificationManager notificationMgr;
    Notification notif;

    public ReminderService() {
        super("Reminder Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("Reminder Service", "Service running");

        //empty pending intent
        PendingIntent reminderPI =
                PendingIntent.getActivity(
                        getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);


        //generate notification
        StringBuilder sb = new StringBuilder();
        sb.append("Appointments: ");
        sb.append(System.getProperty("line.separator"));
        sb.append("\tFirst appointment");
        sb.append(System.getProperty("line.separator"));
        sb.append("\tSecond appointment");

        sb.append(System.getProperty("line.separator"));

        sb.append("Treatments: ");
        sb.append(System.getProperty("line.separator"));
        sb.append("\tFirst treatment");
        sb.append(System.getProperty("line.separator"));
        sb.append("\tSecond treatment");

        String notificationText = sb.toString();
        notif = new Notification.Builder(getApplicationContext())
                .setContentTitle("Today's reminder")
                .setContentText(notificationText)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(reminderPI)
                .setStyle(new Notification.BigTextStyle().bigText(notificationText))
                .build();

        notificationMgr.notify(REMINDER_NOTIFICATION_ID, notif);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationMgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }

}



