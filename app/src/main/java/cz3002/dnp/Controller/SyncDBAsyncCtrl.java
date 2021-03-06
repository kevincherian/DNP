package cz3002.dnp.Controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import cz3002.dnp.Constants;
import cz3002.dnp.MainActivity;
import cz3002.dnp.reminder.ReminderAlarmReceiver;
import database.Appointment;
import database.DatabaseHandler;
import database.Treatment;

/**
 * Created by hizac on 28/2/2016.
 */
public class SyncDBAsyncCtrl extends AsyncTask<String, Void, String> {

    private Context context;
    public SyncDBAsyncCtrl (Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        syncDB(params[0]);
        return "Done syncing the local database";
    }

    @Override
    protected void onPostExecute(String result) {
        scheduleAlarm();
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }

    private void syncDB(String user_id) {
        DatabaseHandler db = new DatabaseHandler(context);
        db.deleteAllAppointments();
        db.deleteAllTreatments();

        //download the appointments belong to the current user
        try {
            String query = String.format("select * from `appointment` where doctorID=%s or patientID=%s", user_id, user_id);
            Document document = Jsoup.connect(Constants.SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) {
                Log.d("After toast", "After toast");
            } else {
                JSONArray queryResultArr = new JSONArray(queryJson);
                Log.d("Insert: ", "Inserting ..");

                for (int i = 0; i < queryResultArr.length(); i++) {
                    JSONObject queryResultObj = queryResultArr.getJSONObject(i);
                    int appointment_id = queryResultObj.getInt("id");
                    int patient_id = queryResultObj.getInt("patientID");
                    int doctor_id = queryResultObj.getInt("doctorID");

                    String query_patient = String.format("select `username` from `user` where id=%d", patient_id);
                    String query_doctor = String.format("select `username` from `user` where id=%d", doctor_id);
                    Document user1 = Jsoup.connect(Constants.SERVER + query_patient).get();
                    String queryJson1 = user1.body().html();
                    Document user2 = Jsoup.connect(Constants.SERVER + query_doctor).get();
                    String queryJson2 = user2.body().html();
                    JSONArray queryPatientArray = new JSONArray(queryJson1);
                    JSONArray queryDoctorArray = new JSONArray(queryJson2);
                    JSONObject queryPatient = queryPatientArray.getJSONObject(0);
                    JSONObject queryDoctor = queryDoctorArray.getJSONObject(0);

                    String patient = queryPatient.getString("username");
                    String doctor = queryDoctor.getString("username");

                    String time = queryResultObj.getString("time");
                    String status = queryResultObj.getString("status");
                    String info = queryResultObj.getString("info");

                    db.addAppointment(appointment_id, patient, doctor, time, status, info);
                }
                Log.d("Reading: ", "Reading all appointments..");
                List<Appointment> aps = db.getAllAppointments();

                for (Appointment ap : aps) {
                    String log = "Id: " + ap.getId() +
                            " ,Patient: " + ap.getPatient() +
                            " ,Doctor: " + ap.getDoctor() +
                            " ,Time: " + ap.getTime() +
                            " ,Info: " + ap.getInfo() +
                            " ,Status: " + ap.getStatus();
                    Log.d("Appointment: ", log);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //download the treatments belong to the current user
        try {
            String query = String.format("select * from `treatment` where doctorID=%s or patientID=%s", user_id, user_id);
            Document document = Jsoup.connect(Constants.SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) {
                Log.d("After toast", "After toast 2");
            } else {
                JSONArray queryResultArr = new JSONArray(queryJson);
                Log.d("Insert: ", "Inserting ..");

                for (int i = 0; i < queryResultArr.length(); i++) {
                    JSONObject queryResultObj = queryResultArr.getJSONObject(i);
                    int treatment_id = queryResultObj.getInt("id");
                    int patient_id = queryResultObj.getInt("patientID");
                    int doctor_id = queryResultObj.getInt("doctorID");

                    String query_patient = String.format("select `username` from `user` where id=%d", patient_id);
                    String query_doctor = String.format("select `username` from `user` where id=%d", doctor_id);
                    Document user1 = Jsoup.connect(Constants.SERVER + query_patient).get();
                    String queryJson1 = user1.body().html();
                    Document user2 = Jsoup.connect(Constants.SERVER + query_doctor).get();
                    String queryJson2 = user2.body().html();
                    JSONArray queryPatientArray = new JSONArray(queryJson1);
                    JSONArray queryDoctorArray = new JSONArray(queryJson2);
                    JSONObject queryPatient = queryPatientArray.getJSONObject(0);
                    JSONObject queryDoctor = queryDoctorArray.getJSONObject(0);

                    String patient = queryPatient.getString("username");
                    String doctor = queryDoctor.getString("username");

                    String startdate = queryResultObj.getString("startdate");
                    String enddate = queryResultObj.getString("enddate");

                    String text = queryResultObj.getString("text");
                    db.addTreatment(treatment_id, patient, doctor, startdate, enddate, text);
                    String log = "Id: " + treatment_id +
                            " ,Patient: " + patient +
                            " ,Doctor: " + doctor +
                            " ,Start Date: " + startdate +
                            " ,End Date: " + enddate +
                            " ,Text: " + text;
                    Log.d("Inserted: ", log);
                }
                Log.d("Reading: ", "Reading all treatments..");
                List<Treatment> trs = db.getAllTreatments();

                for (Treatment tr : trs) {
                    String log = "Id: " + tr.getId() +
                            " ,Patient: " + tr.getPatient() +
                            " ,Doctor: " + tr.getDoctor() +
                            " ,Start Date: " + tr.getStartdate() +
                            " ,End Date: " + tr.getEnddate() +
                            " ,Text: " + tr.getText();
                    Log.d("Treatment: ", log);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // Setup a recurring alarm at 6am everyday
    public void scheduleAlarm() {
        Log.d("scheduleAlarm", "schedule Alarm");
        // Construct an intent that will execute the ReminderAlarmReceiver
        Intent intent = new Intent(MainActivity.getActivity().getApplicationContext(), ReminderAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(MainActivity.getActivity().getApplicationContext(),
                ReminderAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 6am
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6); // For 6AM, Change to current time to test
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0); // to be more precise

        // create an alarm that will first go of at the set calendar time
        // and keep repeating after 1 day
        AlarmManager alarm = (AlarmManager) MainActivity.getActivity().getSystemService(Context.ALARM_SERVICE);

        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pIntent);
        //change to INTERVAL_FIFTEEN_MINUTE to test
    }
}
