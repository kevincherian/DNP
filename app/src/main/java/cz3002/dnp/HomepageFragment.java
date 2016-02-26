package cz3002.dnp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import cz3002.dnp.Controller.UserCtrl;

/**
 * Created by hizac on 23/2/2016.
 */
public class HomepageFragment extends Fragment implements Constants {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_homepage, container, false);

        // Set greeting text
        TextView greetingText = (TextView) rootView.findViewById(R.id.greetingText);
        greetingText.setText(String.format("Hello, %s!", UserCtrl.getInstance().currentUser.getUsername()));
        if (UserCtrl.getInstance().currentUser.isDoctor()) { // If user is a doctor
            greetingText.setText(String.format("Hello, Dr. %s!", UserCtrl.getInstance().currentUser.getUsername()));
        }

        // Get Manage Account Button
        Button manageAccBtn = (Button) rootView.findViewById(R.id.manageAccountButton);
        manageAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toManageAccFragment();
            }
        });

        // Get Appointment Button
        Button appointmentListBtn = (Button) rootView.findViewById(R.id.appointmentButton);
        appointmentListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAppointmentList();
            }
        });

        return rootView;
    }


    private void toManageAccFragment() {
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("home").replace(R.id.main_container, new ManageAccountFragment()).commit();
    }

    private void toAppointmentList() {
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("home").replace(R.id.main_container, new AppointmentListFragment()).commit();
    }
}
