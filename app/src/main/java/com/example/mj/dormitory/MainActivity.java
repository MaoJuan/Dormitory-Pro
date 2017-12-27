package com.example.mj.dormitory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import cn.edu.pku.mj.util.CheckNet;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button login;
    EditText username;
    EditText password;
    private Logger logger;
    String result;

    private Handler handler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            String str = (String) msg.obj;
            new AlertDialog.Builder(MainActivity.this).setTitle("提示").setMessage("学号或密码错误").setPositiveButton("返回", null).show();
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Log.d("abc","dsfsd");
        login= (Button) findViewById(R.id.loginB);
        login.setOnClickListener(this);
        if(CheckNet.getNetState(this)==CheckNet.NET_NONE){
            Log.d("MDOR","网络不通");
            Toast.makeText(MainActivity.this,"网络不通",Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("MDOR","网络OK");
            Toast.makeText(MainActivity.this,"网络OK",Toast.LENGTH_LONG).show();
        }
        initView();
    }
    private void initView() {
        username = (EditText) findViewById(R.id.usernameET);
        password = (EditText) findViewById(R.id.passwordET);
        result=null;
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.loginB){
            infoGet();
            //Toast.makeText(MainActivity.this,"xxxxx",Toast.LENGTH_LONG).show();
        }

    }
    public void infoGet() {
        final String address="https://api.mysspku.com/index.php/V1/MobileCourse/Login?username="+username.getText().toString()+"&password="+password.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection con = null;
                logger = Logger.getLogger("net");
                try {
                   trustAllHttpsCertificates();
                    HostnameVerifier hv = new HostnameVerifier() {
                        public boolean verify(String urlHostName, SSLSession session) {
                            logger.info("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                            return true;
                        }
                    };
                   HttpsURLConnection.setDefaultHostnameVerifier(hv);
                    URL u=new URL(address);
                    con = (HttpsURLConnection) u.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    InputStream in = con.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("abc", str);
                    }
                    result=response.toString();
                    result=result.substring(11,12);
                    Log.d("abc", result);
                    if(result.equals("0")){
                        Intent i=new Intent(MainActivity.this, SearMes.class);
                        Log.d("abc",username.getText().toString());
                        i.putExtra("number",username.getText().toString());

                        startActivity(i);
                    }else {
                        Log.d("abc","为什么");
                    Message msg = new Message();
                    msg.obj = "Hello";
                    handler.sendMessage(msg);
                   }
                }catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    if(con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }
    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}