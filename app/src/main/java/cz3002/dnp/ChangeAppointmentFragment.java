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

import cz3002.dnp.Controller.AppointmentCtrl;
import cz3002.dnp.Entity.Appointment;

/**
 * Created by hizac on 23/2/2016.
 */
public class ChangeAppointmentFragment extends Fragment implements Constants {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_change_appointment, container, false);
        int Id = 0;
        try {
            Bundle bundle = this.getArguments();
            Id = Integer.parseInt(bundle.get("appointmentId").toString());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        final int appointmentId = Id;
        autoFillIn(appointmentId);

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

        Button deleteBtn = (Button) rootView.findViewById(R.id.deleteButton);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(appointmentId);
            }
        });

        return rootView;
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

    private void autoFillIn(int appointmentId) {
        // Set given info
        Appointment currentAppointment = AppointmentCtrl.getInstance().getAppointments().get(appointmentId);

        EditText doctor = (EditText) rootView.findViewById(R.id.doctorField);
        doctor.setText(currentAppointment.getDoctor().getUsername());
        doctor.setEnabled(false);
        EditText patient = (EditText) rootView.findViewById(R.id.patientField);
        patient.setText(currentAppointment.getPatient().getUsername());
        patient.setEnabled(false);
        EditText info = (EditText) rootView.findViewById(R.id.infoField);
        info.setText(currentAppointment.getInfo());
        info.setEnabled(false);
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentAppointment.getTime());
        DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.dateField);
        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.timeField);
        timePicker.setHour(cal.get(Calendar.HOUR));
        timePicker.setMinute(cal.get(Calendar.MINUTE));

    }

    private void submit() {
//        // Validate usernames
//        if (!validateUsername()) { // If fails
//            Toast.makeText(getContext(), "Please check again the usernames you have entered!", Toast.LENGTH_LONG).show();
//            return;
//        }
//        // If usernames are correct, continue
//        // Get all the info
//        Date datetime = getDateTime(); // Get date and time
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String datetimeString = format.format(datetime);
//        EditText doctorUsername = (EditText) rootView.findViewById(R.id.doctorField);
//        String doctorUsernameString = doctorUsername.getText().toString(); // Get doctor username
//        User doctor = UserCtrl.getInstance().getUser(doctorUsernameString); // Get object doctor
//        EditText patientUsername = (EditText) rootView.findViewById(R.id.doctorField);
//        String patientUsernameString = patientUsername.getText().toString(); // Get patient username
//        User patient = UserCtrl.getInstance().getUser(patientUsernameString); // Get object patient
//        EditText info = (EditText) rootView.findViewById(R.id.infoField);
//        String infoString = info.getText().toString(); // Get information
//        String statusString = "Pending"; // Status must be "Pending" because the other party has not confirmed
//
//        // Push all to server
//        try {
//            String query = String.format("insert into `appointment` (time, doctorID, patientID, info, status) " +
//                    "values ('%s', '%d', '%d', '%s', '%s')", datetimeString, doctor.getId(), patient.getId(), infoString, statusString); // query to check username existence
//            Document document = Jsoup.connect(SERVER + query).get();
//            String queryJson = document.body().html();
//            if (queryJson.equals("0")) { // Error happens
//                Toast.makeText(getContext(), "An unexpected error occurs.\nPlease try again!", Toast.LENGTH_LONG).show();
//                return;
//            }
//            // Otherwise, if success, go back to Appointment List Fragment and notify user
//            AppointmentCtrl.getInstance().retrieveAnAppointment(datetime, doctor, patient); // Put the appointment just created to internal database
//            cancel(); // Stop current job, go back to Appointment List Fragment
//            Toast.makeText(MainActivity.getActivity(), "Appointment submitted!", Toast.LENGTH_LONG).show();
//
//            // Notify other party
//            // Insert code here
//            // Insert code here
//            // Insert code here
//            // Insert code here
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void delete(int appointmentId) {
        Appointment currentAppointment = AppointmentCtrl.getInstance().getAppointments().get(appointmentId);
        // Remove from server
        try {
            String query = String.format("delete from `appointment` where id=%d", currentAppointment.getId()); // query to delete an appointment
            Document document = Jsoup.connect(SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // Error happens
                Toast.makeText(getContext(), "An unexpected error occurs.\nPlease try again!", Toast.LENGTH_LONG).show();
                return;
            }
            // Else, if success, continue
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppointmentCtrl.getInstance().getAppointments().remove(appointmentId);
        cancel();
    }

    private void cancel() {
        MainActivity.getActivity().onBackPressed();
    }
}
