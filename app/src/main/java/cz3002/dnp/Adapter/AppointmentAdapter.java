package cz3002.dnp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cz3002.dnp.Controller.AppointmentCtrl;
import cz3002.dnp.Entity.Appointment;
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
                    TextView tv = (TextView)itemView.findViewById(R.id.title);

                    goToAnotherPage(item);
                }
            });
        }

        private void goToAnotherPage(Appointment item) {
            Toast.makeText(MainActivity.getActivity(),item.toString(),Toast.LENGTH_LONG).show();
        }

        public void setItem(Appointment item) {
            this.item = item;
            TextView tv = (TextView)itemView.findViewById(R.id.title);
            tv.setText(item.getStatus());
        }
    }
}
