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
        Button forgetPasswordBtn = (Button)rootView.findViewById(R.id.btn_ForgetPass);
        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverPassword();
            }
        });
        EditText username = (EditText) rootView.findViewById(R.id.usernameField);

        try {
            Bundle bundle = this.getArguments();
            String givenUsername = bundle.get("username").toString();
            username.setText(givenUsername);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private void logIn() {
        EditText username = (EditText) rootView.findViewById(R.id.usernameField);
        String usernameString = username.getText().toString();
        EditText password = (EditText) rootView.findViewById(R.id.passwordField);
        String passwordString = password.getText().toString();
        try {
            String query = String.format("select * from `user` where username='%s'", usernameString);
            Document document = Jsoup.connect(SERVER + query).get();
            String queryJson = document.body().html();
            if (queryJson.equals("0")) {
                Toast.makeText(getContext(), "Wrong username!", Toast.LENGTH_LONG).show();
            } else {
                // Query user information
                JSONArray queryResultArr = new JSONArray(queryJson);
                JSONObject queryResultObj = queryResultArr.getJSONObject(0);
                String realPassword = queryResultObj.getString("password");
                // Query user type (doctor or patient)

                if (realPassword.equals(passwordString)) {
                    // Create user object
                    UserCtrl userCtrl = new UserCtrl();
                    MainActivity.getActivity().currentUser = userCtrl.getUserInfo(MainActivity.getActivity().currentUser, usernameString);
                    Toast.makeText(getContext(), String.format("Welcome %s!", MainActivity.getActivity().currentUser.getUsername()), Toast.LENGTH_LONG).show();
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
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("login").replace(R.id.main_container, new SignupFragment()).commit();
    }

    private void recoverPassword() {
        Toast.makeText(getContext(), "Your password has just been sent to your email!", Toast.LENGTH_SHORT).show();
    }
}
