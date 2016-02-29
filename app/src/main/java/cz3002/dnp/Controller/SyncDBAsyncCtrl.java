package cz3002.dnp.Controller;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import cz3002.dnp.Constants;
import cz3002.dnp.MainActivity;
import database.AppointmentContract;
import database.AppointmentDBHelper;

/**
 * Created by hizac on 28/2/2016.
 */
public class SyncDBAsyncCtrl extends AsyncTask<String, Void, String> {

    private static SyncDBAsyncCtrl instance;
    public static SyncDBAsyncCtrl getInstance() {
        if (instance == null) {
            instance = new SyncDBAsyncCtrl();
        }
        return instance;
    }

    @Override
    protected String doInBackground(String... params) {
        syncDB(Integer.parseInt(params[0]));
        return "Done syncing the local database";
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(MainActivity.getActivity(), result, Toast.LENGTH_LONG).show();
    }

    private void syncDB(int user_id){
        AppointmentDBHelper dbHelper = new AppointmentDBHelper(MainActivity.getActivity());
        // Get the database. If it does not exist, this is where it will
        // also be created. If it exist, drop the old table and create new one
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //query the appointments belong to the current user
        try {
            String query = String.format("select * from `appointment` where doctorID=%d or patientID=%d", user_id, user_id);
            Document document = Jsoup.connect(Constants.SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) {
                Toast.makeText(MainActivity.getActivity(), "Currently have no appointment", Toast.LENGTH_LONG).show();
            } else {
                JSONArray queryResultArr = new JSONArray(queryJson);
                for (int i = 0; i < queryResultArr.length(); i++){
                    JSONObject queryResultObj = queryResultArr.getJSONObject(i);
                    int appointment_id = queryResultObj.getInt("id");
                    int patient_id = queryResultObj.getInt("patient_id");
                    int doctor_id = queryResultObj.getInt("id");
                    String time = queryResultObj.getString("time");
                    String status = queryResultObj.getString("status");
                    String info = queryResultObj.getString("info");

                    ContentValues values = new ContentValues();
                    values.put(AppointmentContract.AppointmentEntry.COLUMN_NAME_APPOINTMENT_ID, appointment_id);
                    values.put(AppointmentContract.AppointmentEntry.COLUMN_NAME_PATIENT_ID, patient_id);
                    values.put(AppointmentContract.AppointmentEntry.COLUMN_NAME_DOCTOR_ID, doctor_id);
                    values.put(AppointmentContract.AppointmentEntry.COLUMN_NAME_TIME, time);
                    values.put(AppointmentContract.AppointmentEntry.COLUMN_NAME_STATUS, status);
                    values.put(AppointmentContract.AppointmentEntry.COLUMN_NAME_INFO, info);

                    // Insert the new row, returning the primary key value of the new row
                    long newRowId;
                    newRowId = db.insert(
                            AppointmentContract.AppointmentEntry.TABLE_NAME,
                            null,
                            values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //TODO: query the treatment belong to the current user
    }
}
