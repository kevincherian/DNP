package cz3002.dnp.reminder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by shadofren on 3/20/16.
 */
public class ReminderService extends Service{
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        //send notification
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}


