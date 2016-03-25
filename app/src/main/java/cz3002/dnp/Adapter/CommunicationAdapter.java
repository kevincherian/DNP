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
import cz3002.dnp.Controller.CommunicationCtrl;
import cz3002.dnp.Controller.UserCtrl;
import cz3002.dnp.Entity.Communication;
import cz3002.dnp.Entity.Treatment;
import cz3002.dnp.LoginFragment;
import cz3002.dnp.MainActivity;
import cz3002.dnp.R;

/**
 * Created by hizac on 26/2/2016.
 */
public class CommunicationAdapter extends RecyclerView.Adapter<CommunicationAdapter.CommunicationViewHolder> {
    private ArrayList<Communication> chats;
    public CommunicationAdapter(){
        chats = CommunicationCtrl.getInstance().getChats();
    }
    @Override
    public CommunicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.communication_in_list, parent, false);
        CommunicationViewHolder vh = new CommunicationViewHolder(mView);
        return vh;
    }

    @Override
    public void onBindViewHolder(CommunicationViewHolder holder, int position) {
        Communication chat = chats.get(position);
        holder.setItem(chat);
    }


    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class CommunicationViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        Communication item;
        public CommunicationViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //goToAnotherPage(item);
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
                            // goToAnotherPage(item);
                            break;

                        default:
                            itemView.setBackgroundColor(Color.parseColor("#FF329593"));
                            break;
                    }

                    return true;
                }
            });
        }

//        private void goToAnotherPage(Appointment item) {
//            // Pass appointment to change_appointment fragment
//            Bundle bundle = new Bundle();
//            bundle.putString("communicationId", String.format("%d", CommunicationCtrl.getInstance().getChats().indexOf(item)));
//            ChangeAppointmentFragment changeAppointmentFragment = new ChangeAppointmentFragment();
//            changeAppointmentFragment.setArguments(bundle);
//            // Go to change_appointment fragment
//            MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("appointmentlist").replace(R.id.main_container, changeAppointmentFragment).commit();
//        }


        public void setItem(Communication item) {
            this.item = item;
            TextView tvTime = (TextView)itemView.findViewById(R.id.time);
            // Reformat time
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String timeString = String.format("Communication on %s", format.format(item.getDate()));
            tvTime.setText(timeString);

            TextView tvPartner = (TextView) itemView.findViewById(R.id.partner);
            String partnerString;
            if (UserCtrl.getInstance().currentUser.isDoctor()) {
                partnerString = String.format("with patient %s", item.getPatient().getUsername());
            } else {
                partnerString = String.format("with Dr. %s",  item.getDoctor().getUsername());
            }
            tvPartner.setText(partnerString);

            TextView tvMessage = (TextView) itemView.findViewById(R.id.info);
            tvMessage.setText(item.getMessage());

        }
    }
}
