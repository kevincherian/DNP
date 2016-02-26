package cz3002.dnp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

        return rootView;
    }

    private void newAppointment() {
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("appointmentlist").replace(R.id.main_container, new CreateEditAppointmentFragment()).commit();
    }
}
