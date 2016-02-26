package cz3002.dnp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import cz3002.dnp.Adapter.AppointmentAdapter;

/**
 * Created by hizac on 23/2/2016.
 */
public class AppointmentListFragment extends Fragment implements Constants {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_appointment_list, container, false);

        Button newAppointmentBtn = (Button) rootView.findViewById(R.id.newAppointmentButton);
        newAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAppointment();
            }
        });
        RecyclerView list = (RecyclerView)rootView.findViewById(R.id.appointment_list);
        list.setAdapter(new AppointmentAdapter());
        list.setLayoutManager(new LinearLayoutManager(MainActivity.getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        return rootView;
    }

    private void newAppointment() {
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("appointmentlist").replace(R.id.main_container, new CreateEditAppointmentFragment()).commit();
    }
}
