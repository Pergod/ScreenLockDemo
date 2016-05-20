package com.anttech.screenlockdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button setButton;
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setButton= (Button) findViewById(R.id.set_button);
        testButton= (Button) findViewById(R.id.test_button);

        setButton.setOnClickListener(this);
        testButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_button:
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                break;
            case R.id.test_button:
                startActivity(new Intent(MainActivity.this,TestActivity.class));
                break;
        }
    }
}
