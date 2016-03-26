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
import cz3002.dnp.Controller.NotificationCtrl;
import cz3002.dnp.Controller.UserCtrl;
import cz3002.dnp.Entity.Notification;
import cz3002.dnp.Entity.User;

/**
 * Created by hizac on 23/2/2016.
 */
public class MakeCommunicationFragment extends Fragment {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_make_communication, container, false);

        autoFillInCurrentUser();

        // Get Cancel button
        Button cancelBtn = (Button) rootView.findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        Button sendBtn = (Button) rootView.findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(new View.OnClickListener() {
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

    private void autoFillInCurrentUser () {
        // Catch partner info
        String partnerUsernameString = "";
        try {
            Bundle bundle = this.getArguments();
            partnerUsernameString = bundle.get("partner").toString();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Set given info
        String usernameString = UserCtrl.getInstance().currentUser.getUsername();
        boolean isCurrentDoctor = UserCtrl.getInstance().currentUser.isDoctor();

        EditText userField, partnerField;
        if(isCurrentDoctor) { // If current user is a doctor
            userField = (EditText) rootView.findViewById(R.id.doctorField);
            partnerField = (EditText) rootView.findViewById(R.id.patientField);
        } else {
            userField = (EditText) rootView.findViewById(R.id.patientField);
            partnerField = (EditText) rootView.findViewById(R.id.doctorField);
        }
        userField.setText(usernameString);
        userField.setEnabled(false);
        partnerField.setText(partnerUsernameString);
    }

    private void submit() {
        // Validate usernames
        if (!validateUsername()) { // If fails
            Toast.makeText(getContext(), "Please check again the usernames you have entered!", Toast.LENGTH_LONG).show();
            return;
        }
        // If usernames are correct, continue
        // Get all the info
//        Date datetime = getDateTime(); // Get date and time
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String datetimeString = format.format(datetime);
        EditText doctorUsername = (EditText) rootView.findViewById(R.id.doctorField);
        String doctorUsernameString = doctorUsername.getText().toString(); // Get doctor username
        User doctor = UserCtrl.getInstance().getUser(doctorUsernameString); // Get object doctor
        EditText patientUsername = (EditText) rootView.findViewById(R.id.patientField);
        String patientUsernameString = patientUsername.getText().toString(); // Get patient username
        User patient = UserCtrl.getInstance().getUser(patientUsernameString); // Get object patient
        EditText info = (EditText) rootView.findViewById(R.id.infoField);
        String infoString = info.getText().toString(); // Get information
        infoString = infoString.replace("\n", "%0A").replace("\r", "");

        // Push all to server
        try {
            Notification notiPartner = new Notification();
            notiPartner.setTime(new Date());
            notiPartner.setSender(UserCtrl.getInstance().currentUser);
            User recipient = doctor;
            if (UserCtrl.getInstance().currentUser.isDoctor()) {
                recipient = patient;
            }
            notiPartner.setRecipient(recipient);
            notiPartner.setType(1);
            notiPartner.setContent(String.format("%s",infoString));
            NotificationCtrl.getInstance().pushANotification(notiPartner);

            // Otherwise, if success, go back to Appointment List Fragment and notify user
            NotificationCtrl.getInstance().retrieveNotifications(); // Put the appointment just created to internal database
            cancel(); // Stop current job, go back to Appointment List Fragment
            Toast.makeText(MainActivity.getActivity(), "Message sent!", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancel() {
        MainActivity.getActivity().onBackPressed();
    }
}
