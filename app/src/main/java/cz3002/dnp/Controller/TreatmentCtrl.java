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
import cz3002.dnp.Entity.Treatment;
import cz3002.dnp.Entity.User;

/**
 * Created by hizac on 21/3/2016.
 */
public class TreatmentCtrl {
    private static TreatmentCtrl instance;
    public static TreatmentCtrl getInstance() {
        if (instance == null) {
            instance = new TreatmentCtrl();
        }
        return instance;
    }

    // Constructor
    private TreatmentCtrl(){
        treatments = new ArrayList<>();
    }

    private ArrayList<Treatment> treatments;

    public ArrayList<Treatment> getTreatments() {
        return treatments;
    }

    public Treatment createTreatment(int id, Date startdate, Date enddate, User doctor, User patient, String info) {
        Treatment treatment = new Treatment();
        treatment.setId(id);
        treatment.setStartdate(startdate);
        treatment.setEnddate(enddate);
        treatment.setDoctor(doctor);
        treatment.setPatient(patient);
        treatment.setInfo(info);
        treatments.add(treatment);
        return treatment;
    }

    // Get all treatments from server
    public void retrieveTreatments(){
        try {
            if (UserCtrl.getInstance().currentUser.getId() < 0) { return; } // If user has not logged in
            String query;
            if (UserCtrl.getInstance().currentUser.isDoctor()) {
                query = String.format("select * from `treatment` where doctorID=%d", UserCtrl.getInstance().currentUser.getId());
            } else {
                query = String.format("select * from `treatment` where patientID=%d", UserCtrl.getInstance().currentUser.getId());
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
                String startdateString = queryResultObj.getString("startdate");
                String enddateString = queryResultObj.getString("enddate");
                Date startdate, enddate;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                startdate = format.parse(startdateString);
                enddate = format.parse(enddateString);
                int doctorId = Integer.parseInt(queryResultObj.getString("doctorID"));
                int patientId = Integer.parseInt(queryResultObj.getString("patientID"));
                User doctor = UserCtrl.getInstance().getUser(doctorId);
                User patient = UserCtrl.getInstance().getUser(patientId);
                String infoString = queryResultObj.getString("text");

                // Set treatment info
                createTreatment(id, startdate, enddate, doctor, patient, infoString);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Retrieve an individual treatment and put it to internal database
    public Treatment retrieveAnTreatment(Date startdate, Date enddate, User doctor, User patient) {
        Treatment result = new Treatment();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String startdateString = format.format(startdate);
            String enddateString = format.format(enddate);
            String query = String.format("select * from `treatment` where doctorID=%d and patientID=%d and startdate='%s' and enddate='%s'", doctor.getId(), patient.getId(), startdateString, enddateString);
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
            String infoString = queryResultObj.getString("text");

            result = createTreatment(id, startdate, enddate, doctor, patient, infoString);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
