package cz3002.dnp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by hizac on 23/2/2016.
 */
public class CreateEditAppointmentFragment extends Fragment implements Constants {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_create_edit_appointment, container, false);

        autoFillInCurrentUser();

        // Get Cancel button
        Button cancelBtn = (Button) rootView.findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        return rootView;
    }

    private void autoFillInCurrentUser () {
        // Set given info
        String usernameString = MainActivity.getActivity().currentUser.getUsername();
        boolean isCurrentDoctor = MainActivity.getActivity().currentUser.isDoctor();

        EditText userField;
        if(isCurrentDoctor) { // If current user is a doctor
            userField = (EditText) rootView.findViewById(R.id.doctorField);
        } else {
            userField = (EditText) rootView.findViewById(R.id.patientField);
        }
        userField.setText(usernameString);
        userField.setEnabled(false);
    }

    private void cancel() {
        MainActivity.getActivity().onBackPressed();
    }
}
