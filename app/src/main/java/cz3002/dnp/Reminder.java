package cz3002.dnp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Reminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder2);

        String reminder = ""+getIntent().getStringExtra("reminder");
        TextView text = (TextView) findViewById(R.id.reminder);
        text.setText(reminder);
    }
}
