package cz3002.dnp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cz3002.dnp.Adapter.NotificationAdapter;
import cz3002.dnp.Controller.UserCtrl;

/**
 * Created by hizac on 23/2/2016.
 */
public class NotificationListFragment extends Fragment {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_notification_list, container, false);

        Button newMessageBtn = (Button) rootView.findViewById(R.id.newMessageButton);
        newMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMessage();
            }
        });
        RecyclerView list = (RecyclerView)rootView.findViewById(R.id.notification_list);
       // list.invalidate();
        list.setAdapter(new NotificationAdapter());
        list.setLayoutManager(new LinearLayoutManager(MainActivity.getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        return rootView;
    }

    private void newMessage() {
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("notificationlist").replace(R.id.main_container, new MakeCommunicationFragment()).commit();
    }
}
