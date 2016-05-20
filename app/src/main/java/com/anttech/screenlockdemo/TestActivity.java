package com.anttech.screenlockdemo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Hyper on 2016/5/19.
 */
public class TestActivity extends Activity {
    private LockView mLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_view);

        mLockView= (LockView) findViewById(R.id.test_lock_view);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        final String password=sp.getString("password","");

        mLockView.setOnDrawFinishListener(new LockView.onDrawFinishListener() {

            @Override
            public boolean onDrawFinish(List<Integer> passList) {
                StringBuilder sb=new StringBuilder();
                for (Integer i:passList) {
                    sb.append(i);
                }
                if (password.equals(sb.toString())){
                    Toast.makeText(TestActivity.this,"密码正确",Toast.LENGTH_SHORT).show();
                    return true;
                }
                else {
                    Toast.makeText(TestActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
    }
}
