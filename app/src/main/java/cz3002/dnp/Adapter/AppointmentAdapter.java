package cz3002.dnp.Adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz3002.dnp.ChangeAppointmentFragment;
import cz3002.dnp.Controller.AppointmentCtrl;
import cz3002.dnp.Controller.UserCtrl;
import cz3002.dnp.Entity.Appointment;
import cz3002.dnp.LoginFragment;
import cz3002.dnp.MainActivity;
import cz3002.dnp.R;

/**
 * Created by hizac on 26/2/2016.
 */
public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    private ArrayList<Appointment> appointments;
    public AppointmentAdapter(){
        appointments = AppointmentCtrl.getInstance().getAppointments();
    }
    @Override
    public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_in_list, parent, false);
        AppointmentViewHolder vh = new AppointmentViewHolder(mView);
        return vh;
    }

    @Override
    public void onBindViewHolder(AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.setItem(appointment);
    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        Appointment item;
        public AppointmentViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToAnotherPage(item);
                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            itemView.setBackgroundColor(Color.WHITE);
                            break;

                        case MotionEvent.ACTION_UP:
                            goToAnotherPage(item);
                            break;

                        default:
                            itemView.setBackgroundColor(Color.parseColor("#FF329593"));
                            break;
                    }

                    return true;
                }
            });
        }

        private void goToAnotherPage(Appointment item) {
            // Pass appointment to change_appointment fragment
            Bundle bundle = new Bundle();
            bundle.putString("appointmentId", String.format("%d", AppointmentCtrl.getInstance().getAppointments().indexOf(item)));
            ChangeAppointmentFragment changeAppointmentFragment = new ChangeAppointmentFragment();
            changeAppointmentFragment.setArguments(bundle);
            // Go to change_appointment fragment
            MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("appointmentlist").replace(R.id.main_container, changeAppointmentFragment).commit();
        }


        public void setItem(Appointment item) {
            this.item = item;
            TextView tvTime = (TextView)itemView.findViewById(R.id.time);
            // Reformat time
            SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            String timeString = format.format(item.getTime());
            tvTime.setText(String.format("Happening at %s", timeString));

            TextView tvPartner = (TextView) itemView.findViewById(R.id.partner);
            String partnerString;
            if (UserCtrl.getInstance().currentUser.isDoctor()) {
                partnerString = String.format("with patient %s", item.getPatient().getUsername());
            } else {
                partnerString = String.format("with Dr. %s",  item.getDoctor().getUsername());
            }
            tvPartner.setText(partnerString);

            TextView tvInfo = (TextView) itemView.findViewById(R.id.info);
            tvInfo.setText(item.getInfo());

            TextView tvStatus = (TextView) itemView.findViewById(R.id.status);
            String statusString = item.getStatus();
            if (statusString.equals("Pending")) {
                tvStatus.setTextColor(Color.GREEN);
            } else if (statusString.equals("Confirmed")) {
                tvStatus.setTextColor(Color.BLUE);
            } else {
                tvStatus.setTextColor(Color.RED);
            }
            tvStatus.setText(statusString);
        }
    }
}
