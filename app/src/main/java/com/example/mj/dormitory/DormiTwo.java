package com.example.mj.dormitory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import cn.edu.pku.mj.dormitory.room;
import cn.edu.pku.mj.util.CheckNet;

/**
 * Created by MJ on 2017/12/21.
 */

public class DormiTwo extends Activity implements View.OnClickListener {
    EditText username, vcode, buildnum;
    Button button;
    private String xuehao;
    private Logger logger;
    private final static int UPDATE_TODAY_WEATHER=1;


/*
    private android.os.Handler handler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            String str = (String) msg.obj;
            new AlertDialog.Builder(DormiTwo.this).setTitle("提示").setMessage("Ffddf"+xuehao).setPositiveButton("返回", null).show();
        }
    };

*/

    private android.os.Handler handler=new android.os.Handler(){
        public void handleMessage(Message msg){
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    String www = (String) msg.obj;
                    if(www.equals("{\"errcode\":0}")) {
                        Intent i=new Intent(DormiTwo.this,SearMes.class);
                        i.putExtra("number",xuehao);
                        startActivity(i);
                    }
                    else {
                        new AlertDialog.Builder(DormiTwo.this).setTitle("提示").setMessage("填写错误").setPositiveButton("返回", null).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dortwo);

        button = (Button) findViewById(R.id.loginB);
        button.setOnClickListener(this);

        if (CheckNet.getNetState(this) == CheckNet.NET_NONE) {
            Log.d("MDOR", "网络不通");
            Toast.makeText(DormiTwo.this, "网络不通", Toast.LENGTH_LONG).show();
        } else {
            Log.d("MDOR", "网络OK");
            Toast.makeText(DormiTwo.this, "网络OK", Toast.LENGTH_LONG).show();
        }
        initview();
    }

    public void initview() {
        username = (EditText) findViewById(R.id.usernameET);
        vcode = (EditText) findViewById(R.id.passwordET);
        buildnum = (EditText) findViewById(R.id.builnumET);
        Intent i = getIntent();
        if (i != null) {
            xuehao=i.getStringExtra("xuehao");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginB:
                connectdtwo();
                break;
            default:
                break;
        }
    }


    public void connectdtwo() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                room room=null;
                logger=Logger.getLogger("net");
                byte[] data=getRequestData().toString().getBytes();
                try {
                    trustAllHttpsCertificates();
                    HostnameVerifier hv = new HostnameVerifier() {
                        public boolean verify(String urlHostName, SSLSession session) {
                            logger.info("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
                            return true;
                        }
                    };
                    HttpsURLConnection.setDefaultHostnameVerifier(hv);

                    URL url = new URL("https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom");//设置连接超时时间


                    Log.d("b", url.toString());
                    con = (HttpsURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setConnectTimeout(8000);
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.setUseCaches(false);
                    OutputStream outputStream=con.getOutputStream();
                    outputStream.write(data);
                    int response=con.getResponseCode();
                    if(response==con.HTTP_OK){
                        InputStream inputStream=con.getInputStream();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder zzz=new StringBuilder();
                        String str;
                        while((str=reader.readLine())!=null){
                            zzz.append(str);
                            Log.d("b",str);
                        }

                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = zzz.toString();
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }


    public StringBuffer getRequestData(){
        StringBuffer stringBuffer=new StringBuffer();
        try{
            stringBuffer.append("stuid").append("=").append(xuehao).append("&");
            stringBuffer.append("stu1id").append("=").append(username.getText().toString()).append("&");
            stringBuffer.append("v1cod").append("=").append(vcode.getText().toString()).append("&");
            stringBuffer.append("buildingNo").append("=").append(buildnum.getText().toString()).append("=");
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }catch(Exception e){
            e.printStackTrace();
        }
        return stringBuffer;
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