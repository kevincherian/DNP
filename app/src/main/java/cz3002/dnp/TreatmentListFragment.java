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

import cz3002.dnp.Adapter.TreatmentAdapter;

/**
 * Created by hizac on 23/2/2016.
 */
public class TreatmentListFragment extends Fragment {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_treatment_list, container, false);

        RecyclerView list = (RecyclerView)rootView.findViewById(R.id.treatment_list);
        list.setAdapter(new TreatmentAdapter());
        list.setLayoutManager(new LinearLayoutManager(MainActivity.getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        return rootView;
    }

}
