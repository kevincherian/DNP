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
        greetingText.setText(String.format("Hello, %s!", MainActivity.getActivity().currentUser.getUsername()));
        if (MainActivity.getActivity().currentUser.isDoctor()) { // If user is a doctor
            greetingText.setText(String.format("Hello, Dr. %s!", MainActivity.getActivity().currentUser.getUsername()));
        }
        return rootView;
    }


}