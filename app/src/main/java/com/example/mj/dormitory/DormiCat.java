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
import android.widget.Toast;

import java.io.InputStream;

import cn.edu.pku.mj.util.CheckNet;

/**
 * Created by MJ on 2017/12/21.
 */

public class DormiCat extends Activity implements View.OnClickListener{
    Button b2,b3,b4;
    private String xuehao;


   /* private android.os.Handler handler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            String str = (String) msg.obj;
            new AlertDialog.Builder(DormiCat.this).setTitle("提示").setMessage("Ffddf"+xuehao).setPositiveButton("返回", null).show();
        }
    };*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dorcat);
        b2=(Button)findViewById(R.id.twoB);
        b2.setOnClickListener(this);
        b3=(Button)findViewById(R.id.threeB);
        b3.setOnClickListener(this);
        b4=(Button)findViewById(R.id.fourB);
        b4.setOnClickListener(this);

        if(CheckNet.getNetState(this)==CheckNet.NET_NONE){
            Log.d("MDOR","网络不通");
            Toast.makeText(DormiCat.this,"网络不通",Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("MDOR","网络OK");
            Toast.makeText(DormiCat.this,"网络OK",Toast.LENGTH_LONG).show();
        }
        initview();
    }

    public void initview(){
        xuehao=null;
        Intent ii=getIntent();
        if(ii!=null){
            xuehao=ii.getStringExtra("number");
        }
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.twoB:
                Intent i1=new Intent(DormiCat.this,DormiTwo.class);
                i1.putExtra("xuehao",xuehao);
                startActivity(i1);
                break;
            case R.id.threeB:
                Intent i2=new Intent(DormiCat.this,DormiThree.class);
                i2.putExtra("xuehao",xuehao);
                startActivity(i2);
                break;
            case R.id.fourB:
                Intent i3=new Intent(DormiCat.this,DormiFour.class);
                i3.putExtra("xuehao",xuehao);
                startActivity(i3);
                break;
        }

    }
}
