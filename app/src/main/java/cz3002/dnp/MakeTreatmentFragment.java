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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz3002.dnp.Controller.NotificationCtrl;
import cz3002.dnp.Controller.TreatmentCtrl;
import cz3002.dnp.Controller.UserCtrl;
import cz3002.dnp.Entity.Notification;
import cz3002.dnp.Entity.User;

/**
 * Created by hizac on 23/2/2016.
 */
public class MakeTreatmentFragment extends Fragment {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_make_treatment, container, false);

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
            Document document = Jsoup.connect(Constants.SERVER + query).get();
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
            Document document = Jsoup.connect(Constants.SERVER + query).get();
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
    private Date getDateTime(DatePicker datePicker) {
        Date datetime = new Date();

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int date = datePicker.getDayOfMonth();
        // Set Date to datetime
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,date);
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
        DatePicker startdatePicker = (DatePicker) rootView.findViewById(R.id.startdateField);
        DatePicker enddatePicker = (DatePicker) rootView.findViewById(R.id.enddateField);
        Date startdate = getDateTime(startdatePicker);
        Date enddate = getDateTime(enddatePicker);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startdateString = format.format(startdate);
        String enddateString = format.format(enddate);
        EditText doctorUsername = (EditText) rootView.findViewById(R.id.doctorField);
        String doctorUsernameString = doctorUsername.getText().toString(); // Get doctor username
        User doctor = UserCtrl.getInstance().getUser(doctorUsernameString); // Get object doctor
        EditText patientUsername = (EditText) rootView.findViewById(R.id.patientField);
        String patientUsernameString = patientUsername.getText().toString(); // Get patient username
        User patient = UserCtrl.getInstance().getUser(patientUsernameString); // Get object patient
        EditText info = (EditText) rootView.findViewById(R.id.infoField);
        String infoString = info.getText().toString(); // Get information

        // Push all to server
        try {
            String query = String.format("insert into `treatment` (startdate, enddate, doctorID, patientID, text) " +
                    "values ('%s', '%s', '%d', '%d', '%s')", startdateString, enddateString, doctor.getId(), patient.getId(), infoString); // query to check username existence
            query = query.replace("\n","%0A");
            Document document = Jsoup.connect(Constants.SERVER + query).get();
            String queryJson = document.body().html();
            System.out.println(queryJson);
            if (queryJson.equals("0")) { // Error happens
                Toast.makeText(getContext(), "An unexpected error occurs.\nPlease try again!", Toast.LENGTH_LONG).show();
                return;
            }
            // Otherwise, if success, go back to Treatment List Fragment and notify user
            TreatmentCtrl.getInstance().retrieveAnTreatment(startdate, enddate, doctor, patient); // Put the treatment just created to internal database
            cancel(); // Stop current job, go back to Treatment List Fragment
            Toast.makeText(MainActivity.getActivity(), "Treatment submitted!", Toast.LENGTH_LONG).show();

            // Notify other party
            // Notify with type as 0 (system notification)
            Notification notiPartner = new Notification();
            notiPartner.setTime(new Date());
            notiPartner.setSender(UserCtrl.getInstance().currentUser);
            User recipient = doctor;
            if (UserCtrl.getInstance().currentUser.isDoctor()) {
                recipient = patient;
            }
            notiPartner.setRecipient(recipient);
            notiPartner.setType(0);
            notiPartner.setContent(String.format(Constants.TREATMENT_NOTIFICATION, UserCtrl.getInstance().currentUser.getUsername()));
            NotificationCtrl.getInstance().pushANotification(notiPartner);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cancel() {
        MainActivity.getActivity().onBackPressed();
    }
}
