package cz3002.dnp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

import cz3002.dnp.R;

/**
 * Created by hizac on 23/2/2016.
 */
public class LoginFragment extends Fragment implements Constants {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_login, container, false);
        Button signUpBtn = (Button)rootView.findViewById(R.id.signupButton);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        Button loginBtn = (Button)rootView.findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        return rootView;
    }

    private void logIn() {
        EditText username = (EditText) rootView.findViewById(R.id.usernameField);
        String usernameString = username.getText().toString();
        EditText password = (EditText) rootView.findViewById(R.id.passwordField);
        String passwordString = password.getText().toString();
        try {
            String query = String.format("select `password` from `user` where username='%s'", usernameString);
            Document document = Jsoup.connect(SERVER + query).get();
            String query_json = document.body().html();
            if (query_json == "0") {
                Toast.makeText(getContext(), "Wrong username!", Toast.LENGTH_LONG).show();
            } else {
                JSONArray query_result_arr = new JSONArray(query_json);
                JSONObject query_result_obj = query_result_arr.getJSONObject(0);
                String realPassword = query_result_obj.getString("password");
                if (realPassword.equals(passwordString)) {
                    Toast.makeText(getContext(), "Welcome!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Wrong password!", Toast.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void signUp() {
        // Code here
    }
}
