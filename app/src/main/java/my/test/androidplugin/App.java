package my.test.androidplugin;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by YiVjay
 * on 2020/4/17
 */
public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            HookHelper.initHook(base);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Resources getResources() {
        return LoadedResource.getResources()==null?super.getResources():LoadedResource.getResources();
    }
}
