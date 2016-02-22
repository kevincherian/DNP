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

import cz3002.dnp.R;

/**
 * Created by hizac on 23/2/2016.
 */
public class LoginFragment extends Fragment{
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
//                logIn();
            }
        });
        loginBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                logIn();
                return true;
            }
        });
        return rootView;
    }

    private void logIn() {
        Toast.makeText(getContext(),"Login",Toast.LENGTH_SHORT).show();
    }

    private void signUp() {
        EditText username = (EditText) rootView.findViewById(R.id.usernameField);
        String usernameString = username.getText().toString();
        EditText password = (EditText) rootView.findViewById(R.id.passwordField);
        String passwordString = password.getText().toString();
        Toast.makeText(getContext(),String.format("Username: %s\nPassword: %s",usernameString,passwordString),Toast.LENGTH_SHORT).show();
    }
}
