package com.relsellglobal.surveyor.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class TempResponse extends AppCompatActivity {

    TextView mResponseStringTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_response);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mResponseStringTv = (TextView)findViewById(R.id.responseString);
        setSupportActionBar(toolbar);

        String responseString = getIntent().getStringExtra("responseString");

        mResponseStringTv.setText(responseString);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

}
