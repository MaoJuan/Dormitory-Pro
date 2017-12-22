package com.example.mj.dormitory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.pku.mj.util.CheckNet;

/**
 * Created by MJ on 2017/12/16.
 */

public class MainActivity extends Activity {
    private Button login;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login=(Button)findViewById(R.id.loginB);
        username=(EditText)super.findViewById(R.id.usernameET);
        password=(EditText)super.findViewById(R.id.passwordET);
        login.setOnClickListener(new View.OnClickListener()
        {   public void onClick(View v)
            {
                if(username.getText().toString().equals("1301210899")&&password.getText().toString().equals("111111"))
                {
                    Toast.makeText(getApplicationContext(),"登陆成功",Toast.LENGTH_SHORT).show();
                    Intent t=new Intent(MainActivity.this,SearMes.class);
                    startActivity(t);
                }
                if(!username.getText().toString().equals("1301210899")||!password.getText().toString().equals("111111"))
                {
                    Toast.makeText(getApplicationContext(),"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        }
        );


        if(CheckNet.getNetState(this)==CheckNet.NET_NONE){
            Log.d("MDOR","网络不通");
            Toast.makeText(MainActivity.this,"网络不通",Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("MDOR","网络OK");
            Toast.makeText(MainActivity.this,"网络OK",Toast.LENGTH_LONG).show();
        }
    }

    private void getPersonDatafromNet(){









    }









}