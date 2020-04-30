package com.test.my;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by YiVjay
 * on 2020/4/17
 */
public class DnActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        TextView tv=findViewById(R.id.tv);
        tv.setText("DnActivity");
    }



    @Override
    public Resources getResources() {
        return getApplication() != null && getApplication().getResources() != null ? getApplication().getResources() : super.getResources();
    }

    public void whoToast(View view) {
        Intent intent=new Intent();
        intent.setClass(DnActivity.this,Dn2Activity.class);
        startActivity(intent);
    }
}

