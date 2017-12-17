package com.example.mj.dormitory;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import cn.edu.pku.mj.util.CheckNet;

/**
 * Created by MJ on 2017/12/16.
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if(CheckNet.getNetState(this)==CheckNet.NET_NONE){
            Log.d("MDOR","网络不通");
            Toast.makeText(MainActivity.this,"网络不通",Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("MDOR","网络OK");
            Toast.makeText(MainActivity.this,"网络OK",Toast.LENGTH_LONG).show();
        }
    }
}