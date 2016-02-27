package cz3002.dnp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import cz3002.dnp.Controller.UserCtrl;
import database.AppointmentContract;
import database.AppointmentDBHelper;

/**
 * Created by hizac on 23/2/2016.
 */
public class LoginFragment extends Fragment implements Constants {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_login, container, false);

        Button signUpBtn = (Button)rootView.findViewById(R.id.signupButton);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        Button loginBtn = (Button)rootView.findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        Button forgetPasswordBtn = (Button)rootView.findViewById(R.id.btn_ForgetPass);
        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverPassword();
            }
        });

        return rootView;
    }

    private void logIn() {
        EditText username = (EditText) rootView.findViewById(R.id.usernameField);
        String usernameString = username.getText().toString();
        EditText password = (EditText) rootView.findViewById(R.id.passwordField);
        String passwordString = password.getText().toString();
        try {
            String query = String.format("select * from `user` where username='%s'", usernameString);
            Document document = Jsoup.connect(SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) {
                Toast.makeText(getContext(), "Wrong username!", Toast.LENGTH_LONG).show();
            } else {
                // Query user information
                JSONArray queryResultArr = new JSONArray(queryJson);
                JSONObject queryResultObj = queryResultArr.getJSONObject(0);
                String realPassword = queryResultObj.getString("password");
                int user_id = queryResultObj.getInt("id");
                // Query user type (doctor or patient)

                if (realPassword.equals(passwordString)) {
                    // Create user object
                    UserCtrl.getInstance().currentUser = UserCtrl.getInstance().getUser(usernameString);
                    new SyncDBAsync().execute(String.valueOf(user_id));
                    MainActivity.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomepageFragment()).commit();
                    Toast.makeText(MainActivity.getActivity(), String.format("Welcome %s!", UserCtrl.getInstance().currentUser.getUsername()), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Wrong password!", Toast.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class SyncDBAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            syncDB(Integer.parseInt(params[0]));
            return "Done syncing the local database";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private void signUp() {
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("login").replace(R.id.main_container, new SignupFragment()).commit();
    }

    private void recoverPassword() {
        Toast.makeText(getContext(), "Your password has just been sent to your email!", Toast.LENGTH_SHORT).show();
    }

    private void syncDB(int user_id){
        AppointmentDBHelper dbHelper = new AppointmentDBHelper(getContext());
        // Get the database. If it does not exist, this is where it will
        // also be created. If it exist, drop the old table and create new one
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //query the appointments belong to the current user
        try {
            String query = String.format("select * from `appointment` where doctorID=%d or patientID=%d", user_id, user_id);
            Document document = Jsoup.connect(SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) {
                Toast.makeText(getContext(), "Currently have no appointment", Toast.LENGTH_LONG).show();
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
