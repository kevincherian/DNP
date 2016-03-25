package cz3002.dnp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cz3002.dnp.Controller.AppointmentCtrl;
import cz3002.dnp.Controller.NotificationCtrl;
import cz3002.dnp.Controller.TreatmentCtrl;
import cz3002.dnp.Controller.UserCtrl;
import cz3002.dnp.Entity.Notification;

/**
 * Created by hizac on 23/2/2016.
 */
public class HomepageFragment extends Fragment {
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

        // Get Treatment Button
        Button treatmentListBtn = (Button) rootView.findViewById(R.id.viewTreatmentButton);
        treatmentListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTreatmentList();
            }
        });

        // Get Notification Button
        Button notificationListBtn = (Button) rootView.findViewById(R.id.notificationButton);
        notificationListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNotificationList();
            }
        });

        // Load appointments
        Thread retrieveAppointments = new Thread(new Runnable() {
            public void run() {
                if (AppointmentCtrl.getInstance().getAppointments().size() < 1) {
                    AppointmentCtrl.getInstance().retrieveAppointments();
                }
            }
        });
        retrieveAppointments.start();

        // Load treatments
        Thread retrieveTreatments = new Thread(new Runnable() {
            public void run() {
                if (TreatmentCtrl.getInstance().getTreatments().size() < 1) {
                    TreatmentCtrl.getInstance().retrieveTreatments();
                }
            }
        });
        retrieveTreatments.start();

        // Load notifications
        Thread retrieveNotifications = new Thread(new Runnable() {
            public void run() {
                if (NotificationCtrl.getInstance().getNotifications().size() < 1) {
                    NotificationCtrl.getInstance().retrieveNotifications();
                }
            }
        });
        retrieveNotifications.start();

        return rootView;
    }

    private void toNotificationList() {
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("home").replace(R.id.main_container, new NotificationListFragment()).commit();
    }

    private void toTreatmentList() {
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("home").replace(R.id.main_container, new TreatmentListFragment()).commit();
    }


    private void toManageAccFragment() {
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("home").replace(R.id.main_container, new ManageAccountFragment()).commit();
    }

    private void toAppointmentList() {
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("home").replace(R.id.main_container, new AppointmentListFragment()).commit();
    }

}
