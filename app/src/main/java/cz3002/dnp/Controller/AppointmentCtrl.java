package cz3002.dnp.Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz3002.dnp.Constants;
import cz3002.dnp.Entity.Appointment;
import cz3002.dnp.Entity.User;

/**
 * Created by hizac on 26/2/2016.
 */
public class AppointmentCtrl implements Constants {
    // Quickly set info for an appointment object
    public Appointment setAppointmentInfo(Appointment appointment, int id, Date time, User doctor, User patient, String info, String status) {
        appointment.setId(id);
        appointment.setTime(time);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setInfo(info);
        appointment.setStatus(status);

        return appointment;
    }

    // Query information of an appointment from server using ID
    public Appointment getAppointmentInfo(Appointment appointment, int id) {
        try {
            String query = String.format("select * from `appointment` where id=%d", id);
            Document document = Jsoup.connect(SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // If try to retrieve info fails
                return appointment;
            }
            // Otherwise if success, continue

            // Process JSON format
            JSONArray queryResultArr = new JSONArray(queryJson);
            JSONObject queryResultObj = queryResultArr.getJSONObject(0);

            // Read information
            String timeString = queryResultObj.getString("time");
            Date time = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            time = format.parse(timeString);
            int doctorId = Integer.parseInt(queryResultObj.getString("doctorID"));
            int patientId = Integer.parseInt(queryResultObj.getString("patientID"));
            UserCtrl userCtrl = new UserCtrl();
            User doctor = new User();
            userCtrl.getUserInfo(doctor, doctorId);
            User patient = new User();
            userCtrl.getUserInfo(patient, patientId);
            String infoString = queryResultObj.getString("info");
            String statusString = queryResultObj.getString("status");

            // Set appointment info
            setAppointmentInfo(appointment, id, time, doctor, patient, infoString, statusString);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return appointment;
    }
}
