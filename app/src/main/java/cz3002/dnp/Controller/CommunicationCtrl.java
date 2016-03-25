package cz3002.dnp.Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz3002.dnp.Constants;
import cz3002.dnp.Entity.Appointment;
import cz3002.dnp.Entity.Communication;
import cz3002.dnp.Entity.User;

/**
 * Created by hizac on 21/3/2016.
 */
public class CommunicationCtrl {
    private static CommunicationCtrl instance;
    public static CommunicationCtrl getInstance() {
        if (instance == null) {
            instance = new CommunicationCtrl();
        }
        return instance;
    }

    // Constructor
    private CommunicationCtrl(){
        chats = new ArrayList<>();
    }

    private ArrayList<Communication> chats;

    public ArrayList<Communication> getTreatments() {
        return chats;
    }

    public Communication createCommunication(int id, Date date, User doctor, User patient, String message) {
        Communication chat = new Communication();
        chat.setId(id);
        chat.setDate(date);
        chat.setDoctor(doctor);
        chat.setPatient(patient);
        chat.setMessage(message);
        chats.add(chat);
        return chat;
    }

    // Get all chats from server
    public void retrieveTreatments(){
        try {
            if (UserCtrl.getInstance().currentUser.getId() < 0) { return; } // If user has not logged in
            String query;
            if (UserCtrl.getInstance().currentUser.isDoctor()) {
                query = String.format("select * from `communication` where doctorID=%d", UserCtrl.getInstance().currentUser.getId());
            } else {
                query = String.format("select * from `communication` where patientID=%d", UserCtrl.getInstance().currentUser.getId());
            }
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
                String dateString = queryResultObj.getString("date");
                Date date;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                date = format.parse(dateString);
                int doctorId = Integer.parseInt(queryResultObj.getString("doctorID"));
                int patientId = Integer.parseInt(queryResultObj.getString("patientID"));
                User doctor = UserCtrl.getInstance().getUser(doctorId);
                User patient = UserCtrl.getInstance().getUser(patientId);
                String messageString = queryResultObj.getString("message");

                // Set chat info
                createCommunication(id, date, doctor, patient, messageString);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Retrieve an individual chat and put it to internal database
    public Communication retrieveACommunication(Date date, User doctor, User patient) {
        Communication result = new Communication();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(date);
            String query = String.format("select * from `communication` where doctorID=%d and patientID=%d and date='%s'", doctor.getId(), patient.getId(), dateString);
            Document document = Jsoup.connect(Constants.SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // If try to retrieve info fails
                return null;
            }
            // Otherwise if success, continue

            // Process JSON format
            JSONArray queryResultArr = new JSONArray(queryJson);
            JSONObject queryResultObj = queryResultArr.getJSONObject(0);

            // Read information
            int id = Integer.parseInt(queryResultObj.getString("id"));
            String messageString = queryResultObj.getString("message");

            result = createCommunication(id, date, doctor, patient, messageString);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
    public ArrayList<Communication> getChats() {
        return chats;
    }
}
