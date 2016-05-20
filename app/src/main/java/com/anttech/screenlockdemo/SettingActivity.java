package com.anttech.screenlockdemo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hyper on 2016/5/19.
 */
public class SettingActivity extends Activity implements View.OnClickListener{
    private Button setButton;
    private Button resetButton;
    private LockView mLockView;
    private List<Integer> passList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_view);

        setButton= (Button) findViewById(R.id.save_code);
        resetButton= (Button) findViewById(R.id.reset_code);
        mLockView= (LockView) findViewById(R.id.setting_lock_view);

        mLockView.setOnDrawFinishListener(new LockView.onDrawFinishListener() {

            @Override
            public boolean onDrawFinish(List<Integer> passList) {
                if (passList.size()<3){
                    Toast.makeText(SettingActivity.this,"密码不能小于3位",Toast.LENGTH_SHORT).show();
                    return false;
                }else {
                    SettingActivity.this.passList= passList;
                    return true;
                }
            }
        });

        setButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_code:
                if (!passList.isEmpty()){
                    StringBuilder sb=new StringBuilder();
                    for (Integer i:passList) {
                        sb.append(i);
                    }
                    SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putString("password",sb.toString());
                    editor.apply();
                }
                Toast.makeText(SettingActivity.this,"保存完毕",Toast.LENGTH_SHORT).show();
                break;
            case R.id.reset_code:
                mLockView.resetPoints();
                break;
        }
    }
}
