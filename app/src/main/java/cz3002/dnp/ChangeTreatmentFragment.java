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
import cz3002.dnp.Entity.Treatment;
import cz3002.dnp.Entity.User;

/**
 * Created by hizac on 23/2/2016.
 */
public class ChangeTreatmentFragment extends Fragment {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_change_treatment, container, false);
        int Id = 0;
        try {
            Bundle bundle = this.getArguments();
            Id = Integer.parseInt(bundle.get("treatmentId").toString());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        final int treatmentId = Id;
        autoFillIn(treatmentId);

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
                submit(treatmentId);
            }
        });

        return rootView;
    }


    private void autoFillIn(int treatmentId) {
        // Set given info
        Treatment currentTreatment = TreatmentCtrl.getInstance().getTreatments().get(treatmentId);

        EditText doctor = (EditText) rootView.findViewById(R.id.doctorField);
        doctor.setText(currentTreatment.getDoctor().getUsername());
        doctor.setEnabled(false);
        EditText patient = (EditText) rootView.findViewById(R.id.patientField);
        patient.setText(currentTreatment.getPatient().getUsername());
        patient.setEnabled(false);
        EditText info = (EditText) rootView.findViewById(R.id.infoField);
        info.setText(currentTreatment.getInfo());

    }

    private void submit(int treatmentId) {
        Treatment currentTreatment = TreatmentCtrl.getInstance().getTreatments().get(treatmentId);
        Treatment editedTreatment = currentTreatment;

        // Get all the info
        EditText info = (EditText) rootView.findViewById(R.id.infoField);
        String infoString = info.getText().toString();
        editedTreatment.setInfo(infoString);

        // Push all to server
        try {
            String query = String.format("update `treatment` set text='%s' where id=%d", infoString, currentTreatment.getId()); // query to check username existence
            query = query.replace("\n", "%0A");
            query = query.replace("\r", "");
            System.out.println(query);
            Document document = Jsoup.connect(Constants.SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) { // Error happens
                Toast.makeText(getContext(), "An unexpected error occurs.\nPlease try again!", Toast.LENGTH_LONG).show();
                return;
            }
            // Otherwise, if success, go back to Treatment List Fragment and notify user
            TreatmentCtrl.getInstance().getTreatments().set(treatmentId, editedTreatment); // Put the treatment just created to internal database
            cancel(); // Stop current job, go back to Treatment List Fragment
            Toast.makeText(MainActivity.getActivity(), "Treatment updated!", Toast.LENGTH_LONG).show();

            // Notify other party
            // Notify with type as 0 (system notification)
            Notification notiPartner = new Notification();
            notiPartner.setTime(new Date());
            notiPartner.setSender(UserCtrl.getInstance().currentUser);
            User recipient = editedTreatment.getDoctor();
            if (UserCtrl.getInstance().currentUser.isDoctor()) {
                recipient = editedTreatment.getPatient();
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
