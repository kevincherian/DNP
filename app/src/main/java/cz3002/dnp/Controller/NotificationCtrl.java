package cz3002.dnp.Controller;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import cz3002.dnp.Constants;
import cz3002.dnp.Entity.Notification;
import cz3002.dnp.Entity.User;

/**
 * Created by hizac on 25/3/2016.
 */
public class NotificationCtrl {
    private static NotificationCtrl instance;

    public static NotificationCtrl getInstance() {
        if (instance == null) { instance = new NotificationCtrl();}
        return instance;
    }

    // Constructor
    private NotificationCtrl(){
        notifications = new ArrayList<>();
    }

    private ArrayList<Notification> notifications;

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    // Get all notifications from server
    public void retrieveNotifications(){
        try {
            if (UserCtrl.getInstance().currentUser.getId() < 0) { return; } // If user has not logged in
            String query = String.format("select * from `notification` where recipientId=%d", UserCtrl.getInstance().currentUser.getId());
            Document document = Jsoup.connect(Constants.SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // If try to retrieve info fails
                return;
            }
            // Otherwise if success, continue

            // Process JSON format
            JSONArray queryResultArr = new JSONArray(queryJson);

            for (int i = 0; i < queryResultArr.length(); i++) {
                JSONObject queryResultObj = queryResultArr.getJSONObject(i);

                // Read information
                int id = Integer.parseInt(queryResultObj.getString("id"));
                String timeString = queryResultObj.getString("time");
                Date time;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                time = format.parse(timeString);
                int senderId = Integer.parseInt(queryResultObj.getString("senderId"));
                int recipientId = Integer.parseInt(queryResultObj.getString("recipientId"));
                User sender = UserCtrl.getInstance().getUser(senderId);
                User recipient = UserCtrl.getInstance().getUser(recipientId);
                String contentString = queryResultObj.getString("content");
                int type = Integer.parseInt(queryResultObj.getString("type"));

                // Set notification info
                createNotification(id, time, sender, recipient, contentString, type);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Notification createNotification(int id, Date time, User sender, User recipient, String content, int type) {
        Notification notification = new Notification();
        notification.setId(id);
        notification.setTime(time);
        notification.setSender(sender);
        notification.setRecipient(recipient);
        notification.setContent(content);
        notification.setType(type);
        // To add the new notification to the beginning of the arraylist
        notifications.add(0, notification);

        return notification;
    }

    public boolean pushANotification(Notification notification) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String datetimeString = format.format(notification.getTime());
            String query = String.format("insert into `notification` (time, senderId, recipientId, content, type) " +
                    "values ('%s', '%d', '%d', '%s', '%d')", datetimeString, notification.getSender().getId(), notification.getRecipient().getId(), notification.getContent(), notification.getType());
            Document document = Jsoup.connect(Constants.SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // Error happens
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
