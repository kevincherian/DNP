package cz3002.dnp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import cz3002.dnp.Controller.AppointmentCtrl;
import cz3002.dnp.Controller.UserCtrl;
import cz3002.dnp.Entity.User;

/**
 * Created by hizac on 23/2/2016.
 */
public class MakeAppointmentFragment extends Fragment implements Constants {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_make_appointment, container, false);

        autoFillInCurrentUser();

        // Get Cancel button
        Button cancelBtn = (Button) rootView.findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        Button submitBtn = (Button) rootView.findViewById(R.id.submitButton);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        return rootView;
    }

    // Check whether doctor and patient are in the correct username
    private boolean validateUsername() {
        // Get doctor's username
        EditText doctorUsername = (EditText) rootView.findViewById(R.id.doctorField);
        String doctorUsernameString = doctorUsername.getText().toString();
        // Query on server using that username
        try {
            String query = String.format("select `username` from `user` inner join `doctor` on user.id=doctor.userid where username='%s'", doctorUsernameString); // query to check username existence
            Document document = Jsoup.connect(SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // Username not existed
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get patient's username
        EditText patientUsername = (EditText) rootView.findViewById(R.id.patientField);
        String patientUsernameString = patientUsername.getText().toString();
        // Query on server using that username
        try {
            String query = String.format("select `username` from `user` inner join `patient` on user.id=patient.userid where username='%s'", patientUsernameString); // query to check username existence
            Document document = Jsoup.connect(SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // Username not existed
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    // Return date and time from DatePicker and TimePicker
    private Date getDateTime() {
        Date datetime = new Date();
        // Get Date
        DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.dateField);
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        month++;
        int date = datePicker.getDayOfMonth();
        // Get Time
        TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.timeField);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        // Set Date and Time to datetime
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,date,hour,minute);
        datetime.setTime(cal.getTimeInMillis());
//        // Print out the time in a proper format
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String datetimeString = format.format(datetime);
//        Toast.makeText(getContext(), datetimeString, Toast.LENGTH_LONG).show();
        return datetime;
    }

    private void autoFillInCurrentUser () {
        // Set given info
        String usernameString = UserCtrl.getInstance().currentUser.getUsername();
        boolean isCurrentDoctor = UserCtrl.getInstance().currentUser.isDoctor();

        EditText userField;
        if(isCurrentDoctor) { // If current user is a doctor
            userField = (EditText) rootView.findViewById(R.id.doctorField);
        } else {
            userField = (EditText) rootView.findViewById(R.id.patientField);
        }
        userField.setText(usernameString);
        userField.setEnabled(false);
    }

    private void submit() {
        // Validate usernames
        if (!validateUsername()) { // If fails
            Toast.makeText(getContext(), "Please check again the usernames you have entered!", Toast.LENGTH_LONG).show();
            return;
        }
        // If usernames are correct, continue
        // Get all the info
        Date datetime = getDateTime(); // Get date and time
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datetimeString = format.format(datetime);
        EditText doctorUsername = (EditText) rootView.findViewById(R.id.doctorField);
        String doctorUsernameString = doctorUsername.getText().toString(); // Get doctor username
        User doctor = UserCtrl.getInstance().getUser(doctorUsernameString); // Get object doctor
        EditText patientUsername = (EditText) rootView.findViewById(R.id.doctorField);
        String patientUsernameString = patientUsername.getText().toString(); // Get patient username
        User patient = UserCtrl.getInstance().getUser(patientUsernameString); // Get object patient
        EditText info = (EditText) rootView.findViewById(R.id.infoField);
        String infoString = info.getText().toString(); // Get information
        String statusString = "Pending"; // Status must be "Pending" because the other party has not confirmed

        // Push all to server
        try {
            String query = String.format("insert into `appointment` (time, doctorID, patientID, info, status) " +
                    "values ('%s', '%d', '%d', '%s', '%s')", datetimeString, doctor.getId(), patient.getId(), infoString, statusString); // query to check username existence
            Document document = Jsoup.connect(SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // Error happens
                Toast.makeText(getContext(), "An unexpected error occurs.\nPlease try again!", Toast.LENGTH_LONG).show();
                return;
            }
            // Otherwise, if success, go back to Appointment List Fragment and notify user
            AppointmentCtrl.getInstance().retrieveAnAppointment(datetime, doctor, patient); // Put the appointment just created to internal database
            cancel(); // Stop current job, go back to Appointment List Fragment
            Toast.makeText(MainActivity.getActivity(), "Appointment submitted!", Toast.LENGTH_LONG).show();

            // Notify other party
            // Insert code here
            // Insert code here
            // Insert code here
            // Insert code here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cancel() {
        MainActivity.getActivity().onBackPressed();
    }
}
