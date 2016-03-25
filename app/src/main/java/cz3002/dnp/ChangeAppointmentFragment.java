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

import cz3002.dnp.Controller.AppointmentCtrl;
import cz3002.dnp.Controller.NotificationCtrl;
import cz3002.dnp.Controller.UserCtrl;
import cz3002.dnp.Entity.Appointment;
import cz3002.dnp.Entity.Notification;
import cz3002.dnp.Entity.User;

/**
 * Created by hizac on 23/2/2016.
 */
public class ChangeAppointmentFragment extends Fragment {
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

        // Get Submit button
        Button submitBtn = (Button) rootView.findViewById(R.id.submitButton);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(appointmentId);
            }
        });

        // Get Delete button
        Button deleteBtn = (Button) rootView.findViewById(R.id.deleteButton);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(appointmentId);
            }
        });

        // Get Confirm button
        Button confirmBtn = (Button) rootView.findViewById(R.id.confirmButton);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm(appointmentId);
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
        int date = datePicker.getDayOfMonth();
        // Get Time
        TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.timeField);
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        // Set Date and Time to datetime
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date, hour, minute);
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

        // Show the confirm button if the current user has his/her right to confirm to a specific appointment
        Button confirmBtn = (Button) rootView.findViewById(R.id.confirmButton);
        confirmBtn.setVisibility(View.GONE);    // Firstly, remove the confirm button
        boolean condition = currentAppointment.getStatus().contains(UserCtrl.currentUser.getUsername()); // current user is someone who has the right to confirm
        if (condition) {
            confirmBtn.setVisibility(View.VISIBLE);
        }
        // Hide Delete button if appointment already canceled
        Button deleteBtn = (Button) rootView.findViewById(R.id.deleteButton);
        if (currentAppointment.getStatus().contains("Canceled")) {
            deleteBtn.setVisibility(View.GONE);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(currentAppointment.getTime());
        DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.dateField);
        datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.timeField);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));

    }

    private void submit(int appointmentId) {
        Appointment currentAppointment = AppointmentCtrl.getInstance().getAppointments().get(appointmentId);
        Appointment editedAppointment = currentAppointment;

        // Get all the info
        Date datetime = getDateTime(); // Get date and time
        editedAppointment.setTime(datetime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datetimeString = format.format(datetime);

        String statusString = String.format("Pending for %s", editedAppointment.getDoctor().getUsername()); // Status must be "Pending" because the other party has not confirmed
        if (UserCtrl.getInstance().currentUser.isDoctor()) {
            statusString = String.format("Pending for %s", editedAppointment.getPatient().getUsername());
        }
        editedAppointment.setStatus(statusString);

        // Push all to server
        try {
            String query = String.format("update `appointment` set time='%s', status='%s' where id=%d", datetimeString, statusString, currentAppointment.getId()); // query to check username existence
            Document document = Jsoup.connect(Constants.SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // Error happens
                Toast.makeText(getContext(), "An unexpected error occurs.\nPlease try again!", Toast.LENGTH_LONG).show();
                return;
            }
            // Otherwise, if success, go back to Appointment List Fragment and notify user
            AppointmentCtrl.getInstance().getAppointments().set(appointmentId, editedAppointment); // Put the appointment just created to internal database
            cancel(); // Stop current job, go back to Appointment List Fragment
            Toast.makeText(MainActivity.getActivity(), "Appointment updated!", Toast.LENGTH_LONG).show();

            // Notify other party
            // Notify with type as 0 (system notification)
            Notification notiPartner = new Notification();
            notiPartner.setTime(new Date());
            notiPartner.setSender(UserCtrl.getInstance().currentUser);
            User recipient = editedAppointment.getDoctor();
            if (UserCtrl.getInstance().currentUser.isDoctor()) {
                recipient = editedAppointment.getPatient();
            }
            notiPartner.setRecipient(recipient);
            notiPartner.setType(0);
            notiPartner.setContent(String.format(Constants.CHANGE_APPOINTMENT_NOTIFICATION, UserCtrl.getInstance().currentUser.getUsername()));
            NotificationCtrl.getInstance().pushANotification(notiPartner);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delete(int appointmentId) {
        Appointment currentAppointment = AppointmentCtrl.getInstance().getAppointments().get(appointmentId);
        Appointment editedAppointment = currentAppointment;

        editedAppointment.setStatus("Canceled");
        // Cancel from server
        try {
            String query = String.format("update `appointment` set status='Canceled' where id=%d", currentAppointment.getId()); // query to delete an appointment
            Document document = Jsoup.connect(Constants.SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // Error happens
                Toast.makeText(getContext(), "An unexpected error occurs.\nPlease try again!", Toast.LENGTH_LONG).show();
                return;
            }
            // Else, if success, continue
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppointmentCtrl.getInstance().getAppointments().set(appointmentId, editedAppointment); // Put the appointment just changed to internal database
        cancel(); // Stop current job, go back to Appointment List Fragment
        Toast.makeText(MainActivity.getActivity(), "Appointment canceled!", Toast.LENGTH_LONG).show();

        // Notify other party
        // Notify with type as 0 (system notification)
        Notification notiPartner = new Notification();
        notiPartner.setTime(new Date());
        notiPartner.setSender(UserCtrl.getInstance().currentUser);
        User recipient = editedAppointment.getDoctor();
        if (UserCtrl.getInstance().currentUser.isDoctor()) {
            recipient = editedAppointment.getPatient();
        }
        notiPartner.setRecipient(recipient);
        notiPartner.setType(0);
        notiPartner.setContent(String.format(Constants.DELETE_APPOINTMENT_NOTIFICATION, UserCtrl.getInstance().currentUser.getUsername()));
        NotificationCtrl.getInstance().pushANotification(notiPartner);
    }

    private void confirm(int appointmentId) {
        Appointment currentAppointment = AppointmentCtrl.getInstance().getAppointments().get(appointmentId);
        Appointment editedAppointment = currentAppointment;

        editedAppointment.setStatus("Confirmed");
        // Cancel from server
        try {
            String query = String.format("update `appointment` set status='Confirmed' where id=%d", currentAppointment.getId()); // query to delete an appointment
            Document document = Jsoup.connect(Constants.SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // Error happens
                Toast.makeText(getContext(), "An unexpected error occurs.\nPlease try again!", Toast.LENGTH_LONG).show();
                return;
            }
            // Else, if success, continue
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppointmentCtrl.getInstance().getAppointments().set(appointmentId, editedAppointment); // Put the appointment just changed to internal database
        cancel(); // Stop current job, go back to Appointment List Fragment
        Toast.makeText(MainActivity.getActivity(), "Appointment confirmed!", Toast.LENGTH_LONG).show();

        // Notify other party
        // Notify with type as 0 (system notification)
        Notification notiPartner = new Notification();
        notiPartner.setTime(new Date());
        notiPartner.setSender(UserCtrl.getInstance().currentUser);
        User recipient = editedAppointment.getDoctor();
        if (UserCtrl.getInstance().currentUser.isDoctor()) {
            recipient = editedAppointment.getPatient();
        }
        notiPartner.setRecipient(recipient);
        notiPartner.setType(0);
        notiPartner.setContent(String.format(Constants.CONFIRM_APPOINTMENT_NOTIFICATION, UserCtrl.getInstance().currentUser.getUsername()));
        NotificationCtrl.getInstance().pushANotification(notiPartner);
    }

    private void cancel() {
        MainActivity.getActivity().onBackPressed();
    }
}
