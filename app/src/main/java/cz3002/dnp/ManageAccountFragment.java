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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by hizac on 23/2/2016.
 */
public class ManageAccountFragment extends Fragment implements Constants {
    ViewGroup rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_manage_account, container, false);

        // Get all form's elements
        EditText username = (EditText) rootView.findViewById(R.id.usernameField);
        EditText password = (EditText) rootView.findViewById(R.id.passwordField);
        EditText email = (EditText) rootView.findViewById(R.id.emailField);
        EditText fullname = (EditText) rootView.findViewById(R.id.nameField);

        // Fill in the form
        username.setText(MainActivity.getActivity().currentUser.getUsername());
        username.setEnabled(false);
        password.setText(MainActivity.getActivity().currentUser.getPassword());
        email.setText(MainActivity.getActivity().currentUser.getEmail());
        fullname.setText(MainActivity.getActivity().currentUser.getFullname());

        // Cancel button
        Button cancelBtn = (Button) rootView.findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        // Log out button
        Button logoutBtn = (Button) rootView.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Save changes button
        Button saveBtn = (Button) rootView.findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        return rootView;
    }

    private void cancel() {
        MainActivity.getActivity().onBackPressed();
    }

    private void logout() {
        // Pass the username to LoginFragment
        Bundle bundle = new Bundle();
        bundle.putString("username", MainActivity.getActivity().currentUser.getUsername());
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(bundle);
        // Erase user information
        UserCtrl userCtrl = new UserCtrl();
        userCtrl.setUserInfo(MainActivity.getActivity().currentUser,-1,null,null,null,null,false);
        cancel();
        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new LoginFragment()).commit();
        Toast.makeText(MainActivity.getActivity(), "You have logged out!", Toast.LENGTH_LONG).show();
    }

    private void saveChanges() {
        // Get info from the form
        EditText password = (EditText) rootView.findViewById(R.id.passwordField);
        EditText email = (EditText) rootView.findViewById(R.id.emailField);
        EditText fullname = (EditText) rootView.findViewById(R.id.nameField);
        String passwordString = password.getText().toString();
        String emailString = email.getText().toString();
        String fullnameString = fullname.getText().toString();
        int id = MainActivity.getActivity().currentUser.getId();
        String usernameString = MainActivity.getActivity().currentUser.getUsername();
        boolean type = MainActivity.getActivity().currentUser.isDoctor();

        // Update to server
        try {
            String query = String.format("update `user` set password='%s', email='%s', fullname='%s' where id=%d", passwordString, emailString, fullnameString, id);
            Document document = Jsoup.connect(SERVER + query).get();
            String queryJson = document.body().html();

            // Check if task fails
            if (queryJson.equals("0")) {
                Toast.makeText(getContext(), "An unexpected error occurs.\nPlease try again!", Toast.LENGTH_LONG).show();
                return;
            }

            // Otherwise, if success, change the current user correspondingly
            UserCtrl userCtrl = new UserCtrl();
            userCtrl.setUserInfo(MainActivity.getActivity().currentUser, id, usernameString, passwordString, emailString, fullnameString, type);

            // Notify user
            Toast.makeText(getContext(), "Changes saved!", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
