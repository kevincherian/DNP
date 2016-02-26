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
import cz3002.dnp.Entity.User;

/**
 * Created by hizac on 26/2/2016.
 */
public class AppointmentCtrl implements Constants {
    
    private static AppointmentCtrl instance;
    public static AppointmentCtrl getInstance() {
        if (instance == null) {
            instance = new AppointmentCtrl();
        }
        return instance;
    }

    // Constructor
    private AppointmentCtrl(){
        appointments = new ArrayList<>();
        retrieveAppointments();
    }

    private ArrayList<Appointment> appointments;

    // Get all appointments from server
    private void retrieveAppointments(){
        try {
            if (UserCtrl.getInstance().currentUser.getId() < 0) { return; } // If user has not logged in
            String query;
            if (UserCtrl.getInstance().currentUser.isDoctor()) {
                query = String.format("select * from `appointment` where doctorID=%d", UserCtrl.getInstance().currentUser.getId());
            } else {
                query = String.format("select * from `appointment` where patientID=%d", UserCtrl.getInstance().currentUser.getId());
            }
            Document document = Jsoup.connect(SERVER + query).get();
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
                int doctorId = Integer.parseInt(queryResultObj.getString("doctorID"));
                int patientId = Integer.parseInt(queryResultObj.getString("patientID"));
                User doctor = UserCtrl.getInstance().getUser(doctorId);
                User patient = UserCtrl.getInstance().getUser(patientId);
                String infoString = queryResultObj.getString("info");
                String statusString = queryResultObj.getString("status");

                // Set appointment info
                createAppointment(id, time, doctor, patient, infoString, statusString);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public Appointment createAppointment(int id, Date time, User doctor, User patient, String info, String status) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setTime(time);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setInfo(info);
        appointment.setStatus(status);
        appointments.add(appointment);
        return appointment;
    }

    // Query an appointment using ID
    public Appointment getAppointmentInfo(int id) {
        for (Appointment appointment : appointments) {
            if (appointment.getId() == id) {
                return appointment;
            }
        }
        return null;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }
}
