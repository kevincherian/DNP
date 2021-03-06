package cz3002.dnp.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Random;

import cz3002.dnp.R;
import cz3002.dnp.Reminder;
import database.Appointment;
import database.DatabaseHandler;
import database.Treatment;

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

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());


        //generate notification
        StringBuilder sb = new StringBuilder();

        List<Appointment> aps = db.getAllAppointmentsToday();
        sb.append("Appointments: ");
        for (Appointment ap : aps){
            sb.append(System.getProperty("line.separator"));
            sb.append(ap.getDoctor() + " and " + ap.getPatient() + " has an appointment at " + ap.getTime()+ ".");
        }

        sb.append(System.getProperty("line.separator"));

        List<Treatment> trs = db.getAllTreatmentsToday();
        sb.append("Treatments: ");
        for (Treatment tr : trs) {
            sb.append(System.getProperty("line.separator"));
            sb.append(tr.getDoctor() + " and " + tr.getPatient() + " has treatment " + tr.getText() + " today.");
        }

        Intent notifIntent = new Intent(getApplicationContext(), Reminder.class);
        String notificationText = sb.toString();

        // set the fragment to the desired ReminderFragment
        notifIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putString("fragment", "ReminderFragment");
        bundle.putString("reminder", notificationText);

        notifIntent.putExtras(bundle);
        PendingIntent notifPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        new Random().nextInt(),
                        notifIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        notif = new Notification.Builder(getApplicationContext())
                .setContentTitle("Today's reminder")
                .setContentText(notificationText)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(notifPendingIntent)
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



