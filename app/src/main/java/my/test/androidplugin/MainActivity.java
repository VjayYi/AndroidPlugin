package my.test.androidplugin;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadedApk.copyApk(MainActivity.this,"dnplugin-debug.apk");
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class activityClass=null;
                try {
                    activityClass = Class.forName("com.test.my.DnActivity");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if(activityClass!=null){
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,activityClass);
                    startActivity(intent);
                }

            }
        });
    }

}
