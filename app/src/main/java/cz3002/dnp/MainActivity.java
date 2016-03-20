package cz3002.dnp;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;


public class MainActivity extends AppCompatActivity {
    private static MainActivity activity;
    public static MainActivity getActivity(){
        return activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MainActivity.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new LoginFragment()).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        MainActivity.getActivity().getSupportFragmentManager().popBackStack();
    }

}
