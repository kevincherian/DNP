package cz3002.dnp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
public class SignupFragment extends Fragment implements Constants {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_signup, container, false);
        Button cancelBtn = (Button)rootView.findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        Button submitBtn = (Button)rootView.findViewById(R.id.submitButton);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        return rootView;
    }

    private void submit() {
        /*===================================================
            Read inputs
         ===================================================*/
        EditText username = (EditText) rootView.findViewById(R.id.usernameField);
        String usernameString = username.getText().toString();
        EditText password = (EditText) rootView.findViewById(R.id.passwordField);
        String passwordString = password.getText().toString();
        EditText email = (EditText) rootView.findViewById(R.id.emailField);
        String emailString = email.getText().toString();
        EditText fullname = (EditText) rootView.findViewById(R.id.nameField);
        String fullnameString = fullname.getText().toString();
        RadioGroup type = (RadioGroup) rootView.findViewById(R.id.typeRadioGrp);
        int selectedTypeID = type.getCheckedRadioButtonId();
        RadioButton selectedTypeRadioBtn = (RadioButton) rootView.findViewById(selectedTypeID);
        String selectedTypeString = selectedTypeRadioBtn.getText().toString().toLowerCase();

        /*===================================================
            Check validation client-side
        ===================================================*/
        if (usernameString.equals("")) {
            Toast.makeText(MainActivity.getActivity(), "Please enter username!", Toast.LENGTH_LONG).show();
            return;
        }
        if (passwordString.equals("")) {
            Toast.makeText(MainActivity.getActivity(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (emailString.equals("")) {
            Toast.makeText(MainActivity.getActivity(), "Please enter email!", Toast.LENGTH_LONG).show();
            return;
        }
        if (fullnameString.equals("")) {
            Toast.makeText(MainActivity.getActivity(), "Please enter your full name!", Toast.LENGTH_LONG).show();
            return;
        }


        /*===================================================
            Check validation server-side and insert to database
         ===================================================*/
        try {
            String query = String.format("select `username` from `user` where username='%s'", usernameString); // query to check username existence
            Document document = Jsoup.connect(SERVER + query).get();
            String queryJson = document.body().html();
            if (!queryJson.equals("0")) { // Username existed
                Toast.makeText(getContext(), String.format("The username %s is existed.\nPlease use another one!", usernameString), Toast.LENGTH_LONG).show();
            } else { // Possible to register
                query = String.format("insert into `user` (username, password, email, fullname) values ('%s', '%s', '%s', '%s')", usernameString, passwordString, emailString, fullnameString);
                document = Jsoup.connect(SERVER + query).get();
                queryJson = document.body().html();
                if (queryJson.equals("0")) { // Error happens
                    Toast.makeText(getContext(), "An unexpected error occurs.\nPlease try again!", Toast.LENGTH_LONG).show();
                } else { // Register user finished, start to register Type
                    // Query for the userID which has just been created
                    query = String.format("select `id` from `user` where username='%s'", usernameString);
                    document = Jsoup.connect(SERVER + query).get();
                    queryJson = document.body().html();
                    JSONArray queryResultArr = new JSONArray(queryJson);
                    JSONObject queryResultObj = queryResultArr.getJSONObject(0);
                    String userid = queryResultObj.getString("id"); // This is the ID we want to get
                    // Start inserting that ID to the table of the corresponding type
                    query = String.format("insert into `%s` (userid) values ('%s')", selectedTypeString, userid);
                    document = Jsoup.connect(SERVER + query).get();
                    queryJson = document.body().html();
                    // Done inserting ID
                    if (queryJson.equals("0")) { // Error happens
                        Toast.makeText(getContext(), "An unexpected error occurs.\nPlease try again!", Toast.LENGTH_LONG).show();
                    } else { // Success
                        // Pass the username to LoginFragment
                        Bundle bundle = new Bundle();
                        bundle.putString("username", usernameString);
                        LoginFragment loginFragment = new LoginFragment();
                        loginFragment.setArguments(bundle);
                        // Switch to LoginFragment
                        cancel();
                        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, loginFragment).commit();
                        Toast.makeText(MainActivity.getActivity(), "Your account has just been created!\nPlease login using your new account.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void cancel() {
        MainActivity.getActivity().onBackPressed();
    }


}
