package com.example.mj.dormitory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import cn.edu.pku.mj.util.CheckNet;
import cn.edu.pku.mj.person.student;
import cn.edu.pku.mj.dormitory.room;

/**
 * Created by MJ on 2017/12/25.
 */

public class SearMes extends Activity implements View.OnClickListener {
    private final static int EMPTYBED_COUNT=1;
    private final static int STUDENT_MES=2;
    Button but1,but2;
    private ListView list;
    public String number;
    private Logger logger;
    private int gender;
    private ArrayAdapter<String> adapter;





    private Handler handler=new Handler(){
      public void handleMessage(Message msg){
          switch (msg.what){
              case EMPTYBED_COUNT:
                  room rr=(room)msg.obj;
                  new AlertDialog.Builder(SearMes.this).setTitle("剩余空床数").setMessage("5号楼 剩余空床数："+rr.getR5()+"\n13号楼 剩余空床数："+rr.getR13()+"\n14号楼 剩余空床数："+rr.getR14()+"\n8号楼 剩余空床数："+rr.getR8()+"\n9号楼 剩余空床数："+rr.getR9()).setPositiveButton("返回",null).show();
                  break;

              case STUDENT_MES:
                  student ss=(student)msg.obj;
                  update(ss);
                  break;
              default:
                  break;
          }
      }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchmes);
        but1=(Button)findViewById(R.id.emptybedB);
        but1.setOnClickListener(this);
        but2=(Button)findViewById(R.id.dorcategoryB);
        but2.setOnClickListener(this);

        if(CheckNet.getNetState(this)==CheckNet.NET_NONE){
            Log.d("MDOR","网络不通");
            Toast.makeText(SearMes.this,"网络不通",Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("MDOR","网络OK");
            Toast.makeText(SearMes.this,"网络OK",Toast.LENGTH_LONG).show();
        }
        initview();
    }

    public void initview(){
        but1=(Button)findViewById(R.id.emptybedB);
        but2=(Button)findViewById(R.id.dorcategoryB);
        list=(ListView) findViewById(R.id.personmesLV);
        number=null;
        Intent i=getIntent();
        if(i != null){
            number=i.getStringExtra("number");
        }
        connectMes(number);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.emptybedB:
                connectEmptybed();
                break;
            case R.id.dorcategoryB:
                Intent i=new Intent(SearMes.this,DormiCat.class);
                i.putExtra("number",number);
                startActivity(i);
                break;
        }
    }

    public void connectMes(final String number) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                student student=null;
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
                    URL url = new URL("https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?stuid="+number);
                    con = (HttpsURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    con.connect();
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    student=parsemes(response.toString());
               if(student!=null){
                        Message msg=new Message();
                        msg.what=STUDENT_MES;
                        msg.obj=student;
                        handler.sendMessage(msg);
                    }
            } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(con!=null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }



    public void connectEmptybed(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpsURLConnection con = null;
                room room=null;
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
                    URL u = new URL("https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?gender=" + gender);
                    con = (HttpsURLConnection)u.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                    }

                    room=parseemptybed(response.toString());

                    if(room!=null){
                        Message msg=new Message();
                        msg.what=EMPTYBED_COUNT;
                        msg.obj=room;
                        handler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
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





    public room parseemptybed(String xmldata){

        room r=null;
        String[] s=xmldata.split("\"");

        for(int i=0;i<s.length;i++){

            if (s[i].equals(":0,")){
                r=new room();
            }
            if (s[i].equals("5")){
                i=i+1;
                r.setR5(s[i].substring(1,s[i].length()));
            }
            if (s[i].equals("13")){
                i=i+1;
                r.setR13(s[i].substring(1,s[i].length()));
            }
            if (s[i].equals("14")){
                i=i+1;
                r.setR14(s[i].substring(1,s[i].length()));
            }
            if (s[i].equals("8")){
                i=i+1;
                r.setR8(s[i].substring(1,s[i].length()));
            }
            if (s[i].equals("9")){
                i=i+1;
                r.setR9(s[i].substring(1,s[i].length()-2));
            }
        }
        return r;
    }

    public student parsemes(String xmldata){

        student stu=null;
        String[] s=xmldata.split("\"");
        for(int i=0;i<s.length;i++){
            if(s[i].equals(":0,")){
                stu=new student();
            }
            if(s[i].equals("studentid")){
                i=i+2;
                stu.setStudentid(s[i]);
            }
            if(s[i].equals("name")){
                i=i+2;
                stu.setName(s[i]);
            }
            if(s[i].equals("gender")){
                i=i+2;
                stu.setGender(s[i]);
                if(s[i].equals("女")){
                    gender=2;
                }else{
                    gender=1;
                }
            }
            if(s[i].equals("vcode")){
                i=i+2;
                stu.setVcode(s[i]);
            }
            if(s[i].equals("location")){
                i=i+2;
                stu.setLocation(s[i]);
            }
            if(s[i].equals("grade")){
                i=i+2;
                stu.setGrade(s[i]);
            }
        }
        return stu;
    }

    public void update(student s){
        List<String> data=new ArrayList<String>();
        data.add("学号："+s.getStudentid());
        data.add("姓名："+s.getName());
        data.add("性别："+s.getGender());
        data.add("校验码："+s.getVcode());
        if(s.getRoom()==null){data.add("宿舍号：");}else{data.add("宿舍号："+s.getRoom());};
        if(s.getBuilding()==null){data.add("楼号：");}else{data.add("楼号："+s.getBuilding());};
        data.add("校区："+s.getLocation());
        data.add("年级："+s.getGrade());
        int size=data.size();
        String[] student_list=(String[])data.toArray(new String[size]);
        adapter=new ArrayAdapter<String>(SearMes.this,android.R.layout.simple_list_item_1, student_list);
        list.setAdapter(adapter);
    }
}








