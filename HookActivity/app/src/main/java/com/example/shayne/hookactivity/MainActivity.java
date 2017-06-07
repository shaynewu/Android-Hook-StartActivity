package com.example.shayne.hookactivity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    public static final String TAG  =  "MainActivity";
    public TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.start);
        tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    Bundle bundle = new Bundle();
                    Log.i(TAG, "-------------------------------->");
                    Log.i(TAG, "startActivity before");
                    Log.i(TAG, "-------------------------------->");

                    startActivity(intent, bundle);

                    Log.i(TAG, "-------------------------------->");
                    Log.i(TAG, "startActivity after");
                    Log.i(TAG, "-------------------------------->");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } );
    }
}
