package com.test.my;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by YiVjay
 * on 2020/4/17
 */
public class Dn2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        TextView tv=findViewById(R.id.tv);
        tv.setText("Dn2Activity");
    }

    public void whoToast(View view) {
        Toast.makeText(Dn2Activity.this,"Activity="+Dn2Activity.this.getClass(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public Resources getResources() {
        return getApplication() != null && getApplication().getResources() != null ? getApplication().getResources() : super.getResources();
    }
}

