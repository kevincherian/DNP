package cz3002.dnp.Adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cz3002.dnp.Controller.NotificationCtrl;
import cz3002.dnp.Controller.UserCtrl;
import cz3002.dnp.Entity.Notification;
import cz3002.dnp.MainActivity;
import cz3002.dnp.R;

/**
 * Created by hizac on 26/2/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private ArrayList<Notification> notifications;
    public NotificationAdapter(){
        notifications = NotificationCtrl.getInstance().getNotifications();
    }
    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_in_list, parent, false);
        NotificationViewHolder vh = new NotificationViewHolder(mView);
        return vh;
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.setItem(notification);
    }


    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        Notification item;
        public NotificationViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    goToAnotherPage(item);
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
                            itemView.setBackgroundColor(Color.parseColor("#FF329593"));
                            break;

                        default:
                            itemView.setBackgroundColor(Color.parseColor("#FF329593"));
                            break;
                    }

                    return true;
                }
            });
        }

//        private void goToAnotherPage(Notification item) {
//            // Pass notification to change_notification fragment
//            Bundle bundle = new Bundle();
//            bundle.putString("notificationId", String.format("%d", NotificationCtrl.getInstance().getNotifications().indexOf(item)));
//            ChangeNotificationFragment changeNotificationFragment = new ChangeNotificationFragment();
//            changeNotificationFragment.setArguments(bundle);
//            // Go to change_notification fragment
//            MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("notificationlist").replace(R.id.main_container, changeNotificationFragment).commit();
//        }


        public void setItem(Notification item) {
            this.item = item;
            TextView tvTime = (TextView)itemView.findViewById(R.id.time);
            // Reformat time
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String timeString = String.format("%s", format.format(item.getTime()));
            tvTime.setText(timeString);

            TextView tvSender = (TextView) itemView.findViewById(R.id.sender);
            String partnerString = String.format("From: %s", item.getSender().getUsername());
            tvSender.setText(partnerString);

            TextView tvContent = (TextView) itemView.findViewById(R.id.content);
            tvContent.setText(item.getContent());

        }
    }
}
